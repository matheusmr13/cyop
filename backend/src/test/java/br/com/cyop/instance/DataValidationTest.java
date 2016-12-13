package br.com.cyop.instance;

import br.com.cyop.api.Instance;
import br.com.cyop.api.RestMethodsService;
import br.com.cyop.endpoint.Endpoint;
import br.com.cyop.endpoint.Propertie;
import br.com.cyop.endpoint.PropertieType;
import br.com.cyop.service.NotFoundException;
import br.com.cyop.version.Version;
import com.google.gson.JsonObject;
import io.yawp.testing.EndpointTestCaseBase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
}
