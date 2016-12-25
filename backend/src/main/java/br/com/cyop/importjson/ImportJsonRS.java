package br.com.cyop.importjson;

import br.com.cyop.service.RestFeature;
import com.google.gson.JsonParser;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/import")
public class ImportJsonRS extends RestFeature {
	@POST
	@Path("/json")
	public Response importJson(@FormParam("json") String jsonApi) {
		feature(ImportJsonService.class).importJson(new JsonParser().parse(jsonApi).getAsJsonObject());
		return Response.ok().build();
	}
}
