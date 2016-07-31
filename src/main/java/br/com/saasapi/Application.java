package br.com.saasapi;

import java.util.HashSet;
import java.util.Set;

public class Application extends javax.ws.rs.core.Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();

		classes.add(InstanceRS.class);

		classes.add(ExceptionMapper.class);
		return classes;
	}
}
