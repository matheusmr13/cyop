package br.com.cyop.endpoint;

import io.yawp.repository.hooks.Hook;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class EndpointHook extends Hook<Endpoint> {

	@Override
	public void beforeSave(Endpoint entity) {
		if (!StringUtils.isEmpty(entity.url)) {
			entity.url = entity.url.replaceAll("[/@]","");
		}
		if (entity.getId() == null) {
			Property idProperty = new Property();
			idProperty.name = "id";
			idProperty.type = PropertyType.INTEGER;
			if (entity.properties == null) {
				entity.properties = new ArrayList<Property>();
			}
			entity.properties.add(idProperty);
		}
	}

}
