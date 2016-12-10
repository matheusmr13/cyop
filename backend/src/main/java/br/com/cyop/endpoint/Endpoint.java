package br.com.cyop.endpoint;

import br.com.cyop.version.Version;
import io.yawp.repository.IdRef;
import io.yawp.repository.annotations.Id;
import io.yawp.repository.annotations.Index;
import io.yawp.repository.annotations.Json;

import java.util.List;

@io.yawp.repository.annotations.Endpoint(path = "/entities")
public class Endpoint {

	@Id
	IdRef<Endpoint> id;

	@Index
	String name;

	@Json
	List<Propertie> properties;

	@Index
	IdRef<Version> version;

	Long maxId;

	public IdRef<Endpoint> getId() {
		return id;
	}

	public List<Propertie> getProperties() {
		return properties;
	}

	public Long getMaxId() {
		return maxId;
	}

	public static Endpoint create(String name, List<Propertie> properties) {
		Endpoint endpoint = new Endpoint();
		endpoint.name = name;
		endpoint.properties = properties;
		endpoint.maxId = 0l;
		return endpoint;
	}
}
