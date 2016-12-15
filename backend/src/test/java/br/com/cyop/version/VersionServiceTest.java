package br.com.cyop.version;

import br.com.cyop.exception.NotFoundException;
import io.yawp.testing.EndpointTestCaseBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class VersionServiceTest extends EndpointTestCaseBase {

	@Test
	public void newVersionTest() {
		VersionService feature = feature(VersionService.class);
		feature.newVersion();
		assertNotNull(feature.getVersionByUrl("v1"));
		feature.newVersion();
		assertNotNull(feature.getVersionByUrl("v2"));
	}

	@Test(expected = NotFoundException.class)
	public void versionNotFoundTest() {
		VersionService feature = feature(VersionService.class);
		assertNull(feature.getVersionByUrl("v1"));
	}

	@Test(expected = RuntimeException.class)
	public void onlyOneVersionWithSameUrlTest() {
		Version v1 = yawp.save(Version.create("v1"));
		Version otherV1 = yawp.save(Version.create("v1"));
		VersionService feature = feature(VersionService.class);
		assertNull(feature.getVersionByUrl("v1"));
	}

}
