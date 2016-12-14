package br.com.cyop.version;

import io.yawp.repository.Feature;

import java.util.Date;
import java.util.List;

public class VersionService extends Feature {

	public Version newVersion() {
		Version lastVersion = yawp(Version.class).order("number", "desc").first();

		Version newVersion = new Version();
		if (lastVersion == null) {
			newVersion.number = 1;
		} else {
			newVersion.number = lastVersion.number + 1;
		}
		newVersion.url = "v" + newVersion.number;
		newVersion.creationDate = new Date();

		return yawp.save(newVersion);
	}

	public Version getVersionByUrl(String url) {
		List<Version> versionWithUrl = yawp(Version.class).where("url", "=", url).and("active", "=", true).list();
		if (versionWithUrl.size() > 1) {
			throw new RuntimeException("More then one active version with same url");
		}
		return versionWithUrl.size() == 0 ? null : versionWithUrl.get(0);
	}
}
