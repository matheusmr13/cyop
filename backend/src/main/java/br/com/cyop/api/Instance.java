package br.com.cyop.api;

import io.yawp.repository.IdRef;
import io.yawp.repository.annotations.Id;
import io.yawp.repository.annotations.Index;
import io.yawp.repository.annotations.Json;

import org.apache.commons.lang3.StringUtils;

import br.com.cyop.endpoint.Endpoint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@io.yawp.repository.annotations.Endpoint(path = "/instances")
public class Instance {

	@Id
	IdRef<Instance> yawpId;

	@Index
	IdRef<Endpoint> entityId;

	@Json
	JsonObject object;

	@Index
	Long id;

	public static Instance create(Endpoint endpoint) {
		Instance instance = new Instance();
		instance.id = endpoint.getMaxId();
		instance.entityId = endpoint.getId();
		instance.object = new JsonObject();
		instance.object.addProperty("id", instance.id);
		return instance;
	}

	public IdRef<Instance> getYawpId() {
		return yawpId;
	}

	public IdRef<Endpoint> getEntityId() {
		return entityId;
	}

	public JsonObject getObject() {
		return object;
	}

	public Long getId() {
		return id;
	}
}
