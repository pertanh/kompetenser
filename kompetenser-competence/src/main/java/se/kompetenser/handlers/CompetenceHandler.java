package se.kompetenser.handlers;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import se.kompetenser.competence.dto.Competence;

public class CompetenceHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompetenceHandler.class);

	private MySQLPool client;

	private static final String SQL = "SELECT ID, COMPETENCE_NAME, CREATED_BY, LAST_CHANGED_BY, DESCRIPTION, DESCRIPTION_URL FROM COMPETENCE WHERE COMPETENCE_NAME LIKE ";

	public CompetenceHandler(MySQLPool client) {
		this.client = client;
	}

	public void findAll(RoutingContext context) {
		// Get a connection from the pool
		client.getConnection(ar1 -> {

			if (ar1.succeeded()) {

				// Obtain our connection
				SqlConnection conn = ar1.result();

				// All operations execute on the same connection
				conn.query("SELECT * FROM COMPETENCE").execute(ar2 -> {
					if (ar2.succeeded()) {
						JsonArray competencies = this.getCompetencies(ar2.result());
						context.response().setStatusCode(200)
								.putHeader("content-type", "application/json; charset=utf-8")
								.putHeader("Access-Control-Allow-Origin", "*")
								.end(Json.encodePrettily(competencies));
						// Release the connection to the pool
						conn.close();
					} else {
						// Release the connection to the pool
						conn.close();
					}
				});
			} else {
				LOGGER.error("Could not connect: " + ar1.cause().getMessage());
			}
		});
	}
	
	public void findCompetence(RoutingContext context) {
		// Get a connection from the pool
		client.getConnection(ar1 -> {

			if (ar1.succeeded()) {

				// Obtain our connection
				SqlConnection conn = ar1.result();
				HttpServerRequest request = context.request();
			    String name = request.getParam("name");
			    final String param = "'".concat(name).concat("%'");
			    final String SQL_SRT = SQL.concat(param.concat(" LIMIT 5"));

				// All operations execute on the same connection
				conn.query(SQL_SRT).execute(ar2 -> {
					if (ar2.succeeded()) {
						JsonArray competencies = this.getCompetencies(ar2.result());
						context.response().setStatusCode(200)
								.putHeader("content-type", "application/json; charset=utf-8")
								.putHeader("Access-Control-Allow-Origin", "*")
								.end(Json.encodePrettily(competencies));
						// Release the connection to the pool
						conn.close();
					} else {
						// Release the connection to the pool
						conn.close();
					}
				});
			} else {
				LOGGER.error("Could not connect: " + ar1.cause().getMessage());
			}
		});
	}
	
	private JsonArray getCompetencies(RowSet<Row> result) {
		JsonArray companies = new JsonArray();
		for (Row row : result) {
			companies.add(this.getCompetence(row));
		}
		return companies;
	}
	
	private JsonObject getCompetence(Row row) {
		Competence competence = new Competence();
		competence.setCompetenceId(row.getInteger(0));
		competence.setCompetenceName(row.getString(1));
		competence.setCreatedBy(row.getInteger(2));
		competence.setChangedBy(row.getInteger(3));
		competence.setDescription(row.getString(4));
		competence.setDescriptionUrl(row.getString(5));
		return JsonObject.mapFrom(competence);
	}
}
