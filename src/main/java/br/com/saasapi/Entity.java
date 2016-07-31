package br.com.saasapi;

import io.yawp.repository.IdRef;
import io.yawp.repository.annotations.Endpoint;
import io.yawp.repository.annotations.Id;
import io.yawp.repository.annotations.Index;
import io.yawp.repository.annotations.Json;

import java.util.List;

@Endpoint(path = "/entities")
public class Entity {

	@Id
	IdRef<Entity> id;

	@Index
	String name;

	@Json
	List<Propertie> properties;

	Integer maxId = 0;

}
