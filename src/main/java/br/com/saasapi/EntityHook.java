package br.com.saasapi;

import java.util.ArrayList;

import io.yawp.repository.hooks.Hook;

public class EntityHook extends Hook<Entity> {

	@Override
	public void beforeSave(Entity entity) {
		if (entity.id == null) {
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
