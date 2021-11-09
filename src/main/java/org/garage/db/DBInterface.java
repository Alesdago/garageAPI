package org.garage.db;

import java.util.List;

import org.garage.Auto;
import org.garage.Condizione;

public interface DBInterface {
	
	Auto getAuto(int id);
	
	List<Auto> ricerca(List<Condizione> condizioni);
	
	void aggiungiAuto(Auto auto);

	void modificaGarage(int chiave, Auto auto);

	void modificaAuto(int id, String campo, String parametro);
	
	void eliminaAuto(int id);
	
	Boolean contiene(Auto auto);
	
}
