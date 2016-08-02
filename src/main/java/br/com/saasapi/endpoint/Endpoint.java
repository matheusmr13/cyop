package br.com.saasapi.endpoint;

import io.yawp.repository.IdRef;
import io.yawp.repository.annotations.Id;
import io.yawp.repository.annotations.Index;
import io.yawp.repository.annotations.Json;

import java.util.List;

@io.yawp.repository.annotations.Endpoint(path = "/entities")
public class Endpoint {

	@Id
	private IdRef<Endpoint> id;

	@Index
	private String name;

	@Json
	private List<Propertie> properties;

	private Integer maxId = 0;

	public IdRef<Endpoint> getId() {
		return id;
	}

	public void setId(IdRef<Endpoint> id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Propertie> getProperties() {
		return properties;
	}

	public void setProperties(List<Propertie> properties) {
		this.properties = properties;
	}

	public Integer getMaxId() {
		return maxId;
	}

	public void setMaxId(Integer maxId) {
		this.maxId = maxId;
	}

}
