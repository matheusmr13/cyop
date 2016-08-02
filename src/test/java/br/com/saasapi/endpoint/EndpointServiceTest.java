package br.com.saasapi.endpoint;

import static org.junit.Assert.*;

import org.junit.Test;

import io.yawp.testing.EndpointTestCaseBase;

public class EndpointServiceTest extends EndpointTestCaseBase {

	@Test
	public void updateEndpointIdTest() {
		Endpoint endpoint = new Endpoint();
		yawp.saveWithHooks(endpoint);

		endpoint = yawp(Endpoint.class).first();
		assertEquals(new Integer(0), endpoint.maxId);
		feature(EndpointService.class).updateId(endpoint);

		endpoint = yawp(Endpoint.class).first();
		assertEquals(new Integer(1), endpoint.maxId);
	}

}
