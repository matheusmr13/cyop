package br.com.cyop.endpoint;

import io.yawp.repository.Feature;

public class EndpointService extends Feature {

	public Endpoint updateId(Endpoint endpoint) {
		endpoint.maxId++;
		return yawp.save(endpoint);
	}

	public Endpoint getEndpointByName(String entityName) {
		return yawp(Endpoint.class).where("name", "=", entityName).first();
	}
}
