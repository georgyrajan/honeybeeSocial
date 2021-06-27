package com.oracle.survey.surveycustomer.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class AnswerDTO {
	private String id;
	@NotEmpty(message ="Answer title is required")
	private String title;
	@NotEmpty(message ="Answer text is required")
	private String text;
	@NotEmpty(message ="Answer priority is required")
	private String priority;
	@NotEmpty(message ="Answer descriptiont is required")
	private String description;
	private String answer;

}
