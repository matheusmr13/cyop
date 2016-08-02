package br.com.saasapi.service;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5066028524909569661L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}

}
