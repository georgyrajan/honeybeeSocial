package com.oracle.survey.surveycustomer.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.survey.surveycustomer.config.Loggable;
import com.oracle.survey.surveycustomer.constant.AppConstants;
import com.oracle.survey.surveycustomer.dto.BaseResponseDTO;
import com.oracle.survey.surveycustomer.dto.CustomerAnswerDTO;
import com.oracle.survey.surveycustomer.service.SurveyCustomerAnswerService;
import com.oracle.survey.surveycustomer.util.SurveyCustomerAnswerUtils;

@RestController
@RequestMapping("/customer/answer")
public class SurveyCustomerAnswerController {

	private static final Logger LOGGER = LogManager.getLogger(SurveyCustomerAnswerController.class);

	@Autowired
	SurveyCustomerAnswerService surveyCustomerService;

	/**
	 * This method is used to create new user answer for a survey
	 * 
	 * @param customerAnswerDTO
	 * @param jwt
	 * @param requestId
	 * @return
	 */
	@Loggable
	@PostMapping("/create")
	public BaseResponseDTO<CustomerAnswerDTO> createCustomerAnswer(@RequestBody CustomerAnswerDTO customerAnswerDTO,
			@RequestHeader(name = "Auth-JWT", required = false) String jwt,
			@RequestHeader(name = "requestId") String requestId) {
		LOGGER.debug("Create Survey: request recieved in controller with code : %s " , customerAnswerDTO.getSurveyCode());
		if (ObjectUtils.isEmpty(jwt)) {
			customerAnswerDTO.setUserId(AppConstants.ANONYMOUS_USER);
		}
		CustomerAnswerDTO surveyCreated = surveyCustomerService.createCustomerAnswer(customerAnswerDTO);
		return SurveyCustomerAnswerUtils.wrapResponse(surveyCreated, HttpStatus.OK);
	}

	/**
	 * This method is used to update customer answer which is already created
	 * 
	 * @param customerAnswerDTO
	 * @param jwt
	 * @return
	 */
	@Loggable
	@PutMapping("/update")
	public BaseResponseDTO<CustomerAnswerDTO> updateCustomerAnswer(@RequestBody CustomerAnswerDTO customerAnswerDTO,
			@RequestHeader(name = "Auth-JWT") String jwt) {
		LOGGER.debug("Update Survey: request recieved in controller with code : %s" , customerAnswerDTO.getSurveyCode());
		CustomerAnswerDTO surveyUpdated = surveyCustomerService.updateCustomerAnswer(customerAnswerDTO);
		return SurveyCustomerAnswerUtils.wrapResponse(surveyUpdated, HttpStatus.OK);
	}


	/**
	 * This method is used to get all survey answer of a user
	 * 
	 * @param userId
	 * @param code
	 * @param version
	 * @param jwt
	 * @return
	 */
	@Loggable
	@GetMapping("/survey")
	public BaseResponseDTO<CustomerAnswerDTO> getCustomerAnswer(@RequestParam(required = true) String userId,
			@RequestParam(required = true) String code, @RequestParam(required = true) Long version,
			@RequestHeader(name = "Auth-JWT") String jwt) {
		LOGGER.debug("Get Survey: request recieved in controller with id %s code %s version %d ", userId, code,
				version);
		CustomerAnswerDTO survey = surveyCustomerService.getCustomerAnswer(userId, code, version, jwt);
		return SurveyCustomerAnswerUtils.wrapResponse(survey, HttpStatus.OK);
	}
	
	
	/**
	 * This method is used to get all survey answer of a user
	 * 
	 * @param userId
	 * @param code
	 * @param version
	 * @param jwt
	 * @return
	 */
	@Loggable
	@GetMapping("/list")
	public BaseResponseDTO<List<CustomerAnswerDTO>> getCustomerAnswerList(@RequestParam(required = true) String userId,
			@RequestHeader(name = "Auth-JWT") String jwt) {
		LOGGER.debug("Get Survey: request recieved in controller with id %s ", userId);
		List<CustomerAnswerDTO> surveyList = surveyCustomerService.getCustomerAnswerList(userId);
		return SurveyCustomerAnswerUtils.wrapResponse(surveyList, HttpStatus.OK);
	}

}
