package br.com.cyop.entity;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import br.com.cyop.service.RestFeature;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Path("/api")
public class InstanceRS extends RestFeature {

	@POST
	@Path("/{entity}")
	public Response create(@PathParam("entity") String entityName, @FormParam("instanceJson") String instanceJson) {
		JsonObject instance = feature(InstanceService.class).createInstance(entityName, instanceJson);
		return Response.ok().entity(new Gson().toJson(instance)).build();
	}

	@GET
	@Path("/{entity}")
	public Response list(@PathParam("entity") String entityName) {
		List<JsonObject> instanciesObjects = feature(InstanceService.class).getListOfInstance(entityName);
		return Response.ok().entity(new Gson().toJson(instanciesObjects)).build();
	}

	@GET
	@Path("/{entity}/{id}")
	public Response fetch(@PathParam("entity") String entity, @PathParam("id") String id) {
		Instance instance = feature(InstanceService.class).getInstanceById(entity, id);
		return Response.ok().entity(new Gson().toJson(instance.object)).build();
	}

	@PUT
	@Path("/{entity}/{id}")
	public Response update(@PathParam("entity") String entity, @PathParam("id") String id, @FormParam("instance") String instanceJson) {
		JsonObject object = feature(InstanceService.class).updateInstance(entity, id, instanceJson);
		return Response.ok().entity(new Gson().toJson(object)).build();
	}

	@DELETE
	@Path("/{entity}/{id}")
	public Response delete(@PathParam("entity") String entityName, @PathParam("id") String id) {
		JsonObject instance = feature(InstanceService.class).deleteEntity(entityName, id);
		return Response.ok().entity(new Gson().toJson(instance)).build();
	}
}
