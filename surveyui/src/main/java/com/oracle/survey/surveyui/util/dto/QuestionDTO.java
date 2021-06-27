package com.oracle.survey.surveyui.util.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "name", "description", "code", "text", "required", "inputtype", "answers" })
public class QuestionDTO {
	private String name;
	private String description;
	private String code;
	private String text;
	private Boolean required;
	private String inputtype;
	private List<AnswerDTO> answers;

}
