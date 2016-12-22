package br.com.cyop.importjson;

import br.com.cyop.endpoint.Endpoint;
import br.com.cyop.endpoint.Property;
import br.com.cyop.endpoint.PropertyType;
import br.com.cyop.enumerator.Enumerator;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.yawp.repository.IdRef;
import io.yawp.testing.EndpointTestCaseBase;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ImportJsonServiceTest extends EndpointTestCaseBase {

	@Test
	public void importSimpleJsonTest() {
		feature(ImportJsonService.class).importJson(getFile("simpleImportExample.json"));

		List<Enumerator> createdEnumerators = yawp(Enumerator.class).list();
		assertEquals(1, createdEnumerators.size());

		Enumerator job = createdEnumerators.get(0);
		assertEquals("job", job.getUrl());
		List<String> values = job.getValues();
		assertEquals(4, values.size());
		assertTrue(values.contains("teacher"));
		assertTrue(values.contains("cientist"));
		assertTrue(values.contains("officer"));
		assertTrue(values.contains("developer"));

		List<Endpoint> createdEndpoints = yawp(Endpoint.class).list();
		assertEquals(1, createdEndpoints.size());

		Endpoint person = createdEndpoints.get(0);
		assertEquals("person", person.getUrl());
		assertEquals(new Long(0), person.getMaxId());
		List<Property> properties = person.getProperties();
		assertEquals(5, properties.size());

		testProperty(properties, "name", null, null, PropertyType.TEXT);
		testProperty(properties, "age", null, null, PropertyType.INTEGER);
		testProperty(properties, "mother", person.getId(), null, PropertyType.ENDPOINT);
		testProperty(properties, "father", person.getId(), null, PropertyType.ENDPOINT);
		testProperty(properties, "actual_job", null, job.getId(), PropertyType.ENUMERATOR);
	}

	private void testProperty(List<Property> properties, String name, IdRef<Endpoint> endpointId, IdRef<Enumerator> enumeratorType, PropertyType propertyType) {
		for (Property property : properties) {
			if (name.equals(property.getName())) {
				assertEquals(endpointId, property.getEndpointId());
				assertEquals(enumeratorType, property.getEnumeratorType());
				assertEquals(propertyType, property.getType());
				return;
			}
		}
		fail();
	}


	private JsonObject getFile(String fileName) {
		StringBuilder result = new StringBuilder("");
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new JsonParser().parse(result.toString()).getAsJsonObject();
	}
}
