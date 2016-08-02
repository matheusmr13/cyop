package br.com.saasapi.service;

import javax.ws.rs.core.Response;

public class NotFoundExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<NotFoundException> {

	@Override
	public Response toResponse(NotFoundException exception) {
		exception.printStackTrace();
		return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
	}

}
