package br.com.cyop.api;

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
import br.com.cyop.endpoint.Property;
import br.com.cyop.endpoint.PropertyType;
import br.com.cyop.exception.NotFoundException;

public class SimpleApiTest extends EndpointTestCaseBase {

	@Before
	public void before() {
		List<Property> properties = new ArrayList<>();
		properties.add(Property.create("bar", PropertyType.TEXT));

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
		assertNotNull(instance.getYawpId());
		assertNotNull(instance.getEndpointId());
		assertFalse(instance.getObject().get("bar").isJsonNull());
		assertEquals("Matheus", instance.getObject().get("bar").getAsString());
	}

	@Test
	public void createInstanceWithoutObjectTest() {
		feature(RestMethodsService.class).createInstance("v1", "foo", "");

		Instance instance = yawp(Instance.class).first();
		assertEquals(new Long(1), instance.getId());
		assertNotNull(instance.getObject());
		assertNotNull(instance.getYawpId());
		assertNotNull(instance.getEndpointId());
	}

	@Test(expected = NotFoundException.class)
	public void createInstanceWithIncorrectEndpointTest() {
		feature(RestMethodsService.class).createInstance("v1", "bar", "{\"foo\":\"Matheus\"}");
	}

	@Test(expected = NotFoundException.class)
	public void createInstanceWithIncorrectVersionTest() {
		feature(RestMethodsService.class).createInstance("v2", "foo", "{\"foo\":\"Matheus\"}");
	}

	@Test(expected = NotFoundException.class)
	public void createInstanceWithNullEndpointTest() {
		feature(RestMethodsService.class).createInstance("v1", null, "{\"foo\":\"Matheus\"}");
	}

	@Test(expected = NotFoundException.class)
	public void createInstanceWithNullVersionTest() {
		feature(RestMethodsService.class).createInstance(null, "foo", "{\"foo\":\"Matheus\"}");
	}

	@Test
	public void fintInstanceByIdTest() {
		RestMethodsService feature = feature(RestMethodsService.class);
		feature.createInstance("v1", "foo", "{\"bar\":\"Matheus\"}");

		assertNotNull(feature.getInstanceById("v1", "foo", 1l));
	}

	@Test(expected = NotFoundException.class)
	public void fintInstanceByIdWithIncorrectEndpointTest() {
		assertNotNull(feature(RestMethodsService.class).getInstanceById("v1", "bar", 1l));
	}

	@Test(expected = NotFoundException.class)
	public void fintInstanceByIdWithIncorrectVersionTest() {
		assertNotNull(feature(RestMethodsService.class).getInstanceById("v2", "foo", 1l));
	}

	@Test(expected = NotFoundException.class)
	public void fintInstanceByIdWithNullEndpointTest() {
		assertNotNull(feature(RestMethodsService.class).getInstanceById("v1", null, 1l));
	}

	@Test(expected = NotFoundException.class)
	public void fintInstanceByIdWithNullVersionTest() {
		assertNotNull(feature(RestMethodsService.class).getInstanceById(null, "foo", 1l));
	}

	@Test(expected = NotFoundException.class)
	public void fintInstanceByIdWithInexistingIdTest() {
		assertNotNull(feature(RestMethodsService.class).getInstanceById("v1", "foo", 2l));
	}

	@Test(expected = NotFoundException.class)
	public void fintInstanceByIdWithNullIdTest() {
		assertNotNull(feature(RestMethodsService.class).getInstanceById("v1", "foo", null));
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
	public void updateInstanceWithIncorrectEndpointTest() {
		feature(RestMethodsService.class).updateInstance("v1", "bar", 1l, "{}");
	}

	@Test(expected = NotFoundException.class)
	public void updateInstanceWithInexistingIdTest() {
		feature(RestMethodsService.class).updateInstance("v1", "foo", 2l, "{}");
	}

	@Test(expected = NotFoundException.class)
	public void updateInstanceWithNullEndpointTest() {
		feature(RestMethodsService.class).updateInstance("v1", null, 1l, "{}");
	}

	@Test(expected = NotFoundException.class)
	public void updateInstanceWithNullVersionTest() {
		feature(RestMethodsService.class).updateInstance(null, "foo", 2l, "{}");
	}

	@Test(expected = NotFoundException.class)
	public void updateInstanceWithNullIdTest() {
		feature(RestMethodsService.class).updateInstance("v1", "foo", null, "{}");
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
	public void deleteInstanceWithIncorrectEndpointTest() {
		feature(RestMethodsService.class).deleteEntity("v1", "bar", 1l);
	}
	@Test(expected = NotFoundException.class)
	public void deleteInstanceWithIncorrectVersionTest() {
		feature(RestMethodsService.class).deleteEntity("v2", "foo", 1l);
	}

	@Test(expected = NotFoundException.class)
	public void deleteInstanceWithNullEndpointTest() {
		feature(RestMethodsService.class).deleteEntity("v1", null, 1l);
	}
	@Test(expected = NotFoundException.class)
	public void deleteInstanceWithNullVersionTest() {
		feature(RestMethodsService.class).deleteEntity(null, "foo", 1l);
	}

	@Test(expected = NotFoundException.class)
	public void deleteInstanceWithInexistingIdTest() {
		feature(RestMethodsService.class).deleteEntity("v1", "foo", 2l);
	}

	@Test(expected = NotFoundException.class)
	public void deleteInstanceWithNullIdTest() {
		feature(RestMethodsService.class).deleteEntity("v1", "foo", null);
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
	public void listInstancesWithIncorrectEndpointTest() {
		feature(RestMethodsService.class).getListOfInstance("v1", "bar");
	}

	@Test(expected = NotFoundException.class)
	public void listInstancesWithIncorrectVersionTest() {
		feature(RestMethodsService.class).getListOfInstance("v2", "foo");
	}

	@Test(expected = NotFoundException.class)
	public void listInstancesWithNullEndpointTest() {
		feature(RestMethodsService.class).getListOfInstance("v1", null);
	}

	@Test(expected = NotFoundException.class)
	public void listInstancesWithNullVersionTest() {
		feature(RestMethodsService.class).getListOfInstance(null, "foo");
	}
}
