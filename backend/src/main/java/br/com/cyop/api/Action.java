package br.com.cyop.api;

import br.com.cyop.endpoint.Endpoint;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.yawp.repository.IdRef;
import io.yawp.repository.annotations.Id;
import io.yawp.repository.annotations.Index;
import io.yawp.repository.annotations.Json;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

@io.yawp.repository.annotations.Endpoint(path = "/actions")
public class Action {

	@Id
	IdRef<Action> id;

	@Index
	String url;

	@Index
	MethodType method;

	@Json
	JsonObject parameters;
}
