package br.com.cyop.api;

import com.google.gson.JsonObject;
import io.yawp.repository.Feature;

import java.util.List;

public class ActionService extends Feature {

	public List<JsonObject> getActionOverList(String version, String entityName, String action) {
		return null;
	}

	public List<JsonObject> getActionOverElement(String version, String entityName, Long id, String action) {
		return null;
	}

	public List<JsonObject> putActionOverList(String version, String entityName, String action) {
		return null;
	}

	public List<JsonObject> putActionOverElement(String version, String entityName, Long id, String action) {
		return null;
	}
}
