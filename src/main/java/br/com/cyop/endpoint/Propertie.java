package br.com.cyop.endpoint;

public class Propertie {
	PropertieType type;
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
}
