package br.com.cyop.entity;

import br.com.cyop.version.Version;
import br.com.cyop.version.VersionService;
import io.yawp.repository.Feature;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import br.com.cyop.endpoint.Endpoint;
import br.com.cyop.endpoint.EndpointService;
import br.com.cyop.endpoint.Propertie;
import br.com.cyop.service.NotFoundException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class InstanceService extends Feature {

	public JsonObject createInstance(String version, String entityName, String instanceJson) {
		validateField(entityName);
		Endpoint endpoint = getEndpoint(version, entityName);
		feature(EndpointService.class).updateId(endpoint);

		Instance instance = Instance.create(endpoint, instanceJson);
		setInstanceDefaults(endpoint, instance);
		return yawp.save(instance).object;
	}

	private void setInstanceDefaults(Endpoint endpoint, Instance instance) {
		for (Propertie propertie : endpoint.getProperties()) {
			JsonElement propertieJson = instance.object.get(propertie.getName());
			if (propertieJson == null && propertie.getDefaultValue() != null) {
				instance.object.addProperty(propertie.getName(), propertie.getDefaultValue().toString());
			}
		}
	}

	public List<JsonObject> getListOfInstance(String version, String entityName) {
		validateField(entityName);
		Endpoint entity = getEndpoint(version, entityName);

		List<Instance> instancies = listByEndpoint(entity);
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
		validateField(entityName);
		Instance instance = findInstanceById(getEndpoint(version, entityName), id);
		return instance;
	}

	public JsonObject updateInstance(String version, String entityName, Long id, String instanceJson) {
		validateField(entityName);
		validateField(id);
		Endpoint entity = getEndpoint(version, entityName);

		Instance instance = findInstanceById(entity, id);
		instance.updateJson(instanceJson);
		return yawp.save(instance).object;
	}

	public JsonObject deleteEntity(String version, String entityName, Long id) {
		validateField(entityName);
		validateField(id);
		Endpoint entity = getEndpoint(version, entityName);

		Instance instance = findInstanceById(entity, id);
		yawp.destroy(instance.yawpId);
		return instance.object;
	}

	private Instance findInstanceById(Endpoint entity, Long id) {
		Instance first = yawp(Instance.class).where("entityId", "=", entity.getId()).and("id", "=", id).first();
		if (first == null) {
			throw new NotFoundException();
		}
		return first;
	}

	private Endpoint getEndpoint(String versionUrl, String entityName) {
		Version version = new VersionService().gerVersionByUrl(versionUrl);
		Endpoint entity = new EndpointService().getEndpointByName(entityName);
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
