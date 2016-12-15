package br.com.cyop.enumerator;

import br.com.cyop.api.Instance;
import br.com.cyop.endpoint.Endpoint;
import br.com.cyop.endpoint.EndpointService;
import br.com.cyop.endpoint.Propertie;
import br.com.cyop.exception.InvalidFieldTypeException;
import br.com.cyop.exception.NotFoundException;
import br.com.cyop.version.Version;
import br.com.cyop.version.VersionService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.yawp.repository.Feature;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EnumeratorService extends Feature {

	public Enumerator createInstance(String versionUrl, String enumeratorName, List<String> values) {
		Version version = feature(VersionService.class).getVersionByUrl(versionUrl);
		return yawp.save(Enumerator.create(enumeratorName, version, values));
	}
}
