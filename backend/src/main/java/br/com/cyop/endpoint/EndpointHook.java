package br.com.cyop.endpoint;

import br.com.cyop.exception.NotFoundException;
import br.com.cyop.version.Version;
import br.com.cyop.version.VersionService;
import io.yawp.repository.hooks.Hook;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class EndpointHook extends Hook<Endpoint> {

	@Override
	public void beforeSave(Endpoint endpoint) {
		if (!StringUtils.isEmpty(endpoint.url)) {
			endpoint.url = endpoint.url.replaceAll("[/@]","");
		}
		if (endpoint.getId() == null) {
			Property idProperty = new Property();
			idProperty.name = "id";
			idProperty.type = PropertyType.INTEGER;
			if (endpoint.properties == null) {
				endpoint.properties = new ArrayList<Property>();
			}
			endpoint.properties.add(idProperty);

			Version version;
			try {
				version = feature(VersionService.class).getVersionByUrl("v1");
			} catch(NotFoundException e) {
				version = Version.create("v1");
				yawp.save(version);
			}

			endpoint.version = version.getId();
		}
	}

}
