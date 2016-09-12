package br.com.cyop.endpoint;

import io.yawp.repository.hooks.Hook;

import java.util.ArrayList;

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
