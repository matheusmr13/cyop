package br.com.cyop.api;

import br.com.cyop.api.Instance;
import br.com.cyop.endpoint.Endpoint;
import br.com.cyop.endpoint.EndpointService;
import br.com.cyop.endpoint.Propertie;
import br.com.cyop.service.NotFoundException;
import br.com.cyop.version.Version;
import br.com.cyop.version.VersionService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.yawp.repository.Feature;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
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
