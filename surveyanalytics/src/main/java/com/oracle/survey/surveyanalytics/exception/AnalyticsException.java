package com.oracle.survey.surveyanalytics.exception;

public class AnalyticsException extends RuntimeException {

	private static final long serialVersionUID = 3601944937043929705L;

	public AnalyticsException(String message, Throwable cause) {
		super(message, cause);
	}

	public AnalyticsException(String message) {
		super(message);
	}

}
