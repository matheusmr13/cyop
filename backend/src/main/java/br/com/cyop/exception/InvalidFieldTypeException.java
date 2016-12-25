package br.com.cyop.exception;

import br.com.cyop.endpoint.PropertyType;
import com.google.gson.JsonObject;

public class InvalidFieldTypeException extends RuntimeException {
	public InvalidFieldTypeException(String valueAsString, String propertieName, PropertyType type) {
		super("{\"value\":\"" + valueAsString + "\",\"column\":\"" + propertieName + "\",\"type\":\"" + type.name() + "\"}");
	}
}
