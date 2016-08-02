package br.com.saasapi.entity;

import br.com.saasapi.endpoint.Endpoint;

import com.google.gson.JsonObject;

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
}
