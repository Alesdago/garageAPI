package org.garage.db;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.garage.Auto;
import org.garage.Condizione;
import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;

@ApplicationScoped
public class DBMongo implements DBInterface{
	
	@Inject
	MongoClient mongoClient;
	
	private static final Logger LOG = Logger.getLogger(DBMongo.class);

	@Override
	public Auto getAuto(int id) {
		
		Gson gson = new Gson();
		try {
			String json = mongoClient.getDatabase("automobili").getCollection("garage")
					.find(new Document("id",id)).first().toJson();
			Auto auto = gson.fromJson(json, Auto.class);
			LOG.debug(json);
			return auto;
		} catch (NullPointerException n) {
			LOG.error("ID " + id + " FUORI RANGE, NULLPOINTER EXCEPTION");
			return null;
		}catch (Exception e) {
			LOG.error(e.getStackTrace());
			return null;
		}
	}

	@Override
	public List<Auto> ricerca(List<Condizione> condizioni) {
		
		List<Auto> lista = new ArrayList<Auto>();
		List<Bson> listaFiltriAnd = new ArrayList<Bson>();

		for(Condizione condizione: condizioni) {
			List<Bson> listaFiltriOr = new ArrayList<Bson>();

			for(String parametro: condizione.getParametri()) {
				Bson filtro = eq(condizione.getCampo(),parametro);
				listaFiltriOr.add(filtro);
			}

			Bson filtroOr = or(listaFiltriOr);
			listaFiltriAnd.add(filtroOr);
		}

		Bson filtroAnd = and(listaFiltriAnd);

		FindIterable<Document> listaDoc = mongoClient.getDatabase("automobili").getCollection("garage").find(filtroAnd);
		

		Gson gson = new Gson();
		String json;
		for(Document doc:listaDoc) {
			json = doc.toJson();
			lista.add(gson.fromJson(json, Auto.class));
		}
		
		return lista;
	}

	@Override
	public void aggiungiAuto(Auto auto) {
		
		Document doc = mongoClient.getDatabase("automobili").getCollection("garage")
				.find().sort(new BasicDBObject("id",-1)).first();
		Gson gson = new Gson();
		if(doc!=null) {
			String json = doc.toJson();
			Auto a = gson.fromJson(json, Auto.class);
			auto.setId(a.getId()+1);
		}

		else {
			auto.setId(1);
			LOG.debug("prima auto nel database, id 1");
		}

		Document document = new Document()
				.append("id", auto.getId())
				.append("colore", auto.getColore())
				.append("modello", auto.getModello())
				.append("marca", auto.getMarca());
		mongoClient.getDatabase("automobili").getCollection("garage").insertOne(document);
		LOG.info("auto aggiunta al database");
		
	}

	@Override
	public void modificaGarage(int chiave, Auto auto) {
		
		Bson filtro = eq("id", chiave);
		try {
			if(!getAuto(chiave).equals(null)) {
				auto.setId(chiave);
				Document documento = new Document()
						.append("id", auto.getId())
						.append("colore", auto.getColore())
						.append("modello", auto.getModello())
						.append("marca", auto.getMarca());
				mongoClient.getDatabase("automobili").getCollection("garage").replaceOne(filtro, documento);
				LOG.info("database modificato");
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void modificaAuto(int id, String campo, String parametro) {
		Bson filtro = eq("id", id);
		Bson operazione = set(campo,parametro);
		try {
			mongoClient.getDatabase("automobili").getCollection("garage").updateOne(filtro, operazione);
		} catch (Exception e) {
			LOG.error(e.getStackTrace());

		}
	}

	@Override
	public void eliminaAuto(int id) {
		try {
			Bson filtro = eq("id", id);
			mongoClient.getDatabase("automobili").getCollection("garage").deleteOne(filtro);	
		}catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}

	@Override
	public Boolean contiene(Auto auto) {
		Document doc = mongoClient.getDatabase("automobili").getCollection("garage")
				.find(new Document("id",auto.getId())).first();
		return (doc!=null);
	}

}
