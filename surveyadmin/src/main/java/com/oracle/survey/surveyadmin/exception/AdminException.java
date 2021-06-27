package com.oracle.survey.surveyadmin.exception;

public class AdminException extends RuntimeException {

	private static final long serialVersionUID = 3601944937043929705L;

	public AdminException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdminException(String message) {
		super(message);
	}

}
