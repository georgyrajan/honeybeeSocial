package com.oracle.survey.surveyui.util.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
	private HttpStatus status;
	private Error error;
	private T body;
}
