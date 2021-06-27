package com.oracle.survey.usermodule.exception;

public class UserException extends RuntimeException {

	private static final long serialVersionUID = 3601944937043929705L;

	public UserException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserException(String message) {
		super(message);
	}

}
