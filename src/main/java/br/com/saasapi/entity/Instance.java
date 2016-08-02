package br.com.saasapi.entity;

import org.apache.commons.lang3.StringUtils;

import br.com.saasapi.endpoint.Endpoint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.yawp.repository.IdRef;
import io.yawp.repository.annotations.Id;
import io.yawp.repository.annotations.Index;
import io.yawp.repository.annotations.Json;

@io.yawp.repository.annotations.Endpoint(path = "/instances")
public class Instance {

	@Id
	IdRef<Instance> yawpId;

	@Index
	IdRef<Endpoint> entityId;

	@Json
	JsonObject object;

	@Index
	String id;

	public static Instance create(Endpoint endpoint, String instanceJson) {
		Instance instance = new Instance();
		instance.id = endpoint.getMaxId().toString();
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
}
