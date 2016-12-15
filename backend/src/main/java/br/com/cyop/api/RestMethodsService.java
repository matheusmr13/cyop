package br.com.cyop.api;

import br.com.cyop.exception.InvalidFieldTypeException;
import br.com.cyop.version.Version;
import br.com.cyop.version.VersionService;
import com.google.gson.JsonParser;
import io.yawp.repository.Feature;

import java.util.ArrayList;
import java.util.List;

import io.yawp.repository.IdRef;
import org.apache.commons.lang3.StringUtils;

import br.com.cyop.endpoint.Endpoint;
import br.com.cyop.endpoint.EndpointService;
import br.com.cyop.endpoint.Property;
import br.com.cyop.exception.NotFoundException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class RestMethodsService extends Feature {

	public JsonObject createInstance(String version, String entityName, String instanceJson) {
		this.validateField(entityName);
		Endpoint endpoint = this.getEndpoint(version, entityName);
		feature(EndpointService.class).updateId(endpoint);

		Instance instance = Instance.create(endpoint);
		this.setAndValidateProperties(instance, instanceJson, endpoint);
		this.setInstanceDefaults(endpoint, instance);
		return yawp.save(instance).object;
	}

	private void setInstanceDefaults(Endpoint endpoint, Instance instance) {
		for (Property property : endpoint.getProperties()) {
			JsonElement propertieJson = instance.object.get(property.getName());
			if (propertieJson == null && property.getDefaultValue() != null) {
				instance.object.addProperty(property.getName(), property.getDefaultValue().toString());
			}
		}
	}

	private Instance setAndValidateProperties(Instance instance, String toMergeInstanceStringJson, Endpoint endpoint) {
		if (StringUtils.isEmpty(toMergeInstanceStringJson)) {
			return instance;
		}
		JsonObject instanceJson = instance.getObject();
		JsonObject toMergeInstanceJson = new JsonParser().parse(toMergeInstanceStringJson).getAsJsonObject();
		toMergeInstanceJson.remove("id");
		for (Property property : endpoint.getProperties()) {
			String propertieName = property.getName();
			if (toMergeInstanceJson.has(propertieName)) {
				JsonElement jsonElement = toMergeInstanceJson.get(propertieName);
				if (jsonElement.isJsonNull()) {
					instanceJson.remove(propertieName);
				} else {
					processPropertie(instanceJson, jsonElement, property);
				}
			}
		}
		return instance;
	}

	private void processPropertie(JsonObject instanceJson, JsonElement jsonElement, Property property) {
		String propertieName = property.getName();
		String valueAsString = jsonElement.getAsString();
		switch (property.getType()) {
			case TEXT:
				instanceJson.addProperty(propertieName, valueAsString);
				break;
			case INTEGER:
				try {
					instanceJson.addProperty(propertieName, jsonElement.getAsInt());
				} catch(NumberFormatException e) {
					throw new InvalidFieldTypeException();
				}
				break;
			case BOOLEAN:
				if ("1". equals(valueAsString) || "true".equals(valueAsString)) {
					instanceJson.addProperty(propertieName, true);
				} else if ("0". equals(valueAsString) || "false".equals(valueAsString)) {
					instanceJson.addProperty(propertieName, false);
				} else {
					throw new InvalidFieldTypeException();
				}
				break;
			case DECIMAL:
				try {
					instanceJson.addProperty(propertieName, jsonElement.getAsBigDecimal());
				} catch(NumberFormatException e) {
					throw new InvalidFieldTypeException();
				}
				break;
			case ENUMERATOR:
				if (property.getEnumeratorType().fetch().getValues().contains(valueAsString)) {
					instanceJson.addProperty(propertieName, valueAsString);
				} else {
					throw new InvalidFieldTypeException();
				}
				break;
			case ENDPOINT:
				try {
					Instance instance = findInstanceById(property.getEndpointId(), jsonElement.getAsLong());
					instanceJson.addProperty(propertieName, valueAsString);
				} catch(NumberFormatException e) {
					throw new InvalidFieldTypeException();
				}
				break;
		}
	}

	public List<JsonObject> getListOfInstance(String version, String entityName) {
		this.validateField(entityName);
		Endpoint entity = this.getEndpoint(version, entityName);

		List<Instance> instancies = this.listByEndpoint(entity);
		List<JsonObject> instanciesObjects = new ArrayList<JsonObject>(instancies.size());
		for (Instance instance : instancies) {
			instanciesObjects.add(instance.object);
		}
		return instanciesObjects;
	}

	private List<Instance> listByEndpoint(Endpoint entity) {
		return yawp(Instance.class).where("entityId", "=", entity.getId()).list();
	}

	public Instance getInstanceById(String version, String entityName, Long id) {
		this.validateField(entityName);
		Instance instance = this.findInstanceById(this.getEndpoint(version, entityName).getId(), id);
		return instance;
	}

	public JsonObject updateInstance(String version, String entityName, Long id, String instanceJson) {
		this.validateField(entityName);
		this.validateField(id);
		Endpoint endpoint = this.getEndpoint(version, entityName);

		Instance instance = this.findInstanceById(endpoint.getId(), id);
		this.setAndValidateProperties(instance, instanceJson, endpoint);
		return yawp.save(instance).object;
	}

	public JsonObject deleteEntity(String version, String entityName, Long id) {
		validateField(entityName);
		validateField(id);
		Endpoint endpoint = getEndpoint(version, entityName);

		Instance instance = findInstanceById(endpoint.getId(), id);
		yawp.destroy(instance.yawpId);
		return instance.object;
	}

	private Instance findInstanceById(IdRef<Endpoint> endpointId, Long id) {
		Instance first = yawp(Instance.class).where("entityId", "=", endpointId).and("id", "=", id).first();
		if (first == null) {
			throw new NotFoundException();
		}
		return first;
	}

	private Endpoint getEndpoint(String versionUrl, String entityName) {
		Version version = feature(VersionService.class).getVersionByUrl(versionUrl);
		Endpoint entity = feature(EndpointService.class).getEndpointByNameAndVersion(version, entityName);
		if (entity == null) {
			throw new NotFoundException();
		}
		return entity;
	}

	private void validateField(String field) {
		if (StringUtils.isEmpty(field)) {
			throw new NotFoundException();
		}
	}

	private void validateField(Object field) {
		if (field == null) {
			throw new NotFoundException();
		}
	}
}
