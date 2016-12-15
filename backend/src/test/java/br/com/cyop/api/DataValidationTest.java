package br.com.cyop.api;

import br.com.cyop.endpoint.Endpoint;
import br.com.cyop.endpoint.Property;
import br.com.cyop.endpoint.PropertyType;
import br.com.cyop.enumerator.Enumerator;
import br.com.cyop.enumerator.EnumeratorService;
import br.com.cyop.exception.InvalidFieldTypeException;
import br.com.cyop.exception.NotFoundException;
import br.com.cyop.version.Version;
import com.google.gson.JsonObject;
import io.yawp.testing.EndpointTestCaseBase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DataValidationTest extends EndpointTestCaseBase {

	Version v1;
	@Before
	public void before() {
		v1 = Version.create("v1");
		yawp.save(v1);

		Enumerator enumeratorTest = feature(EnumeratorService.class).createInstance("v1", "enumeratorTest", Arrays.asList("ADMIN", "0", "true"));

		List<Property> properties = new ArrayList<>();
		properties.add(Property.create("name", PropertyType.TEXT));
		properties.add(Property.create("age", PropertyType.INTEGER));
		properties.add(Property.create("active", PropertyType.BOOLEAN));
		properties.add(Property.create("height", PropertyType.DECIMAL));
		properties.add(Property.createEnumeratorType("enumerator", enumeratorTest));

		Endpoint person = yawp.saveWithHooks(Endpoint.create("person", v1, properties));

		person.getProperties().add(Property.createEndpointType("father", person));
		yawp.save(person);
	}

	@Test
	public void textPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "name";
		assertEquals(newJsonObjectString(testingProperty, "Matheus"), createInstance(feature, newJsonObjectString(testingProperty, "Matheus")).toString());
		assertEquals(newJsonObjectString(testingProperty, "1000"), createInstance(feature, newJsonObjectNumber(testingProperty, 1000)).toString());
		assertEquals(newJsonObjectString(testingProperty, "true"), createInstance(feature, newJsonObjectBoolean(testingProperty, true)).toString());
		assertEquals(newJsonObjectString(testingProperty, "null"), createInstance(feature, newJsonObjectString(testingProperty, "null")).toString());
		assertFalse(createInstance(feature, newJsonObjectString(testingProperty, null)).has(testingProperty));
	}

	@Test
	public void integerPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "age";
		assertEquals(newJsonObjectNumber(testingProperty, 1000), createInstance(feature, newJsonObjectNumber(testingProperty, 1000)).toString());
		assertEquals(newJsonObjectNumber(testingProperty, 1000), createInstance(feature, newJsonObjectString(testingProperty, "1000")).toString());
		assertFalse(createInstance(feature, newJsonObjectNumber(testingProperty, null)).has(testingProperty));
	}

	@Test(expected = InvalidFieldTypeException.class)
	public void integerInvalidLetterPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "age";
		createInstance(feature, newJsonObjectString(testingProperty, "stringTest"));
	}

	@Test(expected = InvalidFieldTypeException.class)
	public void integerInvalidEmptyPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "age";
		createInstance(feature, newJsonObjectString(testingProperty, ""));
	}

	@Test
	public void booleanPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "active";
		assertEquals(newJsonObjectBoolean(testingProperty, true), createInstance(feature, newJsonObjectBoolean(testingProperty, true)).toString());
		assertEquals(newJsonObjectBoolean(testingProperty, true), createInstance(feature, newJsonObjectString(testingProperty, "true")).toString());
		assertEquals(newJsonObjectBoolean(testingProperty, true), createInstance(feature, newJsonObjectString(testingProperty, "1")).toString());
		assertEquals(newJsonObjectBoolean(testingProperty, true), createInstance(feature, newJsonObjectNumber(testingProperty, 1)).toString());
		assertEquals(newJsonObjectBoolean(testingProperty, false), createInstance(feature, newJsonObjectBoolean(testingProperty, false)).toString());
		assertEquals(newJsonObjectBoolean(testingProperty, false), createInstance(feature, newJsonObjectString(testingProperty, "false")).toString());
		assertEquals(newJsonObjectBoolean(testingProperty, false), createInstance(feature, newJsonObjectString(testingProperty, "0")).toString());
		assertEquals(newJsonObjectBoolean(testingProperty, false), createInstance(feature, newJsonObjectNumber(testingProperty, 0)).toString());
		assertFalse(createInstance(feature, newJsonObjectBoolean(testingProperty, null)).has(testingProperty));
	}

	@Test(expected = InvalidFieldTypeException.class)
	public void booleanInvalidLetterPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "active";
		createInstance(feature, newJsonObjectString(testingProperty, "stringTest"));
	}

	@Test(expected = InvalidFieldTypeException.class)
	public void booleanInvalidEmptyPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "active";
		createInstance(feature, newJsonObjectString(testingProperty, ""));
	}

	@Test(expected = InvalidFieldTypeException.class)
	public void booleanInvalidNumberPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "active";
		createInstance(feature, newJsonObjectNumber(testingProperty, 2));
	}

	@Test
	public void decimalPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "height";
		assertEquals(newJsonObjectNumber(testingProperty, 1), createInstance(feature, newJsonObjectNumber(testingProperty, 1)).toString());
		assertEquals(newJsonObjectNumber(testingProperty, 1.2), createInstance(feature, newJsonObjectNumber(testingProperty, 1.2)).toString());
		assertEquals(newJsonObjectNumber(testingProperty, 0), createInstance(feature, newJsonObjectNumber(testingProperty, 0)).toString());
		assertEquals(newJsonObjectNumber(testingProperty, 123.123), createInstance(feature, newJsonObjectString(testingProperty, "123.123")).toString());
		assertFalse(createInstance(feature, newJsonObjectBoolean(testingProperty, null)).has(testingProperty));
	}

	@Test(expected = InvalidFieldTypeException.class)
	public void decimalInvalidLetterPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "height";
		createInstance(feature, newJsonObjectString(testingProperty, "stringTest"));
	}

	@Test(expected = InvalidFieldTypeException.class)
	public void decimalInvalidEmptyPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "height";
		createInstance(feature, newJsonObjectString(testingProperty, ""));
	}

	@Test(expected = InvalidFieldTypeException.class)
	public void decimalInvalidNumberPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "height";
		createInstance(feature, newJsonObjectBoolean(testingProperty, true));
	}

	@Test
	public void enumeratorPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "enumerator";
		assertEquals(newJsonObjectString(testingProperty, "ADMIN"), createInstance(feature, newJsonObjectString(testingProperty, "ADMIN")).toString());
		assertEquals(newJsonObjectString(testingProperty, "0"), createInstance(feature, newJsonObjectNumber(testingProperty, 0)).toString());
		assertEquals(newJsonObjectString(testingProperty, "true"), createInstance(feature, newJsonObjectBoolean(testingProperty, true)).toString());
		assertFalse(createInstance(feature, newJsonObjectBoolean(testingProperty, null)).has(testingProperty));
	}

	@Test(expected = InvalidFieldTypeException.class)
	public void enumeratorInexistentPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);

		String testingProperty = "enumerator";
		createInstance(feature, newJsonObjectString(testingProperty, "InexistentEnumerator"));
	}

	@Test
	public void endpointPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);

		String fatherId = feature.createInstance("v1", "person", newJsonObjectString("name", "father")).get("id").getAsString();

		String testingProperty = "father";
		assertEquals(newJsonObjectString(testingProperty, fatherId), createInstance(feature, newJsonObjectString(testingProperty, fatherId)).toString());
		assertFalse(createInstance(feature, newJsonObjectBoolean(testingProperty, null)).has(testingProperty));
	}

	@Test(expected = NotFoundException.class)
	public void endpointInexistentPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);

		String testingProperty = "father";
		createInstance(feature, newJsonObjectString(testingProperty, "999999"));
	}

	@Test(expected = InvalidFieldTypeException.class)
	public void endpointInvalidPropertyTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		String testingProperty = "father";
		createInstance(feature, newJsonObjectString(testingProperty, "stringTest"));
	}

	private JsonObject createInstance(RestMethodsService feature, String jsonString) {
		JsonObject instance = feature.createInstance("v1", "person", jsonString);
		instance.remove("id");
		return instance;
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
