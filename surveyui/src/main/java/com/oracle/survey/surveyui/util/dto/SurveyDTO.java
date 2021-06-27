package com.oracle.survey.surveyui.util.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({ "name", "description", "code", "ready", "questions" })
public class SurveyDTO {
	private String name;
	private String description;
	private String code;
	private Boolean ready;
	private List<QuestionDTO> questions = new ArrayList<>();

}
