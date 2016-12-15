package br.com.cyop.api;

import br.com.cyop.endpoint.Endpoint;
import br.com.cyop.endpoint.Property;
import br.com.cyop.endpoint.PropertyType;
import br.com.cyop.exception.NotFoundException;
import br.com.cyop.version.Version;
import com.google.gson.JsonObject;
import io.yawp.testing.EndpointTestCaseBase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PropertySetupTest extends EndpointTestCaseBase {

	@Before
	public void before() {
		List<Property> properties = new ArrayList<>();
		Property property = Property.createWithDefault("bar", PropertyType.TEXT, "default value");
		properties.add(property);

		Version v1 = Version.create("v1");
		yawp.save(v1);

		yawp.saveWithHooks(Endpoint.create("foo", v1, properties));
	}

	@Test
	public void dontSetDefaultTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		JsonObject instance = feature.createInstance("v1", "foo", "{\"bar\":\"Matheus\"}");
		assertEquals("Matheus", instance.get("bar").getAsString());

		instance = feature.createInstance("v1", "foo", "{\"bar\":\"\"}");
		assertEquals("", instance.get("bar").getAsString());
	}
	
	@Test
	public void useDefaultTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		JsonObject instance = feature.createInstance("v1", "foo", "{}");
		assertEquals("default value", instance.get("bar").getAsString());

		instance = feature.createInstance("v1", "foo", "{\"bar\": null}");
		assertEquals("default value", instance.get("bar").getAsString());
	}
}
