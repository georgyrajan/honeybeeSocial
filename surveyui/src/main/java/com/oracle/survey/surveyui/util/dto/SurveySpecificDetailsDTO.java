package com.oracle.survey.surveyui.util.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "name", "code", "version", "countDTO" })
public class SurveySpecificDetailsDTO {

	private String code;
	private Long version;
	private CountDTO countDTO;
}
