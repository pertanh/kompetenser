package se.kompetenser.city;

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
import se.kompetenser.city.dto.CityDto;

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
	void testCities(Vertx vertx, VertxTestContext testContext) throws Throwable {
		vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> {
			WebClient client = WebClient.create(vertx);
			client.get(8383, "localhost", "/api/kompetenser/cities").as(BodyCodec.string())
					.send(testContext.succeeding(response -> testContext.verify(() -> {
						assertEquals(new JsonArray(response.body()), this.getCities());
						testContext.completeNow();
					})));
		}));
	}
	
	@Test
	void testCityByName(Vertx vertx, VertxTestContext testContext) throws Throwable {
		vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> {
			WebClient client = WebClient.create(vertx);
			client.get(8383, "localhost", "/api/kompetenser/cities/Ali").as(BodyCodec.string())
					.send(testContext.succeeding(response -> testContext.verify(() -> {
						assertEquals(new JsonArray(response.body()), this.getAlingsas());
						testContext.completeNow();
					})));
		}));
	}

	private JsonArray getCities() {
		JsonArray cities = new JsonArray();
		cities.add(this.getCity(1, "Ale"));
		cities.add(this.getCity(2, "Alingsås"));
		cities.add(this.getCity(3, "Alvesta"));
		return cities;
	}

	private JsonObject getCity(Integer id, String name) {
		CityDto city = new CityDto();
		city.setId(id);
		city.setName(name);
		return JsonObject.mapFrom(city);
	}
	
	private JsonArray getAlingsas() {
		return new JsonArray().add(this.getCities().getJsonObject(1));
	}
}
