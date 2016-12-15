package br.com.cyop.enumerator;

import br.com.cyop.endpoint.Propertie;
import br.com.cyop.version.Version;
import io.yawp.repository.IdRef;
import io.yawp.repository.annotations.Id;
import io.yawp.repository.annotations.Index;
import io.yawp.repository.annotations.Json;

import java.util.List;

@io.yawp.repository.annotations.Endpoint(path = "/enumerators")
public class Enumerator {

	@Id
	IdRef<Enumerator> id;

	@Index
	String url;

	@Json
	List<String> values;

	@Index
	IdRef<Version> version;

	public IdRef<Enumerator> getId() {
		return id;
	}

	public static Enumerator create(String name, Version version, List<String> values) {
		Enumerator enumerator = new Enumerator();
		enumerator.url = name;
		enumerator.version = version.getId();
		enumerator.values = values;
		return enumerator;
	}

	public List<String> getValues() {
		return values;
	}
}
