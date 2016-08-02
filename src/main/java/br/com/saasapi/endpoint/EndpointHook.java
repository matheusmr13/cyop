package br.com.saasapi.endpoint;

import java.util.ArrayList;

import io.yawp.repository.hooks.Hook;

public class EndpointHook extends Hook<Endpoint> {

	@Override
	public void beforeSave(Endpoint entity) {
		if (entity.getId() == null) {
			Propertie idPropertie = new Propertie();
			idPropertie.name = "id";
			idPropertie.type = PropertieType.INTEGER;
			if (entity.properties == null) {
				entity.properties = new ArrayList<Propertie>();
			}
			entity.properties.add(idPropertie);
		}
	}

}
