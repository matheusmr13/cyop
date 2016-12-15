package br.com.cyop.endpoint;

import java.math.BigDecimal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public enum PropertieType {
	INTEGER {
		@Override
		public Integer getPropertie(JsonElement json) {
			return json.getAsInt();
		}
	},
	TEXT {
		@Override
		public String getPropertie(JsonElement json) {
			return json.getAsString();
		}
	},
	DECIMAL {
		@Override
		public BigDecimal getPropertie(JsonElement json) {
			return json.getAsBigDecimal();
		}
	},
	BOOLEAN {
		@Override
		public Boolean getPropertie(JsonElement json) {
			return json.getAsBoolean();
		}
	},
	LIST {
		@Override
		public JsonArray getPropertie(JsonElement json) {
			return json.getAsJsonArray();
		}
	},
	ENUMERATOR {
		@Override
		public String getPropertie(JsonElement json) {
			return json.getAsString();
		}
	},
	ENDPOINT {
		@Override
		public JsonObject getPropertie(JsonElement json) {
			return json.getAsJsonObject();
		}
	};

	public Object getPropertie(JsonElement json) {
		return json;
	}

	public void addProperty(JsonObject instanceJson, JsonElement jsonElement, String propertieName) {

	}
}
