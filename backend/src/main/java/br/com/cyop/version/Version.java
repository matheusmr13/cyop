package br.com.cyop.version;

import io.yawp.repository.IdRef;
import io.yawp.repository.annotations.Id;
import io.yawp.repository.annotations.Index;

import java.util.Date;

@io.yawp.repository.annotations.Endpoint(path = "/version")
public class Version {

	@Id
	IdRef<Version> id;

	@Index
	String url;
	Date creationDate;
	@Index
	Integer number;
}
