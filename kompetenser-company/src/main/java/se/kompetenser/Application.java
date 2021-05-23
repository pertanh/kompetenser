package se.kompetenser;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxJmxMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import se.kompetenser.company.MainVerticle;

/**
 * Application
 * 
 * @author perhjerten
 * @since 2021-05-23
 */
public class Application {

	public static void main(String[] args) {
		
		MicrometerMetricsOptions options = new MicrometerMetricsOptions()
				.setPrometheusOptions(new VertxPrometheusOptions()
						.setStartEmbeddedServer(true)
						.setEmbeddedServerOptions(new HttpServerOptions().setPort(8081))
						.setEnabled(true))
				.setJmxMetricsOptions(new VertxJmxMetricsOptions().setEnabled(true))
				.setEnabled(true);
		
		Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(options));
        vertx.deployVerticle(new MainVerticle());

	}

}
