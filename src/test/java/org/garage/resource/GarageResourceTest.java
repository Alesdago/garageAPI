package org.garage.resource;

import org.garage.Auto;
import org.garage.Condizione;
import org.garage.db.DBMongo;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


@QuarkusTest
@TestHTTPEndpoint(GarageResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GarageResourceTest {
	
	@Inject
	DBMongo garage;
	
	Auto autoTest = new Auto("test","test","test");
	
	public int recuperaId() {
		String p = "test";
		List<String> l = new ArrayList<String>();
		l.add(p);
		Condizione c = new Condizione("colore",l);
		List<Condizione> ci = new ArrayList<Condizione>();
		ci.add(c);
		
		int id = garage.ricerca(ci).get(0).getId();
		
		return id;
	}
	

	@Test
	@Order(1)
	public void testAggiungiAuto() {
		given().contentType(ContentType.JSON).body(autoTest)
		.when().post()
		.then().statusCode(204);
	}
	
	
	@Test
	@Order(2)
	public void testGetAuto() {
		String url = "/auto/" + recuperaId();
		given()
		.when().get(url)
		.then().statusCode(200)
		.body("colore", is("test"),
				"modello", is("test"),
				"marca", is("test"),
				"id", is(recuperaId()));
		
	}
	
	@Test
	@Order(3)
	public void testRicerca() {
		given().contentType(ContentType.JSON).body("[{\"campo\": \"colore\",\"parametri\": [ \"test\"]},"
				+ "{\"campo\":\"modello\",\"parametri\":[\"test\"]}]")
		.when().post("/ricerca")
		.then().statusCode(200)
		.body("$.size()", is(1),
				"[0].colore", is("test"),
				"[0].modello",is("test"),
				"[0].marca", is("test"),
				"[0].id",is(recuperaId()));
	}
	
	@Test
	@Order(4)
	public void testRimuoviAuto() {
		String url = "/garage/" + recuperaId();
		given()
		.when().delete(url)
		.then().statusCode(404);
		
	}
	
	@Test
	@Order(5)
	public void testModificaGarage() {
		String url = "/auto/" + recuperaId();
		given().contentType(ContentType.JSON).body(new Auto("test","testMG","testMG"))
		.when().put(url)
		.then().statusCode(200);
	}
	
	@Test
	public void testModificaAuto() {
		String url = "/auto/" + recuperaId() +"/modifica-colore/testMA";
		given()
		.when().patch(url)
		.then().statusCode(200);
	}
}
