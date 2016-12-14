package br.com.cyop.api;

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

@Path("/")
public class RestMethodsRS extends RestFeature {

	@POST
	@Path("/{version}/{api}")
	public Response create(	@PathParam("version") String version, @PathParam("api") String entityName,
							@FormParam("instanceJson") String instanceJson) {
		JsonObject instance = feature(RestMethodsService.class).createInstance(version, entityName, instanceJson);
		return Response.ok().entity(new Gson().toJson(instance)).build();
	}

	@GET
	@Path("/{version}/{api}")
	public Response list(@PathParam("version") String version, @PathParam("api") String entityName) {
		List<JsonObject> instanciesObjects = feature(RestMethodsService.class).getListOfInstance(version, entityName);
		return Response.ok().entity(new Gson().toJson(instanciesObjects)).build();
	}

	@GET
	@Path("/{version}/{api}/{id}")
	public Response fetch(@PathParam("version") String version, @PathParam("api") String entity, @PathParam("id") Long id) {
		Instance instance = feature(RestMethodsService.class).getInstanceById(version, entity, id);
		return Response.ok().entity(new Gson().toJson(instance.object)).build();
	}

	@PUT
	@Path("/{version}/{api}/{id}")
	public Response update(	@PathParam("version") String version, @PathParam("api") String entity, @PathParam("id") Long id,
							@FormParam("api") String instanceJson) {
		JsonObject object = feature(RestMethodsService.class).updateInstance(version, entity, id, instanceJson);
		return Response.ok().entity(new Gson().toJson(object)).build();
	}

	@DELETE
	@Path("/{version}/{api}/{id}")
	public Response delete(@PathParam("version") String version, @PathParam("api") String entityName, @PathParam("id") Long id) {
		JsonObject instance = feature(RestMethodsService.class).deleteEntity(version, entityName, id);
		return Response.ok().entity(new Gson().toJson(instance)).build();
	}
}
