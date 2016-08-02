package br.com.saasapi.endpoint;

public class Propertie {
	private PropertieType type;
	private String name;
	private Object defaultValue;

	public PropertieType getType() {
		return type;
	}

	public void setType(PropertieType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
}
