package br.com.cyop.endpoint;

import br.com.cyop.version.Version;
import io.yawp.repository.Feature;

public class EndpointService extends Feature {

	public Endpoint updateId(Endpoint endpoint) {
		endpoint.maxId++;
		return yawp.save(endpoint);
	}

	public Endpoint getEndpointByNameAndVersion(Version version, String entityName) {
		return yawp(Endpoint.class).where("version", "=", version.getId()).and("url", "=", entityName).first();
	}
}
