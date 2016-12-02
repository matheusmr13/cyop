package br.com.cyop.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import io.yawp.testing.EndpointTestCaseBase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;

import br.com.cyop.endpoint.Endpoint;
import br.com.cyop.endpoint.Propertie;
import br.com.cyop.endpoint.PropertieType;
import br.com.cyop.entity.Instance;
import br.com.cyop.entity.InstanceService;
import br.com.cyop.service.NotFoundException;

public class InstanceServiceTest extends EndpointTestCaseBase {

	@Before
	public void before() {
		List<Propertie> properties = new ArrayList<>();
		properties.add(Propertie.create("bar", PropertieType.STRING));
		yawp.saveWithHooks(Endpoint.create("foo", properties));
	}

	@Test
	public void createInstanceTest() {
		feature(InstanceService.class).createInstance("foo", "{\"bar\":\"Matheus\"}");

		Instance instance = yawp(Instance.class).first();
		assertEquals("1", instance.getId());
		assertNotNull(instance.getObject());
		assertFalse(instance.getObject().get("bar").isJsonNull());
		assertEquals("Matheus", instance.getObject().get("bar").getAsString());
	}

	@Test(expected = NotFoundException.class)
	public void createInstanceWithoutCorrectEndpointTest() {
		feature(InstanceService.class).createInstance("bar", "{\"foo\":\"Matheus\"}");
	}

	@Test
	public void fintInstanceByIdTest() {
		InstanceService feature = feature(InstanceService.class);
		feature.createInstance("foo", "{\"bar\":\"Matheus\"}");

		assertNotNull(feature.getInstanceById("foo", "1"));
	}

	@Test(expected = NotFoundException.class)
	public void fintInstanceByIdWithoutCorrectEndpointTest() {
		InstanceService feature = feature(InstanceService.class);
		feature.createInstance("foo", "{\"bar\":\"Matheus\"}");

		assertNotNull(feature.getInstanceById("bar", "1"));
	}

	@Test(expected = NotFoundException.class)
	public void fintInstanceByIdWithoutExistingIdTest() {
		InstanceService feature = feature(InstanceService.class);
		feature.createInstance("foo", "{\"bar\":\"Matheus\"}");

		assertNotNull(feature.getInstanceById("foo", "2"));
	}

	@Test
	public void updateInstanceTest() {
		InstanceService feature = feature(InstanceService.class);
		feature.createInstance("foo", "{\"bar\":\"Matheus\"}");

		feature.updateInstance("foo", "1", "{\"bar\":\"Joao\"}");
		Instance instance = yawp(Instance.class).first();
		assertEquals("1", instance.getId());
		assertNotNull(instance.getObject());
		assertFalse(instance.getObject().get("bar").isJsonNull());
		assertEquals("Joao", instance.getObject().get("bar").getAsString());
	}

	@Test(expected = NotFoundException.class)
	public void updateInstanceWithoutEndpointTest() {
		InstanceService feature = feature(InstanceService.class);
		feature.createInstance("foo", "{\"bar\":\"Matheus\"}");
		feature.updateInstance("bar", "1", "{}");
	}

	@Test(expected = NotFoundException.class)
	public void updateInstanceWithoutExistingIdTest() {
		InstanceService feature = feature(InstanceService.class);
		feature.createInstance("foo", "{\"bar\":\"Matheus\"}");
		feature.updateInstance("foo", "2", "{}");
	}

	@Test
	public void deleteInstanceTest() {
		InstanceService feature = feature(InstanceService.class);
		feature.createInstance("foo", "{\"bar\":\"Matheus\"}");

		feature.deleteEntity("foo", "1");
		Instance instance = yawp(Instance.class).first();
		assertEquals(null, instance);
	}

	@Test(expected = NotFoundException.class)
	public void deleteInstanceWithoutEndpointTest() {
		InstanceService feature = feature(InstanceService.class);
		feature.createInstance("foo", "{\"bar\":\"Matheus\"}");
		feature.deleteEntity("bar", "1");
	}

	@Test(expected = NotFoundException.class)
	public void deleteInstanceWithoutExistingIdTest() {
		InstanceService feature = feature(InstanceService.class);
		feature.createInstance("foo", "{\"bar\":\"Matheus\"}");
		feature.deleteEntity("foo", "2");
	}

	@Test
	public void listInstancesTest() {
		InstanceService feature = feature(InstanceService.class);
		feature.createInstance("foo", "{\"bar\":\"Matheus\"}");
		feature.createInstance("foo", "{\"bar\":\"Joao\"}");
		feature.createInstance("foo", "{\"bar\":\"Filipe\"}");
		feature.createInstance("foo", "{\"bar\":\"Matheus M\"}");

		List<JsonObject> listOfInstance = feature.getListOfInstance("foo");
		assertEquals(4, listOfInstance.size());
	}

	@Test(expected = NotFoundException.class)
	public void listInstancesWithoutEndpointTest() {
		InstanceService feature = feature(InstanceService.class);
		feature.createInstance("foo", "{\"bar\":\"Matheus\"}");
		feature.getListOfInstance("bar");
	}
}
