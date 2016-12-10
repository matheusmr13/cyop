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
	@Path("/{version}/{entity}")
	public Response create(@PathParam("version") String version, @PathParam("entity") String entityName, @FormParam("instanceJson") String instanceJson) {
		JsonObject instance = feature(InstanceService.class).createInstance(entityName, instanceJson);
		return Response.ok().entity(new Gson().toJson(instance)).build();
	}

	@GET
	@Path("/{version}/{entity}")
	public Response list(@PathParam("version") String version, @PathParam("entity") String entityName) {
		List<JsonObject> instanciesObjects = feature(InstanceService.class).getListOfInstance(entityName);
		return Response.ok().entity(new Gson().toJson(instanciesObjects)).build();
	}

	@GET
	@Path("/{version}/{entity}/{id}")
	public Response fetch(@PathParam("version") String version, @PathParam("entity") String entity, @PathParam("id") Long id) {
		Instance instance = feature(InstanceService.class).getInstanceById(entity, id);
		return Response.ok().entity(new Gson().toJson(instance.object)).build();
	}

	@PUT
	@Path("/{version}/{entity}/{id}")
	public Response update(@PathParam("version") String version, @PathParam("entity") String entity, @PathParam("id") Long id, @FormParam("instance") String instanceJson) {
		JsonObject object = feature(InstanceService.class).updateInstance(entity, id, instanceJson);
		return Response.ok().entity(new Gson().toJson(object)).build();
	}

	@DELETE
	@Path("/{version}/{entity}/{id}")
	public Response delete(@PathParam("version") String version, @PathParam("entity") String entityName, @PathParam("id") Long id) {
		JsonObject instance = feature(InstanceService.class).deleteEntity(entityName, id);
		return Response.ok().entity(new Gson().toJson(instance)).build();
	}

	@GET
	@Path("/{version}/{entity}/{action}")
	public Response getActionOverList(@PathParam("version") String version, @PathParam("entity") String entityName) {
		List<JsonObject> instanciesObjects = feature(InstanceService.class).getListOfInstance(entityName);
		return Response.ok().entity(new Gson().toJson(instanciesObjects)).build();
	}

	@GET
	@Path("/{version}/{entity}/{id}/{action}")
	public Response getActionOverElement(@PathParam("version") String version, @PathParam("entity") String entityName, @PathParam("id") Long id, @PathParam("action") String action) {
		List<JsonObject> instanciesObjects = feature(InstanceService.class).getListOfInstance(entityName);
		return Response.ok().entity(new Gson().toJson(instanciesObjects)).build();
	}

	@PUT
	@Path("/{version}/{entity}/{action}")
	public Response putActionOverList(@PathParam("version") String version, @PathParam("entity") String entityName) {
		List<JsonObject> instanciesObjects = feature(InstanceService.class).getListOfInstance(entityName);
		return Response.ok().entity(new Gson().toJson(instanciesObjects)).build();
	}

	@PUT
	@Path("/{version}/{entity}/{id}/{action}")
	public Response putActionOverElement(@PathParam("version") String version, @PathParam("entity") String entityName, @PathParam("id") Long id, @PathParam("action") String action) {
		List<JsonObject> instanciesObjects = feature(InstanceService.class).getListOfInstance(entityName);
		return Response.ok().entity(new Gson().toJson(instanciesObjects)).build();
	}
}
