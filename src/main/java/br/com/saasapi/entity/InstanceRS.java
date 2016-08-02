package br.com.saasapi.entity;

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

import br.com.saasapi.endpoint.Endpoint;
import br.com.saasapi.endpoint.Propertie;
import br.com.saasapi.service.RestFeature;

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
		Endpoint entity = yawp(Endpoint.class).where("name", "=", entityName).only();
		if (entity == null) {
			return Response.status(HttpStatus.SC_NOT_FOUND).build();
		}

		entity.setMaxId(entity.getMaxId() + 1);
		instance.id = entity.getMaxId().toString();
		instance.entityId = entity.getId();

		if (StringUtils.isEmpty(instanceJson)) {
			instance.object = new JsonObject();
		} else {
			instance.object = new JsonParser().parse(instanceJson).getAsJsonObject();
		}
		for (Propertie propertie : entity.getProperties()) {
			JsonElement propertieJson = instance.object.get(propertie.getName());
			if (propertieJson == null && propertie.getDefaultValue() != null) {
				instance.object.addProperty(propertie.getName(), propertie.getDefaultValue().toString());
			}
		}
		instance.object.addProperty("id", instance.id);

		yawp.save(entity);
		return Response.ok().entity(new Gson().toJson(yawp.save(instance).object)).build();
	}

	@GET
	@Path("/{entity}")
	public Response list(@PathParam("entity") String entityName) {
		Endpoint entity = yawp(Endpoint.class).where("name", "=", entityName).first();
		if (entity == null) {
			return Response.status(HttpStatus.SC_NOT_FOUND).build();
		}

		List<Instance> instancies = yawp(Instance.class).where("entityId", "=", entity.getId()).list();
		List<JsonObject> instanciesObjects = new ArrayList<JsonObject>(instancies.size());
		for (Instance instance : instancies) {
			instanciesObjects.add(instance.object);
		}
		return Response.ok().entity(new Gson().toJson(instanciesObjects)).build();
	}

	@GET
	@Path("/{entity}/{id}")
	public Response fetch(@PathParam("entity") String entity, @PathParam("id") String id) {
		IdRef<Endpoint> entityId = yawp(Endpoint.class).where("name", "=", entity).onlyId();
		if (entityId == null) {
			return Response.status(HttpStatus.SC_NOT_FOUND).build();
		}

		Instance instance = yawp(Instance.class).where("entityId", "=", entityId).and("id", "=", id).only();
		return Response.ok().entity(new Gson().toJson(instance.object)).build();
	}

	@PUT
	@Path("/{entity}/{id}")
	public Response update(@PathParam("entity") String entity, @PathParam("id") String id, @FormParam("instance") String instanceJson) {
		IdRef<Endpoint> entityId = yawp(Endpoint.class).where("name", "=", entity).onlyId();
		if (entityId == null) {
			return Response.status(HttpStatus.SC_NOT_FOUND).build();
		}

		if (StringUtils.isEmpty(instanceJson)) {
			return Response.status(HttpStatus.SC_NOT_FOUND).build();
		}

		Instance instance = yawp(Instance.class).where("entityId", "=", entityId).and("id", "=", id).only();
		instance.object = new JsonParser().parse(instanceJson).getAsJsonObject();
		return Response.ok().entity(new Gson().toJson(yawp.save(instance).object)).build();
	}

	@DELETE
	@Path("/{entity}/{id}")
	public Response delete(@PathParam("entity") String entityName, @PathParam("id") String id) {
		Endpoint entity = yawp(Endpoint.class).where("name", "=", entityName).first();
		if (entity == null) {
			return Response.status(HttpStatus.SC_NOT_FOUND).build();
		}

		Instance instance = yawp(Instance.class).where("entityId", "=", entity.getId()).and("id", "=", id).only();
		yawp.destroy(instance.yawpId);
		return Response.ok().entity(new Gson().toJson(instance.object)).build();
	}
}
