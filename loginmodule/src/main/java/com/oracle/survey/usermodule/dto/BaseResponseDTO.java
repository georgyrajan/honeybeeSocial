package com.oracle.survey.usermodule.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseDTO<T> {
	private HttpStatus status;
	private String error;
	private T body;
}
