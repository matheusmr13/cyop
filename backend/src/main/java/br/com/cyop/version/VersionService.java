package br.com.cyop.version;

import io.yawp.repository.Feature;

import java.util.Date;

public class VersionService extends Feature {

	public Version newVersion() {
		Version lastVersion = yawp(Version.class).order("number", "desc").first();

		Version newVersion = new Version();
		newVersion.number = lastVersion.number + 1;
		newVersion.url = "v" + newVersion.number;
		newVersion.creationDate = new Date();

		return yawp.save(newVersion);
	}

	public Version gerVersionByUrl(String url) {
		return yawp(Version.class).where("url","=", url).only();
	}
}
