package br.com.cyop.endpoint;

import br.com.cyop.enumerator.Enumerator;
import io.yawp.repository.IdRef;

public class Propertie {
	PropertieType type;
	PropertieType listType;
	String endpointType;
	IdRef<Enumerator> enumeratorType;
	String name;
	Object defaultValue;

	public PropertieType getType() {
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

	public static Propertie create(String name, PropertieType type) {
		Propertie propertie = new Propertie();
		propertie.name = name;
		propertie.type = type;
		return propertie;
	}

	public static Propertie createListType(String name, PropertieType listType) {
		Propertie propertie = create(name, PropertieType.LIST);
		propertie.listType = listType;
		return propertie;
	}

	public static Propertie createEnumeratorType(String name, Enumerator enumeratorType) {
		Propertie propertie = create(name, PropertieType.ENUMERATOR);
		propertie.enumeratorType = enumeratorType.getId();
		return propertie;
	}

	public static Propertie createEndpointType(String name, String endpointType) {
		Propertie propertie = create(name, PropertieType.ENDPOINT);
		propertie.endpointType = endpointType;
		return propertie;
	}

	public static Propertie createEndpointListType(String name, String endpointType) {
		Propertie propertie = createListType(name, PropertieType.ENDPOINT);
		propertie.endpointType = endpointType;
		return propertie;
	}
}
