package br.com.cyop.endpoint;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.cyop.endpoint.Endpoint;
import br.com.cyop.endpoint.EndpointService;
import io.yawp.testing.EndpointTestCaseBase;

public class EndpointServiceTest extends EndpointTestCaseBase {

	@Test
	public void updateEndpointIdTest() {
		Endpoint endpoint = new Endpoint();
		yawp.saveWithHooks(endpoint);

		endpoint = yawp(Endpoint.class).first();
		assertEquals(new Long(0), endpoint.maxId);
		feature(EndpointService.class).updateId(endpoint);

		endpoint = yawp(Endpoint.class).first();
		assertEquals(new Long(1), endpoint.maxId);
	}

}
