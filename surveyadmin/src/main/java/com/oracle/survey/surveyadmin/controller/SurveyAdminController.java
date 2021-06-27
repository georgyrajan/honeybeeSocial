package com.oracle.survey.surveyadmin.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.survey.surveyadmin.config.Loggable;
import com.oracle.survey.surveyadmin.dto.BaseResponseDTO;
import com.oracle.survey.surveyadmin.dto.SurveyDTO;
import com.oracle.survey.surveyadmin.service.SurveyAdminService;
import com.oracle.survey.surveyadmin.util.SurveyAdminUtils;

/**
 * This is the controller method to handle admin related flow
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
@RestController
@RequestMapping("/admin/")
public class SurveyAdminController {

	private static final Logger LOGGER = LogManager.getLogger(SurveyAdminController.class);

	@Autowired
	SurveyAdminService surveyAdminService;

	/**
	 * This method is used to create new survey for users
	 * 
	 * @param SurveyDTO
	 * @param jwt
	 * @param requestId
	 * @return
	 */
	@Loggable
	@PostMapping("/create")
	public BaseResponseDTO<SurveyDTO> createSurvey(@Valid @RequestBody SurveyDTO surveyDTO,
			@RequestHeader(name = "Auth-JWT") String jwt,
			@RequestHeader(name = "requestId", required = false) String requestId) {
		LOGGER.debug("Create Survey: request recieved in controller with code : %s " , surveyDTO.getCode());
		SurveyDTO surveyCreated = surveyAdminService.createSurvey(surveyDTO);
		return SurveyAdminUtils.wrapResponse(surveyCreated, HttpStatus.OK);
	}

	/**
	 * This method is used to update already present surveys
	 * 
	 * @param surveyDTO
	 * @param version
	 * @param jwt
	 * @return
	 */
	@Loggable
	@PutMapping("/update")
	public BaseResponseDTO<SurveyDTO> updateSurvey(@Valid @RequestBody SurveyDTO surveyDTO, @RequestParam Long version,
			@RequestHeader(name = "Auth-JWT") String jwt) {
		LOGGER.debug("Update Survey: request recieved in controller with code : %s " , surveyDTO.getCode());
		SurveyDTO surveyUpdated = surveyAdminService.updateSurvey(surveyDTO, version);
		return SurveyAdminUtils.wrapResponse(surveyUpdated, HttpStatus.OK);
	}

	/**
	 * This method is used to create new version of existing survey
	 * 
	 * @param Survey
	 * @return
	 */
	@Loggable
	@PutMapping("/createnewversion")
	public BaseResponseDTO<SurveyDTO> createNextVersion(@Valid @RequestBody SurveyDTO surveyDTO,
			@RequestHeader(name = "Auth-JWT") String jwt) {
		LOGGER.debug("Create new version Survey: request recieved in controller with code : %s " , surveyDTO.getCode());
		SurveyDTO survey = surveyAdminService.createNextVersion(surveyDTO);
		return SurveyAdminUtils.wrapResponse(survey, HttpStatus.OK);
	}

	/**
	 * This method is used to list all surveys
	 * 
	 * @return
	 */
	@Loggable
	@GetMapping("/listallsurvey")
	public BaseResponseDTO<List<SurveyDTO>> getAllSurvey(@RequestHeader(name = "Auth-JWT") String jwt) {
		List<SurveyDTO> surveyList = surveyAdminService.getSurveysList();
		return SurveyAdminUtils.wrapResponse(surveyList, HttpStatus.OK);
	}

}
