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
import se.kompetenser.company.dto.Company;

public class CompanyHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyHandler.class);

	private MySQLPool client;

	private static final String SQL = "SELECT ID, COMPANY_NAME, ORGANIZATION_NUMBER FROM COMPANY WHERE COMPANY_NAME LIKE ";

	public CompanyHandler(MySQLPool client) {
		this.client = client;
	}

	public void findAll(RoutingContext context) {
		// Get a connection from the pool
		client.getConnection(ar1 -> {

			if (ar1.succeeded()) {

				// Obtain our connection
				SqlConnection conn = ar1.result();

				// All operations execute on the same connection
				conn.query("SELECT * FROM COMPANY").execute(ar2 -> {
					if (ar2.succeeded()) {
						JsonArray cities = this.getCompanies(ar2.result());
						context.response().setStatusCode(200)
								.putHeader("content-type", "application/json; charset=utf-8")
								.putHeader("Access-Control-Allow-Origin", "*")
								.end(Json.encodePrettily(cities));
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
	
	public void findCompany(RoutingContext context) {
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
						JsonArray companies = this.getCompanies(ar2.result());
						context.response().setStatusCode(200)
								.putHeader("content-type", "application/json; charset=utf-8")
								.putHeader("Access-Control-Allow-Origin", "*")
								.end(Json.encodePrettily(companies));
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
	
	private JsonArray getCompanies(RowSet<Row> result) {
		JsonArray companies = new JsonArray();
		for (Row row : result) {
			companies.add(this.getCompany(row));
		}
		return companies;
	}
	
	private JsonObject getCompany(Row row) {
		Company company = new Company();
		company.setCompanyId(row.getInteger(0));
		company.setCompanyName(row.getString(1));
		company.setOrganisationNumber(row.getString(2));
		company.setWebUrl(row.getString(3));
		return JsonObject.mapFrom(company);
	}
}
