package br.com.saasapi.service;

import java.util.HashSet;
import java.util.Set;

import br.com.saasapi.entity.InstanceRS;

public class Application extends javax.ws.rs.core.Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();

		classes.add(InstanceRS.class);

		classes.add(ExceptionMapper.class);
		return classes;
	}
}
