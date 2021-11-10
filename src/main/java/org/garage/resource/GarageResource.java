package org.garage.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.garage.Auto;
import org.garage.Condizione;
import org.garage.db.DBMongo;
import org.jboss.logging.Logger;

@Path("/garage") 
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GarageResource {
	
	@Inject
	DBMongo garage;

	private static final Logger LOG = Logger.getLogger(GarageResource.class);


	@POST
	public void aggiungiAuto(Auto auto){
		garage.aggiungiAuto(auto);
        LOG.debug("Aggiunta dell'auto " + auto.toString() + " al garage");
	}
	
	@GET
	@Path("/auto/{id}")
	public Auto getAuto(@PathParam("id") int id) {
		LOG.info("Get auto con id: " + id);
		return garage.getAuto(id);
	}

	
	@POST
	@Path("/ricerca")
	public List<Auto> ricerca(List<Condizione> condizioni){
		List<Auto> listaRicerca = garage.ricerca(condizioni);
		LOG.debug("Risultato della ricerca \"" + condizioni + "\" : " + listaRicerca);
        return listaRicerca;
	}
	
	@DELETE
	@Path("/auto/{id}")
	public Response rimuoviAuto(@PathParam("id") int id) {
		LOG.info("elimina auto tramite id " + id);

		if(garage.getAuto(id) != null) {
			garage.eliminaAuto(id);
			LOG.info("Rimozione dell'auto riuscita");
			LOG.debug("Rimozione dell'auto con id: " + id + " dal garage");
			return Response.status(Status.NO_CONTENT).build();
		}
		
		LOG.error("Rimozione dell'auto non riuscita, l'id dell'auto da rimuovere non e' esistente");
		return Response.status(Status.NOT_FOUND).build();
	}

	@PUT
	@Path("/auto/{id}")
	public Response modificaGarage(@PathParam("id")Integer id, Auto auto){
		if(garage.getAuto(id) != null) {
			garage.modificaGarage(id, auto);
			LOG.info("Modifica auto riuscita");
			LOG.debug("Modifica dell'auto con id " + id);
			return Response.ok().build();
		}
		
		LOG.error("Modifica dell'auto non riuscita, l'id dell'auto da modificare non e' esistente");
		return Response.status(Status.NOT_FOUND).build();
	}
	
	@PATCH
	@Path("/auto/{id}/modifica-{campo}/{parametro}")
	public Response modificaAuto(@PathParam("id") int id, @PathParam("campo") String campo, @PathParam("parametro") String parametro) {
		
		if(garage.getAuto(id) != null) {
			garage.modificaAuto(id, campo, parametro);
			LOG.info("Modifica auto riuscita");
			LOG.debug("Modifica auto " + campo + " auto " + id + " in " + parametro);
			return Response.ok().build();
		}
		
		LOG.error("Modifica dell'auto non riuscita, l'id dell'auto da modificare non e' esistente");
		return Response.status(Status.NOT_FOUND).build();
	}

}
