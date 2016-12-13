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

	public static Instance create(Endpoint endpoint, String instanceJson) {
		Instance instance = new Instance();
		instance.id = endpoint.getMaxId();
		instance.entityId = endpoint.getId();
		instance.updateJson(instanceJson);
		return instance;
	}

	public void updateJson(String instanceJson) {
		if (StringUtils.isEmpty(instanceJson)) {
			this.object = new JsonObject();
		} else {
			this.object = new JsonParser().parse(instanceJson).getAsJsonObject();
		}
		this.object.addProperty("id", this.id);
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
