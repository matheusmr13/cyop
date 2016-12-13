package br.com.cyop.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import br.com.cyop.version.Version;
import io.yawp.testing.EndpointTestCaseBase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;

import br.com.cyop.endpoint.Endpoint;
import br.com.cyop.endpoint.Propertie;
import br.com.cyop.endpoint.PropertieType;
import br.com.cyop.api.Instance;
import br.com.cyop.api.RestMethodsService;
import br.com.cyop.service.NotFoundException;

public class InstanceServiceTest extends EndpointTestCaseBase {

	@Before
	public void before() {
		List<Propertie> properties = new ArrayList<>();
		properties.add(Propertie.create("bar", PropertieType.STRING));

		Version v1 = Version.create("v1");
		yawp.save(v1);

		yawp.saveWithHooks(Endpoint.create("foo", v1, properties));
	}

	@Test
	public void createInstanceTest() {
		feature(RestMethodsService.class).createInstance("v1", "foo", "{\"bar\":\"Matheus\"}");

		Instance instance = yawp(Instance.class).first();
		assertEquals(new Long(1), instance.getId());
		assertNotNull(instance.getObject());
		assertFalse(instance.getObject().get("bar").isJsonNull());
		assertEquals("Matheus", instance.getObject().get("bar").getAsString());
	}

	@Test(expected = NotFoundException.class)
	public void createInstanceWithoutCorrectEndpointTest() {
		feature(RestMethodsService.class).createInstance("v1", "bar", "{\"foo\":\"Matheus\"}");
	}

	@Test
	public void fintInstanceByIdTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		feature.createInstance("v1", "foo", "{\"bar\":\"Matheus\"}");

		assertNotNull(feature.getInstanceById("v1", "foo", 1l));
	}

	@Test(expected = NotFoundException.class)
	public void fintInstanceByIdWithoutCorrectEndpointTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		feature.createInstance("v1", "foo", "{\"bar\":\"Matheus\"}");

		assertNotNull(feature.getInstanceById("v1", "bar", 1l));
	}

	@Test(expected = NotFoundException.class)
	public void fintInstanceByIdWithoutExistingIdTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		feature.createInstance("v1", "foo", "{\"bar\":\"Matheus\"}");

		assertNotNull(feature.getInstanceById("v1", "foo", 2l));
	}

	@Test
	public void updateInstanceTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		feature.createInstance("v1", "foo", "{\"bar\":\"Matheus\"}");

		feature.updateInstance("v1", "foo", 1l, "{\"bar\":\"Joao\"}");
		Instance instance = yawp(Instance.class).first();
		assertEquals(new Long(1), instance.getId());
		assertNotNull(instance.getObject());
		assertFalse(instance.getObject().get("bar").isJsonNull());
		assertEquals("Joao", instance.getObject().get("bar").getAsString());
	}

	@Test(expected = NotFoundException.class)
	public void updateInstanceWithoutEndpointTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		feature.createInstance("v1", "foo", "{\"bar\":\"Matheus\"}");
		feature.updateInstance("v1", "bar", 1l, "{}");
	}

	@Test(expected = NotFoundException.class)
	public void updateInstanceWithoutExistingIdTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		feature.createInstance("v1", "foo", "{\"bar\":\"Matheus\"}");
		feature.updateInstance("v1", "foo", 2l, "{}");
	}

	@Test
	public void deleteInstanceTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		feature.createInstance("v1", "foo", "{\"bar\":\"Matheus\"}");

		feature.deleteEntity("v1", "foo", 1l);
		Instance instance = yawp(Instance.class).first();
		assertEquals(null, instance);
	}

	@Test(expected = NotFoundException.class)
	public void deleteInstanceWithoutEndpointTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		feature.createInstance("v1", "foo", "{\"bar\":\"Matheus\"}");
		feature.deleteEntity("v1", "bar", 1l);
	}

	@Test(expected = NotFoundException.class)
	public void deleteInstanceWithoutExistingIdTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		feature.createInstance("v1", "foo", "{\"bar\":\"Matheus\"}");
		feature.deleteEntity("v1", "foo", 2l);
	}

	@Test
	public void listInstancesTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		feature.createInstance("v1", "foo", "{\"bar\":\"Matheus\"}");
		feature.createInstance("v1", "foo", "{\"bar\":\"Joao\"}");
		feature.createInstance("v1", "foo", "{\"bar\":\"Filipe\"}");
		feature.createInstance("v1", "foo", "{\"bar\":\"Matheus M\"}");

		List<JsonObject> listOfInstance = feature.getListOfInstance("v1", "foo");
		assertEquals(4, listOfInstance.size());
	}

	@Test(expected = NotFoundException.class)
	public void listInstancesWithoutEndpointTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		feature.createInstance("v1", "foo", "{\"bar\":\"Matheus\"}");
		feature.getListOfInstance("v1", "bar");
	}
}
