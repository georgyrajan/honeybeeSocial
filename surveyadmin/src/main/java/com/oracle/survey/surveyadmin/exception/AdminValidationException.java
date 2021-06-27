package com.oracle.survey.surveyadmin.exception;

public class AdminValidationException extends RuntimeException {

	private static final long serialVersionUID = 3601944937043929705L;

	public AdminValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdminValidationException(String message) {
		super(message);
	}

}
