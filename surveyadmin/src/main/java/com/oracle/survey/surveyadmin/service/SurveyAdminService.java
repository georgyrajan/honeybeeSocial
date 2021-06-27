package com.oracle.survey.surveyadmin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.oracle.survey.surveyadmin.config.Loggable;
import com.oracle.survey.surveyadmin.dao.SurveyAdminDAO;
import com.oracle.survey.surveyadmin.dto.AdminViewDTO;
import com.oracle.survey.surveyadmin.dto.SurveyDTO;
import com.oracle.survey.surveyadmin.entity.Survey;
import com.oracle.survey.surveyadmin.exception.AdminException;
import com.oracle.survey.surveyadmin.exception.AdminValidationException;
import com.oracle.survey.surveyadmin.util.SurveyAdminUtils;

/**
 * This is the Service class to handle admin related flow
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
@Service
public class SurveyAdminService {

	private static final Logger LOGGER = LogManager.getLogger(SurveyAdminService.class);

	@Autowired
	SurveyAdminDAO surveyAdminDAO;

	/**
	 * Method do validation and call dao method to create survey
	 * 
	 * @param surveyDTO
	 * @return
	 */
	@Loggable
	public SurveyDTO createSurvey(SurveyDTO surveyDTO) {
		SurveyDTO surveyCreated = null;
		try {
			validateSurveyInput(surveyDTO);
			checkIfSurveyAlreadyCreated(surveyDTO);
			Survey survey = surveyAdminDAO.saveOrUpdateSurvey(SurveyAdminUtils.createSurveyEntity(surveyDTO));
			surveyCreated = SurveyAdminUtils.createSurveyDTO(survey);
		} catch (AdminValidationException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error occured in create survey service method %s " , e.getMessage());
			throw new AdminException(e.getMessage());
		}
		return surveyCreated;
	}

	/**
	 * check if survey code already exist
	 * 
	 * @param surveyDTO
	 * @return
	 */
	@Loggable
	private void checkIfSurveyAlreadyCreated(SurveyDTO surveyDTO) {
		List<Survey> survey = surveyAdminDAO.getByCode(surveyDTO.getCode());
		if (!ObjectUtils.isEmpty(survey)) {
			LOGGER.debug("Survey code already exist");
			throw new AdminValidationException("Survey code already exist");
		}

	}

	/**
	 * basic validation for survey
	 * 
	 * @param surveyDTO
	 */
	@Loggable
	private void validateSurveyInput(SurveyDTO surveyDTO) {
		if (ObjectUtils.isEmpty(surveyDTO.getQuestions())) {
			LOGGER.debug("Survey should have atleast one question");
			throw new AdminValidationException("Survey should have atleast one question");
		}
		if (surveyDTO.getQuestions().stream().anyMatch(question -> ObjectUtils.isEmpty(question.getAnswers()))) {
			LOGGER.debug("Survey should not have question without answer");
			throw new AdminValidationException("Survey should not have question without answer");
		}

	}

	/**
	 * check if survey exist and then update to DB
	 * 
	 * @param surveyDTO
	 * @param version
	 * @return
	 */
	@Loggable
	public SurveyDTO updateSurvey(SurveyDTO surveyDTO, Long version) {
		SurveyDTO surveyUpdated = null;
		try {
			Survey survey = surveyAdminDAO.getByCodeByVersion(surveyDTO.getCode(), version);
			if (ObjectUtils.isEmpty(survey)) {
				LOGGER.debug("Survey not found with given code and version for update");
				throw new AdminValidationException("Survey not found with given code and version to update");
			}
			validateSurveyInput(surveyDTO);
			survey = surveyAdminDAO.saveOrUpdateSurvey(SurveyAdminUtils.updateSurveyEntity(surveyDTO, survey));
			surveyUpdated = SurveyAdminUtils.createSurveyDTO(survey);
		} catch (AdminValidationException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new AdminException(e.getMessage());
		}
		return surveyUpdated;
	}

	/**
	 * Get all survey from DB
	 * 
	 * @return
	 */
	@Loggable
	public List<SurveyDTO> getSurveysList() {
		List<SurveyDTO> surveyUpdated = null;
		try {
			List<Survey> surveyList = surveyAdminDAO.getAllSurvey();
			surveyUpdated = surveyList.stream().map(survey -> SurveyAdminUtils.createSurveyDTO(survey))
					.collect(Collectors.toList());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new AdminException(e.getMessage());
		}

		return surveyUpdated;
	}

	/**
	 * Method get a survey based on code and version
	 * 
	 * @param code
	 * @param version
	 * @return
	 */
	@Loggable
	public SurveyDTO getSurvey(String code, Long version) {
		SurveyDTO surveyDTO = null;
		try {
			Survey survey = surveyAdminDAO.getByCodeByVersion(code, version);
			if (ObjectUtils.isEmpty(survey)) {
				LOGGER.debug("Survey not found with given code and version");
				throw new AdminValidationException("Survey not found with given code and version");
			}
			surveyDTO = SurveyAdminUtils.createSurveyDTO(survey);
		} catch (AdminValidationException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new AdminException(e.getMessage());
		}

		return surveyDTO;
	}

	/**
	 * The method create next version of existing survey
	 * 
	 * @param surveyDTO
	 * @return
	 */
	@Loggable
	public SurveyDTO createNextVersion(SurveyDTO surveyDTO) {
		SurveyDTO surveyUpdated = null;
		try {
			List<Survey> surveyList = surveyAdminDAO.findByCodeAndOrderByVersionDesc(surveyDTO.getCode());

			if (ObjectUtils.isEmpty(surveyList)) {
				LOGGER.debug("Survey not found with given code %s", surveyDTO.getCode());
				throw new AdminValidationException("Survey not found with given code");
			}
			Survey survey = surveyList.get(0);
			validateSurveyInput(surveyDTO);
			survey = surveyAdminDAO.saveOrUpdateSurvey(SurveyAdminUtils.createNewVersionSurveyEntity(surveyDTO, survey));
			surveyUpdated = SurveyAdminUtils.createSurveyDTO(survey);
		} catch (AdminValidationException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new AdminException(e.getMessage());
		}
		return surveyUpdated;
	}

	@Loggable
	public List<AdminViewDTO> getSurveyURLList() {
		List<AdminViewDTO> adminViewList = new ArrayList<>();
		try {
			List<Survey> surveyList = surveyAdminDAO.getAllSurvey();
			if (!ObjectUtils.isEmpty(surveyList)) {
				for (Survey survey : surveyList) {
					AdminViewDTO adminViewDTO = new AdminViewDTO();
					adminViewDTO.setName(survey.getName());
					adminViewDTO.setDescription(survey.getDescription());
					adminViewDTO.setUrl(survey.getUrl());
					adminViewDTO.setCode(survey.getCode());
					adminViewList.add(adminViewDTO);
				}
			}
		} catch (AdminValidationException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new AdminException(e.getMessage());
		}

		return adminViewList;
	}

}
