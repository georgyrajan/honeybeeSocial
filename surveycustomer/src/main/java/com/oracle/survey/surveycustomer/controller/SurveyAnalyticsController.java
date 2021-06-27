package com.oracle.survey.surveycustomer.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.survey.surveycustomer.config.Loggable;
import com.oracle.survey.surveycustomer.dto.BaseResponseDTO;
import com.oracle.survey.surveycustomer.dto.SurveySpecificDetailsDTO;
import com.oracle.survey.surveycustomer.dto.SurveysCompletionDataResponseDTO;
import com.oracle.survey.surveycustomer.service.SurveyCustomerAnswerService;
import com.oracle.survey.surveycustomer.util.SurveyCustomerAnswerUtils;
/**
*
* @author Georgy Rajan
* @version 1.0
* @since 2021-06-22
*/
@RestController
@RequestMapping("/analytics")
public class SurveyAnalyticsController {

	private static final Logger LOGGER = LogManager.getLogger(SurveyAnalyticsController.class);

	@Autowired
	SurveyCustomerAnswerService surveyCustomerService;

	/**
	 * This method is used to get all the survey details for a given surveyCode and Version
	 * @param surveyCode
	 * @param surveyVersion
	 * @param jwt
	 * @param requestId
	 * @return
	 */
	@Loggable
	@GetMapping("/detail")
	public BaseResponseDTO<List<SurveySpecificDetailsDTO>> getSurveyDetails(@RequestParam String surveyCode,
			@RequestParam Long surveyVersion, @RequestHeader(name = "Auth-JWT", required = false) String jwt,
			@RequestHeader(name = "requestId", required = false) String requestId) {
		LOGGER.debug("getSurveyDetails: get survey call for survey %s version %s",surveyCode,surveyVersion);
		List<SurveySpecificDetailsDTO> surveyDetails = surveyCustomerService.getSurveyDetails(surveyCode, surveyVersion);
		return SurveyCustomerAnswerUtils.wrapResponse(surveyDetails, HttpStatus.OK);
	}

	/**
	 * This method is used to get static count of each survey completion status
	 * @param jwt
	 * @param requestId
	 * @return
	 */
	@Loggable
	@GetMapping("/all")
	public BaseResponseDTO<SurveysCompletionDataResponseDTO> getAllData(@RequestHeader(name = "Auth-JWT", required = false) String jwt,
			@RequestHeader(name = "requestId", required = false) String requestId) {
		LOGGER.debug("getSurveyDetails: get survey call for all surveys");
		SurveysCompletionDataResponseDTO allSurveyDetails = surveyCustomerService.getAllData();
		return SurveyCustomerAnswerUtils.wrapResponse(allSurveyDetails, HttpStatus.OK);
	}

}
