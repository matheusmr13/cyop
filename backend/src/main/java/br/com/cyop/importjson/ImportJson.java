package br.com.cyop.importjson;

import com.google.gson.JsonObject;
import io.yawp.repository.IdRef;
import io.yawp.repository.annotations.Id;
import io.yawp.repository.annotations.Index;
import io.yawp.repository.annotations.Json;

import java.util.Date;

@io.yawp.repository.annotations.Endpoint(path = "/importjson")
public class ImportJson {

	@Id
	IdRef<ImportJson> id;

	@Json
	JsonObject json;

	@Index
	Date importDate;

	public static ImportJson create(JsonObject file) {
		ImportJson importJson = new ImportJson();
		importJson.importDate = new Date();
		importJson.json = file;
		return importJson;
	}
}
