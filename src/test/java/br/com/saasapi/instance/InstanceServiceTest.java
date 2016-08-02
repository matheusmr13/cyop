package br.com.saasapi.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import io.yawp.testing.EndpointTestCaseBase;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.saasapi.endpoint.Endpoint;
import br.com.saasapi.endpoint.Propertie;
import br.com.saasapi.endpoint.PropertieType;
import br.com.saasapi.entity.Instance;
import br.com.saasapi.entity.InstanceService;
import br.com.saasapi.service.NotFoundException;

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
	public void updateInstanceIdWithoutEndpointTest() {
		InstanceService feature = feature(InstanceService.class);
		feature.createInstance("foo", "{\"bar\":\"Matheus\"}");
		feature.updateInstance("bar", "1", "{}");
	}

	@Test(expected = NotFoundException.class)
	public void updateInstanceIdWithoutExistingIdTest() {
		InstanceService feature = feature(InstanceService.class);
		feature.createInstance("foo", "{\"bar\":\"Matheus\"}");
		feature.updateInstance("foo", "2", "{}");
	}
}
