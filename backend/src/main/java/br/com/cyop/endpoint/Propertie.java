package br.com.cyop.endpoint;

public class Propertie {
	PropertieType type;
	PropertieType listType;
	String endpointType;
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

	public static Propertie create(String name, PropertieType type) {
		Propertie propertie = new Propertie();
		propertie.name = name;
		propertie.type = type;
		return propertie;
	}

	public static Propertie createListType(String name, PropertieType listType) {
		Propertie propertie = new Propertie();
		propertie.name = name;
		propertie.type = PropertieType.LIST;
		propertie.listType = listType;
		return propertie;
	}

	public static Propertie createEndpointType(String name, String endpointType) {
		Propertie propertie = new Propertie();
		propertie.name = name;
		propertie.type = PropertieType.ENDPOINT;
		propertie.endpointType = endpointType;
		return propertie;
	}

	public static Propertie createEndpointListType(String name, String endpointType) {
		Propertie propertie = Propertie.createListType(name, PropertieType.ENDPOINT);
		propertie.endpointType = endpointType;
		return propertie;
	}
}
