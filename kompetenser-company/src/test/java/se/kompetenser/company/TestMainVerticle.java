package se.kompetenser.company;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import se.kompetenser.company.dto.Company;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

	@BeforeEach
	void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
		Vertx.vertx(new VertxOptions()
			      .setMetricsOptions(new MicrometerMetricsOptions()
			        .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true)
			          .setStartEmbeddedServer(true)
			          .setEmbeddedServerOptions(new HttpServerOptions().setPort(9090)))
			        .setEnabled(true)));

		vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
	}

	@Test
	void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
		testContext.completeNow();
	}

	@Test
	void testCompany(Vertx vertx, VertxTestContext testContext) throws Throwable {
		vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> {
			WebClient client = WebClient.create(vertx);
			client.get(8384, "localhost", "/api/kompetenser/companies").as(BodyCodec.string())
					.send(testContext.succeeding(response -> testContext.verify(() -> {
						assertEquals(new JsonArray(response.body()), this.getCompanies());
						testContext.completeNow();
					})));
		}));
	}
	
	@Test
	void testFindCompany(Vertx vertx, VertxTestContext testContext) throws Throwable {
		vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> {
			WebClient client = WebClient.create(vertx);
			client.get(8384, "localhost", "/api/kompetenser/companies/SAF").as(BodyCodec.string())
					.send(testContext.succeeding(response -> testContext.verify(() -> {
						assertEquals(new JsonArray(response.body()), this.getSAFe());
						testContext.completeNow();
					})));
		}));
	}

	private JsonArray getCompanies() {
		JsonArray cities = new JsonArray();
		cities.add(this.getCompany(1, "Cool company Inc", "555555-5555"));
		cities.add(this.getCompany(2, "SAFe Sucks Inc", "333333-3333"));
		return cities;
	}

	private JsonObject getCompany(Integer id, String name, String organizationNumber) {
		Company company = new Company();
		company.setCompanyId(id);
		company.setCompanyName(name);
		company.setOrganisationNumber(organizationNumber);
		company.setWebUrl(null);
		return JsonObject.mapFrom(company);
	}
	
	private JsonArray getSAFe() {
		return new JsonArray().add(this.getCompanies().getJsonObject(1));
	}
}
