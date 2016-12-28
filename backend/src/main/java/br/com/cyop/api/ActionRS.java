package br.com.cyop.api;

import br.com.cyop.service.RestFeature;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
public class ActionRS extends RestFeature {
	@GET
	@Path("/{version}/{api}/{action}")
	public Response getActionOverList(	@PathParam("version") String version, @PathParam("api") String entityName,
										@PathParam("action") String action) {
		List<JsonObject> instanciesObjects = feature(ActionService.class).getActionOverList(version, entityName, action);
		return Response.ok().entity(new Gson().toJson(instanciesObjects)).build();
	}

	@GET
	@Path("/{version}/{api}/{id}/{action}")
	public Response getActionOverElement(	@PathParam("version") String version, @PathParam("api") String entityName,
											@PathParam("id") Long id, @PathParam("action") String action) {
		List<JsonObject> instanciesObjects = feature(ActionService.class).getActionOverElement(version, entityName, id, action);
		return Response.ok().entity(new Gson().toJson(instanciesObjects)).build();
	}

	@PUT
	@Path("/{version}/{api}/{action}")
	public Response putActionOverList(	@PathParam("version") String version, @PathParam("api") String entityName,
										@PathParam("action") String action) {
		List<JsonObject> instanciesObjects = feature(ActionService.class).putActionOverList(version, entityName, action);
		return Response.ok().entity(new Gson().toJson(instanciesObjects)).build();
	}

	@PUT
	@Path("/{version}/{api}/{id}/{action}")
	public Response putActionOverElement(	@PathParam("version") String version, @PathParam("api") String entityName,
											@PathParam("id") Long id, @PathParam("action") String action) {
		List<JsonObject> instanciesObjects = feature(ActionService.class).putActionOverElement(version, entityName, id, action);
		return Response.ok().entity(new Gson().toJson(instanciesObjects)).build();
	}
}
