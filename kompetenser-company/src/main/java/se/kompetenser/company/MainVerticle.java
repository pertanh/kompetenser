package se.kompetenser.company;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.web.Router;
import io.vertx.micrometer.PrometheusScrapingHandler;
import io.vertx.micrometer.backends.BackendRegistries;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import se.kompetenser.handlers.CompanyHandler;

public class MainVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		ConfigRetriever retriever = ConfigRetriever.create(vertx, this.getOptions());
		retriever.getConfig(config -> {
			LOGGER.info("config retrieved");
			if (config.failed()) {
				LOGGER.info("No config");
			} else {
				LOGGER.info("Got config");
				startup(startPromise, config.result());
			}
		});
	}

	private void startup(Promise<Void> startPromise, JsonObject config) {
		MySQLPool client = this.getClient(config);

		CompanyHandler cityHandler = new CompanyHandler(client);

		HttpServer server = vertx.createHttpServer();

		MeterRegistry registry = BackendRegistries.getDefaultNow();
		new ClassLoaderMetrics().bindTo(registry);
		new JvmMemoryMetrics().bindTo(registry);
		new JvmGcMetrics().bindTo(registry);
		new ProcessorMetrics().bindTo(registry);
		new JvmThreadMetrics().bindTo(registry);

		HealthCheckHandler healthCheckHandlerHealth = HealthCheckHandler.create(vertx);
		HealthCheckHandler healthCheckHandlerPing = HealthCheckHandler
				.createWithHealthChecks(HealthChecks.create(vertx));

		Router router = Router.router(vertx);
		/* Metrics */
		router.route("/actuator/prometheus").handler(PrometheusScrapingHandler.create());
		/* Health checks */
		router.route("/actuator/health*").handler(healthCheckHandlerHealth);
		router.route("/actuator/ping*").handler(healthCheckHandlerPing);
		router.get("/api/kompetenser/companies").handler(cityHandler::findAll);
		router.get("/api/kompetenser/companies/:name").handler(cityHandler::findCompany);

		server.requestHandler(router).listen(config.getInteger("port"), ar -> {
			if (ar.succeeded()) {
				startPromise.complete();
			} else {
				LOGGER.error(ar.cause());
				startPromise.fail(ar.cause());
			}
		});
	}

	private MySQLPool getClient(JsonObject config) {
		MySQLConnectOptions connectOptions = new MySQLConnectOptions()
				.setPort(config.getInteger("db-port"))
				.setHost(config.getString("host"))
				.setDatabase(config.getString("schema"))
				.setUser(config.getString("user"))
				.setPassword(config.getString("password"));

		// Pool options
		PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

		// Create the pooled client
		return MySQLPool.pool(vertx, connectOptions, poolOptions);
	}

	private ConfigRetrieverOptions getOptions() {
		ConfigStoreOptions fileStore = new ConfigStoreOptions()
				.setType("file")
				.setConfig(new JsonObject().put("path", "conf.json"));
		return new ConfigRetrieverOptions().addStore(fileStore);
	}
}
