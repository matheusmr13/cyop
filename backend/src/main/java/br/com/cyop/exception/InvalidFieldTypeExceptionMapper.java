package br.com.cyop.exception;

import javax.ws.rs.core.Response;

public class InvalidFieldTypeExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<InvalidFieldTypeException> {

	@Override
	public Response toResponse(InvalidFieldTypeException exception) {
		exception.printStackTrace();
		return Response.status(Response.Status.FORBIDDEN).entity(exception.getMessage()).build();
	}

}
