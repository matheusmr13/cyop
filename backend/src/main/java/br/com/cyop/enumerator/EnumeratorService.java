package br.com.cyop.enumerator;

import br.com.cyop.version.Version;
import br.com.cyop.version.VersionService;
import io.yawp.repository.Feature;

import java.util.List;

public class EnumeratorService extends Feature {

	public Enumerator createInstance(String versionUrl, String enumeratorName, List<String> values) {
		Version version = feature(VersionService.class).getVersionByUrl(versionUrl);
		return yawp.save(Enumerator.create(enumeratorName, version, values));
	}
}
