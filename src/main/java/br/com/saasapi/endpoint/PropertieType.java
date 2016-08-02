package br.com.saasapi.endpoint;

import java.math.BigDecimal;

import com.google.gson.JsonElement;

public enum PropertieType {
	INTEGER {
		@Override
		public Integer getPropertie(JsonElement json) {
			return json.getAsInt();
		}
	},
	STRING {
		@Override
		public String getPropertie(JsonElement json) {
			return json.getAsString();
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
	};

	public Object getPropertie(JsonElement json) {
		return json;
	}
}
