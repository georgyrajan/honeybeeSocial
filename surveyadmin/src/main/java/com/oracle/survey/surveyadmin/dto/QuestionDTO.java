package com.oracle.survey.surveyadmin.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "name", "description", "code", "text", "required", "inputtype", "answers" })
public class QuestionDTO {
	@NotEmpty(message ="Question name required")
	private String name;
	private String description;
	@NotEmpty(message ="Question code required")
	private String code;
	@NotEmpty(message ="Question text required")
	private String text;
	private Boolean required=true;
	@NotEmpty(message ="Question Input type text required")
	private String inputtype;
	private List<AnswerDTO> answers;

}
