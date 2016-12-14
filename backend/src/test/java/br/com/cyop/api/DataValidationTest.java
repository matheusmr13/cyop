package br.com.cyop.api;

import br.com.cyop.endpoint.Endpoint;
import br.com.cyop.endpoint.Propertie;
import br.com.cyop.endpoint.PropertieType;
import br.com.cyop.version.Version;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.yawp.testing.EndpointTestCaseBase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DataValidationTest extends EndpointTestCaseBase {

	@Before
	public void before() {
		List<Propertie> properties = new ArrayList<>();
		properties.add(Propertie.create("name", PropertieType.TEXT));
		properties.add(Propertie.create("age", PropertieType.INTEGER));
		properties.add(Propertie.create("active", PropertieType.BOOLEAN));
		properties.add(Propertie.create("height", PropertieType.DECIMAL));
		properties.add(Propertie.createEndpointType("father", "person"));
		properties.add(Propertie.create("job", PropertieType.ENUMETARED));
		properties.add(Propertie.createListType("children", PropertieType.ENDPOINT));

		Version v1 = Version.create("v1");
		yawp.save(v1);

		yawp.saveWithHooks(Endpoint.create("person", v1, properties));
	}

	@Test
	public void textPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "name";
		assertEquals("Matheus", createInstance(feature, newJsonObjectString(testingProperty, "Matheus")).get(testingProperty).getAsString());
		assertEquals("1000", createInstance(feature, newJsonObjectString(testingProperty, "1000")).get(testingProperty).getAsString());
		assertEquals("true", createInstance(feature, newJsonObjectString(testingProperty, "true")).get(testingProperty).getAsString());
		assertEquals("null", createInstance(feature, newJsonObjectString(testingProperty, "null")).get(testingProperty).getAsString());
		assertFalse(createInstance(feature, newJsonObjectString(testingProperty, null)).has(testingProperty));
	}

	@Test
	public void integerPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "age";
		assertEquals(1000, createInstance(feature, newJsonObjectNumber(testingProperty, 1000)).get(testingProperty).getAsInt());
		assertEquals(1000, createInstance(feature, newJsonObjectString(testingProperty, "1000")).get(testingProperty).getAsInt());
		assertFalse(createInstance(feature, newJsonObjectNumber(testingProperty, null)).has(testingProperty));
	}

	private JsonObject createInstance(RestMethodsService feature, String jsonString) {
		return feature.createInstance("v1", "person", jsonString);
	}

	private String newJsonObjectString(String key, String value) {
		JsonObject json = new JsonObject();
		json.addProperty(key, value);
		return json.toString();
	}

	private String newJsonObjectNumber(String key, Number value) {
		JsonObject json = new JsonObject();
		json.addProperty(key, value);
		return json.toString();
	}

	private String newJsonObjectBoolean(String key, Boolean value) {
		JsonObject json = new JsonObject();
		json.addProperty(key, value);
		return json.toString();
	}
}
