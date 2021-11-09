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
		LOG.debug("auto " + auto.getMarca() + " " + auto.getModello() + " di colore " + 
				auto.getColore() + ", con id " + auto.getId() + " AGGIUNTA AL DB");
	}
	
	@GET
	@Path("/auto/{id}")
	public Auto getAuto(@PathParam("id")int id) {
		LOG.info("get auto id: " + id);
		return garage.getAuto(id);
	}

	
	@POST
	@Path("/ricerca")
	public List<Auto> ricerca(List<Condizione> condizioni){
		LOG.info("ricerca " + condizioni);
		return garage.ricerca(condizioni);
	}
	
	@DELETE
	@Path("/auto/{id}")
	public Response rimuoviAuto(@PathParam("id") int id) {
		LOG.info("elimina auto tramite id " + id);

		if(garage.getAuto(id)!= null) {
			garage.eliminaAuto(id);
		}
		else {
			LOG.error("nessuna auto con quell'id");
		}
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@PUT
	@Path("/auto/{id}")
	public void modificaGarage(@PathParam("id")Integer id, Auto auto){
		LOG.info("modifica auto " + id);
		garage.modificaGarage(id, auto);

	}
	
	@PATCH
	@Path("/auto/{id}/modifica-{campo}/{parametro}")
	public void modificaAuto(@PathParam("id") int id, @PathParam("campo") String campo, @PathParam("parametro") String parametro) {
		LOG.info("modifica " + campo + " auto " + id + " in " + parametro);
		garage.modificaAuto(id, campo, parametro);
	}

}
