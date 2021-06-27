package com.oracle.survey.surveycustomer.dto;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "userId", "surveyCode", "surveyVersion", "isComplete", "isFirstTime", "surveyDetails" })
public class CustomerAnswerDTO {
	@NotEmpty(message = "User id required")
	private String userId;
	@NotEmpty(message = "Survey code required")
	private String surveyCode;
	@NotEmpty(message = "Survey version required")
	private Long surveyVersion;
	@NotEmpty(message = "Survey details required")
	private SurveyDTO surveyDetails;
	private Boolean isComplete;
	private Boolean isFirstTime;

}
