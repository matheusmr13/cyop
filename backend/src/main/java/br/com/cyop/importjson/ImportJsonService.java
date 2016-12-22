package br.com.cyop.importjson;

import br.com.cyop.endpoint.Endpoint;
import br.com.cyop.endpoint.Property;
import br.com.cyop.endpoint.PropertyType;
import br.com.cyop.enumerator.Enumerator;
import br.com.cyop.version.Version;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.yawp.repository.Feature;

import java.util.*;

public class ImportJsonService extends Feature {

	public void importJson(JsonObject jsonToImport) {
		ImportJson importJson = new ImportJson();
		importJson.importDate = new Date();
		importJson.json = jsonToImport;
		yawp.save(importJson);

		this.createEstructure(importJson);
	}

	private void createEstructure(ImportJson importJson) {
		JsonObject json = importJson.json;

		Version version = setupSettings(json);
		Map<String, Endpoint> endpointMap = setupEndpoints(json, version);
		Map<String, Enumerator> enumeratorMap = setupEnumerators(json, version);

		setupProperties(json, endpointMap, enumeratorMap);
	}

	private Map<String, Endpoint> setupEndpoints(JsonObject json, Version version) {
		Map<String, Endpoint> endpointMap = new HashMap<>();
		JsonArray endpoints = json.get("endpoints").getAsJsonArray();
		for (JsonElement endpointElement : endpoints) {
			JsonObject endpointObject = endpointElement.getAsJsonObject();

			String url = endpointObject.get("url").getAsString();
			Endpoint endpoint = yawp.save(Endpoint.create(url, version));

			if (endpointMap.get(url) != null) {
				throw new RuntimeException();
			}
			endpointMap.put(url, endpoint);
		}
		return endpointMap;
	}

	private Map<String, Enumerator> setupEnumerators(JsonObject json, Version version) {
		Map<String, Enumerator> enumeratorMap = new HashMap<>();
		JsonArray enumerators = json.get("enumerators").getAsJsonArray();
		for (JsonElement enumeratorElement : enumerators) {
			JsonObject enumeratorObject = enumeratorElement.getAsJsonObject();

			String name = enumeratorObject.get("name").getAsString();
			List<String> values = new ArrayList<>();
			JsonArray valuesArray = enumeratorObject.get("values").getAsJsonArray();
			for (JsonElement value : valuesArray) {
				values.add(value.getAsString());
			}
			Enumerator enumerator = yawp.save(Enumerator.create(name, version, values));

			if (enumeratorMap.get(name) != null) {
				throw new RuntimeException();
			}
			enumeratorMap.put(name, enumerator);
		}
		return enumeratorMap;
	}

	private void setupProperties(JsonObject json, Map<String, Endpoint> endpointMap, Map<String, Enumerator> enumeratorsMap) {
		JsonArray endpoints = json.get("endpoints").getAsJsonArray();
		for (JsonElement endpointElement : endpoints) {
			JsonObject endpointObject = endpointElement.getAsJsonObject();
			JsonArray propertiesArray = endpointObject.get("properties").getAsJsonArray();
			Endpoint actualEndpoint = endpointMap.get(endpointObject.get("url").getAsString());
			List<Property> properties = new ArrayList<>();

			for (JsonElement propertyElement : propertiesArray) {
				JsonObject propertyObject = propertyElement.getAsJsonObject();

				String propertyName = propertyObject.get("name").getAsString();
				PropertyType type = PropertyType.valueOf(propertyObject.get("type").getAsString().toUpperCase());
				Property property;
				switch (type) {
					case INTEGER:
					case TEXT:
					case DECIMAL:
					case BOOLEAN:
						property = Property.create(propertyName, type);
						break;
					case ENDPOINT:
						property = Property.createEndpointType(propertyName, endpointMap.get(propertyObject.get("referer").getAsString()));
						break;
					case ENUMERATOR:
						property = Property.createEnumeratorType(propertyName, enumeratorsMap.get(propertyObject.get("referer").getAsString()));
						break;
					default:
						property = new Property();
				}
				properties.add(property);
			}

			actualEndpoint.setProperties(properties);
			yawp.save(actualEndpoint);
		}
	}

	private Version setupSettings(JsonObject json) {
		JsonObject settings = json.get("api_config").getAsJsonObject();
		Version version = Version.create(settings.get("version_url").getAsString());
		return yawp.save(version);
	}
}
