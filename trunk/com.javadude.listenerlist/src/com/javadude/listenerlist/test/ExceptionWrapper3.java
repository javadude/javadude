package com.javadude.listenerlist.test;

public class ExceptionWrapper3 extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ExceptionWrapper3(Throwable cause) {
		super(cause);
	}
	public ExceptionWrapper3(String message, Throwable cause) {
		super(message, cause);
	}
}
