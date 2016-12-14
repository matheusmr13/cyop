package br.com.cyop.service;

import java.util.HashSet;
import java.util.Set;

import br.com.cyop.api.RestMethodsRS;
import br.com.cyop.exception.InvalidFieldTypeExceptionMapper;
import br.com.cyop.exception.NotFoundExceptionMapper;

public class Application extends javax.ws.rs.core.Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();

		classes.add(RestMethodsRS.class);

		classes.add(NotFoundExceptionMapper.class);
		classes.add(InvalidFieldTypeExceptionMapper.class);
		classes.add(ExceptionMapper.class);
		return classes;
	}
}
