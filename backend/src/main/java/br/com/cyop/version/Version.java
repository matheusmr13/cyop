package br.com.cyop.version;

import io.yawp.repository.IdRef;
import io.yawp.repository.annotations.Id;
import io.yawp.repository.annotations.Index;

import java.util.Date;

@io.yawp.repository.annotations.Endpoint(path = "/versions")
public class Version {

	@Id
	IdRef<Version> id;

	@Index
	String url;
	Date creationDate;
	@Index
	Integer number;
	@Index
	boolean active = true;

	public static Version create(String url) {
		Version version = new Version();
		version.url = url;
		return version;
	}
	public IdRef<Version> getId() {
		return id;
	}
}
