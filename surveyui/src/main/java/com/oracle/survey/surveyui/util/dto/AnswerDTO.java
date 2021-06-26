package com.oracle.survey.surveyui.util.dto;

import lombok.Data;

@Data
public class AnswerDTO {
	private String id;
	private String title;
	private String text;
	private String priority;
	private String description;
	private String answer;

}
