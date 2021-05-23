package se.kompetenser.employment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

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
import se.kompetenser.competence.dto.EmployeeDto;
import se.kompetenser.competence.dto.EmploymentDto;

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
	void testEmployeesByCompanyId(Vertx vertx, VertxTestContext testContext) throws Throwable {
		vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> {
			WebClient client = WebClient.create(vertx);
			client.get(8386, "localhost", "/api/kompetenser/employees/1/0/10competence=1,2&city=1").as(BodyCodec.string())
					.send(testContext.succeeding(response -> testContext.verify(() -> {
						assertEquals(new JsonArray(response.body()), this.getEmployments());
						testContext.completeNow();
					})));
		}));
	}
	
	private JsonArray getEmployees() {
		JsonArray employees = new JsonArray();
		employees.add(this.getEmployee(Long.valueOf(1), LocalDate.of(2001, 3, 1), Long.valueOf(1), Long.valueOf(1)));
		employees.add(this.getEmployee(Long.valueOf(2), LocalDate.of(1978, 11, 1), Long.valueOf(2), Long.valueOf(1)));
		return employees;
	}

	private JsonArray getEmployments() {
		JsonArray employees = new JsonArray();
		employees.add(this.getEmployment("2001-03-01", "Putte", "Persson", "Ale"));
		employees.add(this.getEmployment("1978-11-01", "Anna", "Andersson", "Alingsås"));
		return employees;
	}
	
	private JsonObject getEmployee(Long id, LocalDate date, Long userId, Long companyId) {
		EmployeeDto emp = new EmployeeDto();
		emp.setEmploymentId(id);
		emp.setEmploymentDate(date);
		emp.setEmploymentEndDate(null);
		emp.setUserAccountId(userId);
		emp.setCompanyId(companyId);
		return JsonObject.mapFrom(emp);
	}

	private JsonObject getEmployment(String date, String firstName, String lastName, String city) {
		EmploymentDto emp = new EmploymentDto();
		emp.setEmploymentDate(date);
		emp.setFirstName(firstName);
		emp.setLastName(lastName);
		emp.setCity(city);
		return JsonObject.mapFrom(emp);
	}
	
	private JsonArray getPutte() {
		return new JsonArray().add(this.getEmployments().getJsonObject(1));
	}
}
