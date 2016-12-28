package br.com.cyop.service;

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

	public <T extends Feature> T feature(Class<T> clazz) {
		yawp();
		return super.feature(clazz);
	}

}
