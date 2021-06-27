package com.oracle.survey.surveyadmin.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonPropertyOrder({ "name", "description", "code", "ready", "questions" })
public class SurveyDTO {
	@NotEmpty(message = "Survey name required")
	private String name;
	private String description;
	@NotEmpty(message = "Survey  code required")
	private String code;
	private Boolean ready;
	private List<QuestionDTO> questions = new ArrayList<>();

}
