package com.oracle.survey.surveyadmin.exception;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.oracle.survey.surveyadmin.dto.BaseResponseDTO;
import com.oracle.survey.surveyadmin.util.SurveyAdminUtils;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<BaseResponseDTO<Object>> handleGenericException(Exception ex, WebRequest request) {
		LOGGER.error("Generic error occured with message %s", ex.getMessage());
		return new ResponseEntity<>(
				SurveyAdminUtils.wrapErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
				HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(AdminValidationException.class)
	protected ResponseEntity<BaseResponseDTO<Object>> handleAdminValidationException(AdminValidationException ex,
			WebRequest request) {
		LOGGER.error("AdminValidationException error occured with message %s", ex.getMessage());
		return new ResponseEntity<>(
				SurveyAdminUtils.wrapErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST),
				HttpStatus.BAD_REQUEST);

	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<BaseResponseDTO<Object>> handleConstraintViolation(ConstraintViolationException ex) {
		List<String> errors = new ArrayList<>();

		ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

		LOGGER.error("ConstraintViolationException error occured with message %s", ex.getMessage());
		return new ResponseEntity<>(
				SurveyAdminUtils.wrapErrorResponse( errors.toString(), HttpStatus.BAD_REQUEST),
				HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<BaseResponseDTO<Object>> handleConstraintViolation(MethodArgumentNotValidException ex) {
		List<String> errors = new ArrayList<>();
		ex.getAllErrors().forEach(cv -> errors.add(cv.getDefaultMessage()));

		LOGGER.error("ConstraintViolationException error occured with message %s", ex.getMessage());
		return new ResponseEntity<>(
				SurveyAdminUtils.wrapErrorResponse(errors.toString(), HttpStatus.BAD_REQUEST),
				HttpStatus.BAD_REQUEST);
	}

	public static <T> BaseResponseDTO<T> wrapErrorResponse(T body, String message, HttpStatus badRequest) {
		BaseResponseDTO<T> baseResponse = new BaseResponseDTO<>();
		baseResponse.setStatus(badRequest);
		baseResponse.setBody(body);
		baseResponse.setError(message);
		return baseResponse;
	}
}
