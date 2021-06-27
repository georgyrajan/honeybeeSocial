package com.oracle.survey.surveyanalytics.dto;

import java.util.List;

import lombok.Data;

@Data
public class SurveysCompletionDataResponseDTO {

	private Long totalSurveyCount;
	List<SurveySpecificDetailsDTO> listResult;

}

