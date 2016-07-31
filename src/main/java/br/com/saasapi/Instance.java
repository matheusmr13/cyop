package br.com.saasapi;

import com.google.gson.JsonObject;

import io.yawp.repository.IdRef;
import io.yawp.repository.annotations.Endpoint;
import io.yawp.repository.annotations.Id;
import io.yawp.repository.annotations.Index;
import io.yawp.repository.annotations.Json;

@Endpoint(path = "/instances")
public class Instance {

	@Id
	IdRef<Instance> yawpId;

	@Index
	IdRef<Entity> entityId;

	@Json
	JsonObject object;

	@Index
	String id;
}
