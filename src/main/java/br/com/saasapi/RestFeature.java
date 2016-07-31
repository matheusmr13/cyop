package br.com.saasapi;

import io.yawp.repository.Feature;
import io.yawp.repository.Repository;
import io.yawp.repository.Yawp;
import io.yawp.repository.query.QueryBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class RestFeature extends Feature {

	public synchronized Repository yawp() {
		if (yawp == null) {
			yawp = Yawp.yawp();
		}

		return yawp;
	}

	public <T> QueryBuilder<T> yawp(Class<T> clazz) {
		return yawp().query(clazz);
	}

	public <T> QueryBuilder<T> yawpWithHooks(Class<T> clazz) {
		return yawp().queryWithHooks(clazz);
	}

	public <T extends Feature> T feature(Class<T> clazz) {
		yawp();
		return super.feature(clazz);
	}

	public JsonElement toJson(String objAsString) {
		return new JsonParser().parse(objAsString);
	}
}
