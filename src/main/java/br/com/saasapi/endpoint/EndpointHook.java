package br.com.saasapi.endpoint;

import java.util.ArrayList;

import io.yawp.repository.hooks.Hook;

public class EndpointHook extends Hook<Endpoint> {

	@Override
	public void beforeSave(Endpoint entity) {
		if (entity.getId() == null) {
			Propertie idPropertie = new Propertie();
			idPropertie.setName("id");
			idPropertie.setType(PropertieType.INTEGER);
			if (entity.getProperties() == null) {
				entity.setProperties(new ArrayList<Propertie>());
			}
			entity.getProperties().add(idPropertie);
		}
	}

}
