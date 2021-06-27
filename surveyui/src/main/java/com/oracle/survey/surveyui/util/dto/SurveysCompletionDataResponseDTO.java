package com.oracle.survey.surveyui.util.dto;

import java.util.List;

import lombok.Data;

@Data
public class SurveysCompletionDataResponseDTO {

	private Long totalSurveyCount;
	List<SurveySpecificDetailsDTO> listResult;

}
