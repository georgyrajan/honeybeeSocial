package com.oracle.survey.surveycustomer.exception;

public class CustomerException extends RuntimeException {

	private static final long serialVersionUID = 3601944937043929705L;

	public CustomerException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomerException(String message) {
		super(message);
	}

}
