package br.com.saasapi;

import io.yawp.repository.IdRef;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/api")
public class InstanceRS extends RestFeature {

	@POST
	@Path("/{entity}")
	public Response create(@PathParam("entity") String entityName, @FormParam("instanceJson") String instanceJson) {
		Instance instance = new Instance();
		Entity entity = yawp(Entity.class).where("name", "=", entityName).only();
		if (entity == null) {
			return Response.status(HttpStatus.SC_FAILED_DEPENDENCY).build();
		}

		instance.id = (++entity.maxId).toString();
		instance.entityId = entity.id;

		if (StringUtils.isEmpty(instanceJson)) {
			instance.object = new JsonObject();
		} else {
			instance.object = new JsonParser().parse(instanceJson).getAsJsonObject();
		}
		for (Propertie propertie : entity.properties) {
			JsonElement propertieJson = instance.object.get(propertie.name);
			if (propertieJson == null && propertie.defaultValue != null) {
				instance.object.addProperty(propertie.name, propertie.defaultValue.toString());
			}
		}
		instance.object.addProperty("id", instance.id);
		
		yawp.save(entity);
		return Response.ok().entity(new Gson().toJson(yawp.save(instance).object)).build();
	}

	@GET
	@Path("/{entity}")
	public Response list(@PathParam("entity") String entityName) {
		Entity entity = yawp(Entity.class).where("name", "=", entityName).first();
		if (entity == null) {
			return Response.status(HttpStatus.SC_FAILED_DEPENDENCY).build();
		}

		List<Instance> instancies = yawp(Instance.class).where("entityId", "=", entity.id).list();
		List<JsonObject> instanciesObjects = new ArrayList<JsonObject>(instancies.size());
		for (Instance instance : instancies) {
			instanciesObjects.add(instance.object);
		}
		return Response.ok().entity(new Gson().toJson(instanciesObjects)).build();
	}

	@GET
	@Path("/{entity}/{id}")
	public Response fetch(@PathParam("entity") String entity, @PathParam("id") String id) {
		IdRef<Entity> entityId = yawp(Entity.class).where("name", "=", entity).onlyId();
		if (entityId == null) {
			return Response.status(HttpStatus.SC_FAILED_DEPENDENCY).build();
		}

		Instance instance = yawp(Instance.class).where("entityId", "=", entityId).and("id", "=", id).only();
		return Response.ok().entity(new Gson().toJson(instance.object)).build();
	}

	@PUT
	@Path("/{entity}/{id}")
	public Response update(@PathParam("entity") String entity, @PathParam("id") String id, @FormParam("instanceJson") String instanceJson) {
		IdRef<Entity> entityId = yawp(Entity.class).where("name", "=", entity).onlyId();
		if (entityId == null) {
			return Response.status(HttpStatus.SC_FAILED_DEPENDENCY).build();
		}

		Instance instance = yawp(Instance.class).where("entityId", "=", entityId).and("id", "=", id).only();
		instance.object = new JsonParser().parse(instanceJson).getAsJsonObject();
		return Response.ok().entity(new Gson().toJson(yawp.save(instance))).build();
	}

	@DELETE
	@Path("/{entity}/{id}")
	public Response delete(@PathParam("entity") String entityName, @PathParam("id") String id) {
		Entity entity = yawp(Entity.class).where("name", "=", entityName).first();
		if (entity == null) {
			return Response.status(HttpStatus.SC_FAILED_DEPENDENCY).build();
		}

		Instance instance = yawp(Instance.class).where("entityId", "=", entity.id).and("id", "=", id).only();
		yawp.destroy(instance.yawpId);
		return Response.ok().entity(new Gson().toJson(instance.object)).build();
	}
}
