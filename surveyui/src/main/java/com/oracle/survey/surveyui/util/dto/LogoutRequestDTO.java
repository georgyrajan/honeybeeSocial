package com.oracle.survey.surveyui.util.dto;

import lombok.Data;

@Data
public class LogoutRequestDTO {
	private String userId;
	private String jwt;
}
