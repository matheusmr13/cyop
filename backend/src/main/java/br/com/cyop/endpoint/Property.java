package br.com.cyop.endpoint;

import br.com.cyop.enumerator.Enumerator;
import io.yawp.repository.IdRef;

public class Property {
	PropertyType type;
	PropertyType listType;
	String endpointName;
	IdRef<Endpoint> endpointId;
	IdRef<Enumerator> enumeratorType;
	String name;
	Object defaultValue;

	public PropertyType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public IdRef<Enumerator> getEnumeratorType() {
		return enumeratorType;
	}

	public IdRef<Endpoint> getEndpointId() {
		return endpointId;
	}

	public String getEndpointName() {
		return endpointName;
	}

	public static Property createWithDefault(String name, PropertyType type, Object defaultValue) {
		Property property = create(name, type);
		property.defaultValue = defaultValue;
		return property;
	}

	public static Property create(String name, PropertyType type) {
		Property property = new Property();
		property.name = name;
		property.type = type;
		return property;
	}

	public static Property createEnumeratorType(String name, Enumerator enumeratorType) {
		Property property = create(name, PropertyType.ENUMERATOR);
		property.enumeratorType = enumeratorType.getId();
		return property;
	}

	public static Property createEndpointType(String name, Endpoint endpoint) {
		Property property = create(name, PropertyType.ENDPOINT);
		property.endpointName = endpoint.getUrl();
		property.endpointId = endpoint.getId();
		return property;
	}
}
