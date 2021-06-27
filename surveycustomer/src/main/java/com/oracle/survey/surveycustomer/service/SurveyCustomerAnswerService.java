package com.oracle.survey.surveycustomer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.oracle.survey.surveycustomer.config.Loggable;
import com.oracle.survey.surveycustomer.constant.AppConstants;
import com.oracle.survey.surveycustomer.dao.SurveyCustomerAnswerDAO;
import com.oracle.survey.surveycustomer.dto.BaseResponseDTO;
import com.oracle.survey.surveycustomer.dto.CustomerAnswerDTO;
import com.oracle.survey.surveycustomer.dto.SurveyDTO;
import com.oracle.survey.surveycustomer.dto.SurveySpecificDetailsDTO;
import com.oracle.survey.surveycustomer.dto.SurveysCompletionDataResponseDTO;
import com.oracle.survey.surveycustomer.entity.CustomerAnswer;
import com.oracle.survey.surveycustomer.exception.CustomerException;
import com.oracle.survey.surveycustomer.util.SurveyCustomerAnswerUtils;

@Service
public class SurveyCustomerAnswerService {

	private static final Logger LOGGER = LogManager.getLogger(SurveyCustomerAnswerService.class);

	@Value("${app.admin.survey}")
	String url;

	@Autowired
	SurveyCustomerAnswerDAO surveyCustomerAnswerDAO;

	@Autowired
	SurveyCustomerAnswerUtils surveyCustomerAnswerUtils;

	/**
	 * This method is service layer which handle the business logic for create
	 * 
	 * @param customerAnswerDTO
	 * @return
	 */
	@Loggable
	public CustomerAnswerDTO createCustomerAnswer(CustomerAnswerDTO customerAnswerDTO) {
		CustomerAnswerDTO customerAnswerCreated = null;
		try {
			LOGGER.debug("User Answer before save");
			CustomerAnswer customerAnswerInDB = null;
			if (!AppConstants.ANONYMOUS_USER.equals(customerAnswerDTO.getUserId())) {
				customerAnswerInDB = surveyCustomerAnswerDAO.findBySurveyCodeAndSurveyVersionAndUserId(
						customerAnswerDTO.getSurveyCode(), customerAnswerDTO.getSurveyVersion(),
						customerAnswerDTO.getUserId());
			}

			if (!ObjectUtils.isEmpty(customerAnswerInDB)) {
				LOGGER.debug("Customer Answer for this user is already created please update");
				throw new CustomerException("Customer Answer for this user is already created please update");
			}
			CustomerAnswer customerAnswer = surveyCustomerAnswerDAO
					.createCustomerAnswer(SurveyCustomerAnswerUtils.createCustomerAnswerEntity(customerAnswerDTO));
			customerAnswerCreated = SurveyCustomerAnswerUtils.createCustomerAnswerDTO(customerAnswer);
			LOGGER.debug("User Answer saved sucessfully");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			if (e instanceof CustomerException) {
				throw e;
			}
			throw new CustomerException("Error while creating user answer");
		}
		return customerAnswerCreated;
	}

	/**
	 * @param customerAnswerDTO
	 * @return
	 */
	@Loggable
	public CustomerAnswerDTO updateCustomerAnswer(CustomerAnswerDTO customerAnswerDTO) {
		CustomerAnswerDTO customerAnswerUpdated = null;
		LOGGER.debug("User Answer before update");
		try {
			CustomerAnswer customerAnswerInDB = surveyCustomerAnswerDAO.findBySurveyCodeAndSurveyVersionAndUserId(
					customerAnswerDTO.getSurveyCode(), customerAnswerDTO.getSurveyVersion(),
					customerAnswerDTO.getUserId());
			if (ObjectUtils.isEmpty(customerAnswerInDB)) {
				LOGGER.debug("Customer Answer had no entry so creating new record");
				customerAnswerUpdated = createCustomerAnswer(customerAnswerDTO);
			} else {
				LOGGER.debug("Customer Answer had  entry so updating record");
				CustomerAnswer customerAnswer = surveyCustomerAnswerDAO
						.createCustomerAnswer(SurveyCustomerAnswerUtils.createCustomerAnswerEntity(customerAnswerDTO));
				surveyCustomerAnswerDAO.delete(customerAnswerInDB);
				customerAnswerUpdated = SurveyCustomerAnswerUtils.createCustomerAnswerDTO(customerAnswer);
			}

		} catch (Exception e) {
			LOGGER.error("Customer Answer updating record had issue :" + e.getMessage());
			throw new CustomerException("Error while saving your answer please try later");
		}
		return customerAnswerUpdated;
	}

	/**
	 * @param userId
	 * @param code
	 * @param version
	 * @param jwt
	 * @return
	 */
	@Loggable
	public CustomerAnswerDTO getCustomerAnswer(String userId, String code, Long version, String jwt) {
		CustomerAnswerDTO customerAnswerDTO = null;
		LOGGER.debug("User Answer before getCustomerAnswer");
		try {
			CustomerAnswer customerAnswer = surveyCustomerAnswerDAO.findBySurveyCodeAndSurveyVersionAndUserId(code,
					version, userId);
			if (ObjectUtils.isEmpty(customerAnswer)) {
				BaseResponseDTO<SurveyDTO> survey = surveyCustomerAnswerUtils.callAdminModuleAndGetSurvey(code, version,
						jwt, url);
				customerAnswerDTO = setCustomerAnswerDefaultValues(userId, code, version, survey);
			} else {
				customerAnswerDTO = SurveyCustomerAnswerUtils.createCustomerAnswerDTO(customerAnswer);
			}

		} catch (Exception e) {
			throw new CustomerException(e.getMessage());
		}

		return customerAnswerDTO;
	}

	private CustomerAnswerDTO setCustomerAnswerDefaultValues(String userId, String code, Long version,
			BaseResponseDTO<SurveyDTO> survey) {
		CustomerAnswerDTO customerAnswerDTO;
		customerAnswerDTO = new CustomerAnswerDTO();
		customerAnswerDTO.setSurveyDetails(survey.getBody());
		customerAnswerDTO.setSurveyCode(code);
		customerAnswerDTO.setSurveyVersion(version);
		customerAnswerDTO.setUserId(userId);
		customerAnswerDTO.setIsComplete(false);
		customerAnswerDTO.setIsFirstTime(true);
		return customerAnswerDTO;
	}

	/**
	 * This method is used to get all the survey details for a given surveyCode
	 * and Version
	 * 
	 * @param surveyCode
	 * @param surveyVersion
	 * @return
	 */
	@Loggable
	public List<SurveySpecificDetailsDTO> getSurveyDetails(String surveyCode, Long surveyVersion) {
		LOGGER.debug("before get survey details");
		List<Object[]> customerAnswer = surveyCustomerAnswerDAO.getSurveyDetails(surveyCode, surveyVersion);
		List<SurveySpecificDetailsDTO> list = SurveyCustomerAnswerUtils.convertToCustomerAnswerDTOList(customerAnswer);

		return list;
	}

	/**
	 * This method is used to get static count of each survey completion status
	 * 
	 * @return
	 */
	@Loggable
	public SurveysCompletionDataResponseDTO getAllData() {
		LOGGER.debug("before get all survey details");
		List<Object[]> customerAnswerList = surveyCustomerAnswerDAO.getAllData();
		SurveysCompletionDataResponseDTO surveysCompletionDataResponseDTO = SurveyCustomerAnswerUtils
				.convertToSurveysCompletionDataResponseDTO(customerAnswerList);
		return surveysCompletionDataResponseDTO;
	}

	/**
	 * Service method to get all the user answers
	 * @param userId
	 * @return
	 */
	public List<CustomerAnswerDTO> getCustomerAnswerList(String userId) {
		LOGGER.debug("before get all survey details");
		List<CustomerAnswerDTO> response = new ArrayList<CustomerAnswerDTO>();
		List<CustomerAnswer> customerAnswerList = surveyCustomerAnswerDAO.getCustomerAnswerList(userId);
		if (!ObjectUtils.isEmpty(customerAnswerList)) {
			response = customerAnswerList.stream()
					.map(customerAnswer -> SurveyCustomerAnswerUtils.createCustomerAnswerDTO(customerAnswer))
					.collect(Collectors.toList());
		}
		return response;
	}

}
