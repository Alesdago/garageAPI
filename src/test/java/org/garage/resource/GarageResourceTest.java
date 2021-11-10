package org.garage.resource;

import org.garage.Auto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;


@QuarkusTest
@TestHTTPEndpoint(GarageResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GarageResourceTest {

	@Test
	@Order(1)
	public void testAggiungiAuto() {
		given().contentType(ContentType.JSON).body(new Auto("rosso","panda","fiat"))
		.when().post()
		.then().statusCode(204);
	}
	
	@Test
	@Order(2)
	public void testGetAuto() {
		given()
		.when().get("/auto/1")
		.then().statusCode(200)
		.body("colore", is("rosso"),
				"modello", is("panda"),
				"marca", is("fiat"),
				"id", is(1));
		
	}
	
	@Test
	@Order(3)
	public void testRicerca() {
		given().contentType(ContentType.JSON).body("[{\"campo\": \"colore\",\"parametri\": [ \"rosso\"]},"
				+ "{\"campo\":\"modello\",\"parametri\":[\"panda\"]}]")
		.when().post("/ricerca")
		.then().statusCode(200)
		.body("$.size()", is(1),
				"[0].colore", is("rosso"),
				"[0].modello",is("panda"),
				"[0].marca", is("fiat"),
				"[0].id",is(1));
	}
	
	@Test
	@Order(4)
	public void testRimuoviAuto() {
		given()
		.when().delete("/garage/1")
		.then().statusCode(404);
		
	}
	
	@Test
	@Order(5)
	public void testModificaGarage() {
		given().contentType(ContentType.JSON).body(new Auto("nero","a1","audi"))
		.when().put("/auto/1")
		.then().statusCode(200);
	}
	
	@Test
	public void testModificaAuto() {
		given()
		.when().patch("/auto/1/modifica-colore/bianco")
		.then().statusCode(200);
	}
}
