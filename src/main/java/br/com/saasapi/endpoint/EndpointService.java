package br.com.saasapi.endpoint;

import io.yawp.repository.Feature;

public class EndpointService extends Feature {

	public Endpoint updateId(Endpoint endpoint) {
		endpoint.maxId++;
		return yawp.save(endpoint);
	}
}
