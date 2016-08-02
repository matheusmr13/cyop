package br.com.saasapi.entity;

import io.yawp.repository.Feature;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import br.com.saasapi.endpoint.Endpoint;
import br.com.saasapi.endpoint.EndpointService;
import br.com.saasapi.endpoint.Propertie;
import br.com.saasapi.service.NotFoundException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class InstanceService extends Feature {

	public JsonObject createInstance(String entityName, String instanceJson) {
		validateField(entityName);
		Endpoint endpoint = getEndpoint(entityName);
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

	public List<JsonObject> getListOfInstance(String entityName) {
		validateField(entityName);
		Endpoint entity = getEndpoint(entityName);

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

	public Instance getInstanceById(String entityName, String id) {
		validateField(entityName);
		Instance instance = findInstanceById(getEndpoint(entityName), id);
		if (instance == null) {
			throw new NotFoundException();
		}
		return instance;
	}

	public JsonObject updateInstance(String entityName, String id, String instanceJson) {
		validateField(entityName);
		validateField(id);
		Endpoint entity = getEndpoint(entityName);

		Instance instance = findInstanceById(entity, id);
		instance.updateJson(instanceJson);
		return yawp.save(instance).object;
	}

	public JsonObject deleteEntity(String entityName, String id) {
		validateField(entityName);
		validateField(id);
		Endpoint entity = getEndpoint(entityName);

		Instance instance = findInstanceById(entity, id);
		yawp.destroy(instance.yawpId);
		return instance.object;
	}

	private Instance findInstanceById(Endpoint entity, String id) {
		return yawp(Instance.class).where("entityId", "=", entity.getId()).and("id", "=", id).first();
	}

	private Endpoint getEndpoint(String entityName) {
		Endpoint entity = yawp(Endpoint.class).where("name", "=", entityName).first();
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

}
