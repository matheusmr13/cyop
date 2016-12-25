package br.com.cyop.importjson;

import io.yawp.repository.hooks.Hook;

public class ImportJsonHook extends Hook<ImportJson> {

	@Override
	public void beforeSave(ImportJson importJson) {
		feature(ImportJsonService.class).importJson(importJson.json);
	}

}
