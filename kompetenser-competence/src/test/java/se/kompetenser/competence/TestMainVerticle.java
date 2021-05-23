package se.kompetenser.competence;

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
import se.kompetenser.competence.MainVerticle;
import se.kompetenser.competence.dto.Competence;

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
	void testCompetencies(Vertx vertx, VertxTestContext testContext) throws Throwable {
		vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> {
			WebClient client = WebClient.create(vertx);
			client.get(8385, "localhost", "/api/kompetenser/competencies").as(BodyCodec.string())
					.send(testContext.succeeding(response -> testContext.verify(() -> {
						assertEquals(new JsonArray(response.body()), this.getCompetencies());
						testContext.completeNow();
					})));
		}));
	}
	
	@Test
	void testFindCompetence(Vertx vertx, VertxTestContext testContext) throws Throwable {
		vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> {
			WebClient client = WebClient.create(vertx);
			client.get(8385, "localhost", "/api/kompetenser/competencies/Pyt").as(BodyCodec.string())
					.send(testContext.succeeding(response -> testContext.verify(() -> {
						assertEquals(new JsonArray(response.body()), this.getPython());
						testContext.completeNow();
					})));
		}));
	}

	private JsonArray getCompetencies() {
		JsonArray comps = new JsonArray();
		comps.add(this.getCompetence(1, "Java", "Programming language", "https://java.com"));
		comps.add(this.getCompetence(2, "Python", "Programming language", "https://www.python.org/"));
		return comps;
	}

	private JsonObject getCompetence(Integer id, String name, String description, String url) {
		Competence competence = new Competence();
		competence.setCompetenceId(id);
		competence.setCompetenceName(name);
		competence.setCreatedBy(1);
		competence.setChangedBy(1);
		competence.setDescription(description);
		competence.setDescriptionUrl(url);
		return JsonObject.mapFrom(competence);
	}
	
	private JsonArray getPython() {
		return new JsonArray().add(this.getCompetencies().getJsonObject(1));
	}
}
