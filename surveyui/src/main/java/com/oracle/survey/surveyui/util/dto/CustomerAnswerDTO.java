package com.oracle.survey.surveyui.util.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
@Data
@JsonPropertyOrder({ "userId", "surveyCode", "surveyVersion", "isComplete", "isFirstTime","surveyDetails" })
public class CustomerAnswerDTO {

	private String userId;
	private String surveyCode;
	private Long surveyVersion;
	private SurveyDTO surveyDetails;
	private Boolean isComplete;
	private Boolean isFirstTime;

}
