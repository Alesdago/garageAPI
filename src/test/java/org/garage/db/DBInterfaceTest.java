package org.garage.db;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.garage.Condizione;
import org.garage.Auto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DBInterfaceTest {

	@Inject
	DBMongo garage;
	Auto auto = new Auto("rosso", "panda","fiat");
	
	@Test
	@Order(1)
	void garage() {
		assertNotNull(garage);
	}
	
	@Test
	@Order(2)
	void testAggiungiAuto() {
		garage.aggiungiAuto(auto);
		Auto auto2 = new Auto("giallo", "bug", "volkswagen");
		garage.aggiungiAuto(auto2);

		assertNotNull(garage.getAuto(auto2.getId()));
		assertNotNull(garage.getAuto(auto2.getId()).getMarca());
		assertNotNull(garage.getAuto(auto2.getId()).getColore());
		assertNotNull(garage.getAuto(auto2.getId()).getModello());

		assertEquals(garage.getAuto(auto2.getId()).getMarca(),auto2.getMarca());
		assertEquals(garage.getAuto(auto2.getId()).getModello(),auto2.getModello());
		assertEquals(garage.getAuto(auto2.getId()).getColore(),auto2.getColore());
		assertEquals(garage.getAuto(auto2.getId()).getId(),auto2.getId());
		
		assert(garage.contiene(auto2));
	}
	
	@Test
	@Order(3)
	void testGetAuto() {
		garage.aggiungiAuto(auto);
		assertNotNull(garage.getAuto(auto.getId()));
		assertNotNull(garage.getAuto(auto.getId()).getMarca());
		assertNotNull(garage.getAuto(auto.getId()).getColore());
		assertNotNull(garage.getAuto(auto.getId()).getModello());

		assertEquals(garage.getAuto(auto.getId()).getMarca(),auto.getMarca());
		assertEquals(garage.getAuto(auto.getId()).getModello(),auto.getModello());
		assertEquals(garage.getAuto(auto.getId()).getColore(),auto.getColore());
		assertEquals(garage.getAuto(auto.getId()).getId(),auto.getId());
	}
	
	@Test
	@Order(4)
	void testEliminaAuto() {
		Auto punto = new Auto("nero", "punto","volkswagen");
		garage.aggiungiAuto(punto);
		garage.eliminaAuto(punto.getId());
		assert(!garage.contiene(punto));
	}
	
	@Test
	@Order(5)
	void testModificaGarage() {
		Auto autoNuova = new Auto("verde", "c1", "citroen");
		garage.modificaGarage(2, autoNuova);
		assertEquals(autoNuova.getId(), garage.getAuto(autoNuova.getId()).getId());
		assertEquals(autoNuova.getColore(), garage.getAuto(autoNuova.getId()).getColore());
		assertEquals(autoNuova.getMarca(), garage.getAuto(autoNuova.getId()).getMarca());
		assertEquals(autoNuova.getModello(), garage.getAuto(autoNuova.getId()).getModello());

	}
	
	
	@Test
	@Order(6)
	void testContiene() {
		Auto autoC = new Auto("grigio", "500", "fiat");
		garage.aggiungiAuto(autoC);
		assert (garage.contiene(autoC));
	}
	
	@Test
	@Order(7)
	void testRicerca() {
		List<String> colori = new ArrayList<String>();
		colori.add("rosso");
		colori.add("verde");
		
		List<String> marche = new ArrayList<String>();
		marche.add("fiat");
		marche.add("volkswagen");
		
		List<String> modelli = new ArrayList<String>();
		modelli.add("panda");
		modelli.add("golf");
		
		Condizione cond1 = new Condizione("colore", colori);
		Condizione cond2 = new Condizione("marca", marche);
		Condizione cond3 = new Condizione("modello", modelli);
		
		List<Condizione> condizioni = new ArrayList<Condizione>();
		condizioni.add(cond1);
		condizioni.add(cond2);
		condizioni.add(cond3);

		for(Auto auto: garage.ricerca(condizioni)) {
			assert((auto.getColore().equalsIgnoreCase("rosso")||auto.getColore().equalsIgnoreCase("verde"))
					&&(auto.getModello().equalsIgnoreCase("panda")|| auto.getModello().equalsIgnoreCase("golf"))
					&&(auto.getMarca().equalsIgnoreCase("fiat")|| auto.getMarca().equalsIgnoreCase("volkswagen")));
			
			assert(!auto.getColore().equals("blu")&& !auto.getModello().equals("500")&& !auto.getMarca().equals("citroen"));
		}
	}

}
