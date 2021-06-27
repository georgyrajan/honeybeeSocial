package com.oracle.survey.surveyadmin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.survey.surveyadmin.config.Loggable;
import com.oracle.survey.surveyadmin.dto.AdminViewDTO;
import com.oracle.survey.surveyadmin.dto.BaseResponseDTO;
import com.oracle.survey.surveyadmin.dto.SurveyDTO;
import com.oracle.survey.surveyadmin.service.SurveyAdminService;
import com.oracle.survey.surveyadmin.util.SurveyAdminUtils;
/**
* This is the controller method to handle survey related flow 
*
* @author  Georgy Rajan
* @version 1.0
* @since   2021-06-22 
*/
@RestController
@RequestMapping("/survey/")
public class SurveyController {


	@Autowired
	SurveyAdminService surveyAdminService;

	
	/**
	 * This method is used to get survey with given code and version
	 * @param code
	 * @param version
	 * @param jwt
	 * @return
	 */
	@Loggable
	@GetMapping("/details")
	public BaseResponseDTO<SurveyDTO> getSurvey(@RequestParam(required = true) String code, @RequestParam Long version,
			@RequestHeader(name = "Auth-JWT",required=false) String jwt) {
		SurveyDTO survey = surveyAdminService.getSurvey(code, version);
		return SurveyAdminUtils.wrapResponse(survey, HttpStatus.OK);
	}

	/**
	 * This method is used to get the url of survey which can be shared to the customer by admin
	 * @param code
	 * @param version
	 * @param jwt
	 * @return
	 */
	@Loggable
	@GetMapping("/urlmap")
	public BaseResponseDTO<List<AdminViewDTO>> getSurveyURL(
			@RequestHeader(name = "Auth-JWT") String jwt) {
		List<AdminViewDTO> adminViewList = surveyAdminService.getSurveyURLList();
		return SurveyAdminUtils.wrapResponse(adminViewList, HttpStatus.OK);
	}

}
