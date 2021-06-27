package com.oracle.survey.surveyanalytics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.survey.surveyanalytics.config.Loggable;
import com.oracle.survey.surveyanalytics.dto.BaseResponseDTO;
import com.oracle.survey.surveyanalytics.dto.SurveysCompletionDataResponseDTO;
import com.oracle.survey.surveyanalytics.service.SurveyAnalyticsService;
/**
*
* @author Georgy Rajan
* @version 1.0
* @since 2021-06-22
*/
@RestController
public class SurveyAnalyticsController {
	
	@Autowired
	SurveyAnalyticsService surveyAnalyticsService;
	
	/**
	 * This method is used to get the current completion status of all survey available
	 * @param jwt
	 * @param requestId
	 * @return
	 */
	@Loggable
	@GetMapping("/report")
	public BaseResponseDTO<SurveysCompletionDataResponseDTO> getAllDataReport(@RequestHeader(name = "Auth-JWT", required = false) String jwt,
			@RequestHeader(name = "requestId", required = false) String requestId) {
		return surveyAnalyticsService.getAllDataReport(jwt);
	}

}
