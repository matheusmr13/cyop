package br.com.cyop.service;

import javax.ws.rs.core.Response;


public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception exception) {
		exception.printStackTrace();
		return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
	}

}
