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
import se.kompetenser.city.dto.City;

public class CityHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CityHandler.class);

	private MySQLPool client;

	private static final String SQL = "SELECT ID, CITY_NAME FROM CITY WHERE CITY_NAME LIKE ";

	public CityHandler(MySQLPool client) {
		this.client = client;
	}

	public void findAll(RoutingContext context) {
		// Get a connection from the pool
		client.getConnection(ar1 -> {

			if (ar1.succeeded()) {

				// Obtain our connection
				SqlConnection conn = ar1.result();

				// All operations execute on the same connection
				conn.query("SELECT * FROM CITY").execute(ar2 -> {
					if (ar2.succeeded()) {
						JsonArray cities = this.getCities(ar2.result());
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
	
	public void findCity(RoutingContext context) {
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
						JsonArray cities = this.getCities(ar2.result());
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
	
	private JsonArray getCities(RowSet<Row> result) {
		JsonArray cities = new JsonArray();
		for (Row row : result) {
			cities.add(this.getCity(row));
		}
		return cities;
	}
	
	private JsonObject getCity(Row row) {
		City city = new City();
		city.setCityId(row.getInteger(0));
		city.setCityName(row.getString(1));
		return JsonObject.mapFrom(city);
	}
}
