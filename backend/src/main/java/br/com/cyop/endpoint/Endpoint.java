package br.com.cyop.endpoint;

import br.com.cyop.version.Version;
import io.yawp.repository.IdRef;
import io.yawp.repository.annotations.Id;
import io.yawp.repository.annotations.Index;
import io.yawp.repository.annotations.Json;

import java.util.List;

@io.yawp.repository.annotations.Endpoint(path = "/endpoints")
public class Endpoint {

	@Id
	IdRef<Endpoint> id;

	@Index
	String url;

	@Json
	List<Propertie> properties;

	@Index
	IdRef<Version> version;

	Long maxId = 0l;

	public IdRef<Endpoint> getId() {
		return id;
	}

	public List<Propertie> getProperties() {
		return properties;
	}

	public Long getMaxId() {
		return maxId;
	}

	public String getUrl() {
		return url;
	}

	public static Endpoint create(String name, Version version, List<Propertie> properties) {
		Endpoint endpoint = new Endpoint();
		endpoint.url = name;
		endpoint.properties = properties;
		endpoint.maxId = 0l;
		endpoint.version = version.getId();
		return endpoint;
	}
}
