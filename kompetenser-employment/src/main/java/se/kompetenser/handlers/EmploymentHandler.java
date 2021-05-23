package se.kompetenser.handlers;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
import io.vertx.sqlclient.Tuple;
import se.kompetenser.competence.dto.EmploymentDto;
import se.kompetenser.util.PathUtil;

public class EmploymentHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmploymentHandler.class);
	
	DateTimeFormatter formatter =
		    DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone( ZoneId.systemDefault() );

	private MySQLPool client;

	private static final String SQL = "SELECT ID, EMPLOYMENT_DATE, END_DATE, USERACCOUNT_ID, COMPANY_ID FROM EMPLOYMENT";
	
	private static final String WHERE = "   WHERE e.COMPANY_ID = ? \n";
	
	private static final String LIMIT = " LIMIT ?,?";
	
	private static String EMPLOYMENT = "SELECT \n" + 
			"	    e.EMPLOYMENT_DATE, \n" + 
			"	    u.FIRST_NAME, \n" + 
			"	    u.LAST_NAME, \n" + 
			"	    c.CITY_NAME\n" + 
			"	FROM\n" + 
			"	    EMPLOYMENT e\n" + 
			"	INNER JOIN USERACCOUNT u \n" + 
			"	    ON e.USERACCOUNT_ID = u.ID \n" +
			"   LEFT OUTER JOIN CITY c \n" +
			"       ON u.CITY_ID = c.ID \n";
	
	private static final String COMPETENCE_JOIN = "   LEFT OUTER JOIN USERCOMPETENCE uc \n" +
			"       ON u.ID = uc.USERACCOUNT_ID \n";

	public EmploymentHandler(MySQLPool client) {
		this.client = client;
	}

	public void findAll(RoutingContext context) {
		// Get a connection from the pool
		client.getConnection(ar1 -> {

			if (ar1.succeeded()) {

				// Obtain our connection
				SqlConnection conn = ar1.result();

				// All operations execute on the same connection
				conn.query("SELECT * FROM EMPLOYMENT").execute(ar2 -> {
					if (ar2.succeeded()) {
						JsonArray competencies = this.getEmployees(ar2.result());
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
	
	public void getByCompanyId(RoutingContext context) {
		
		String SQL = EMPLOYMENT;
		Long companyId = PathUtil.parseLongParam(context.pathParam("companyId"));
	    if (companyId == null) {
	    	context.next();
	      return;
	    }
	    Long start = PathUtil.parseLongParam(context.pathParam("start"));
		Long startRow = start == null ? 0L : start;
	    Long count = PathUtil.parseLongParam(context.pathParam("count"));
	    Long rowCount = count == null ? 10L : count;
	    
	    HttpServerRequest request = context.request();
	    String competence = request.getParam("competence");
	    boolean isWhereAdded = false;
	    if (competence != null) {
	    	SQL = SQL.concat(COMPETENCE_JOIN).concat(WHERE);
	    	SQL = SQL.concat(" AND uc.COMPETENCE_ID IN (" + competence + ") ");
	    	isWhereAdded = true;
	    }
	    
	    String city = request.getParam("city");
	    if (city != null) {
	    	if (competence == null) {
	    		SQL = SQL.concat(WHERE);
	    		isWhereAdded = true;
			}
			SQL = SQL.concat(" AND c.ID IN (" + city + ") ");
		}
	    
	    if (!isWhereAdded) {
	    	SQL = SQL.concat(WHERE);
		}
	    
	    final String SQL_SRT = SQL.concat(LIMIT);
		
		client
		  .preparedQuery(SQL_SRT)
		  .execute(Tuple.of(companyId, startRow, rowCount), ar -> {
		  if (ar.succeeded()) {
			  JsonArray employees = this.getEmployees(ar.result());
				context.response().setStatusCode(200)
						.putHeader("content-type", "application/json; charset=utf-8")
						.putHeader("Access-Control-Allow-Origin", "*")
						.end(Json.encodePrettily(employees));
		  } else {
			  LOGGER.info("Could not connect: " + ar.cause().getMessage());
		  }
		});
	}
	
	private JsonArray getEmployees(RowSet<Row> result) {
		JsonArray employees = new JsonArray();
		for (Row row : result) {
			employees.add(this.getEmployee(row));
		}
		return employees;
	}
	
	private JsonObject getEmployee(Row row) {
		EmploymentDto emp = new EmploymentDto();
		emp.setEmploymentDate(row.getLocalDate(0).format(formatter));
		emp.setFirstName(row.getString(1));
		emp.setLastName(row.getString(2));
		emp.setCity(row.getString(3));
		return JsonObject.mapFrom(emp);
	}
}
