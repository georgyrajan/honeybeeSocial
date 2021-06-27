package com.oracle.survey.surveyadmin.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oracle.survey.surveyadmin.config.Loggable;
import com.oracle.survey.surveyadmin.entity.Survey;
import com.oracle.survey.surveyadmin.exception.AdminException;
import com.oracle.survey.surveyadmin.repository.SurveyRepository;

/**
 * This is the Dao class to handle DB access for all Survey admin flow
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
@Service
public class SurveyAdminDAO {

	@Autowired
	SurveyRepository surveyRepository;

	private static final Logger LOGGER = LogManager.getLogger(SurveyAdminDAO.class);

	/**
	 * @param survey
	 * @return
	 */
	@Loggable
	public Survey saveOrUpdateSurvey(Survey survey) {
		try {
			LOGGER.debug("Before create of survey with code %s " , survey.getCode());
			survey = surveyRepository.save(survey);
			LOGGER.debug("Survey created with id %s " ,survey.getId());
		} catch (Exception e) {
			LOGGER.error("Error in DB operation saveOrUpdateSurvey %s" , e.getMessage());
			throw new AdminException("Error in DB operation ", e);
		}
		return survey;

	}

	/**
	 * @return
	 */
	@Loggable
	public List<Survey> getAllSurvey() {

		List<Survey> surveyList = null;
		try {
			surveyList = surveyRepository.findAll();
			LOGGER.debug("Survey list found with size %d",surveyList.size());
		} catch (Exception e) {
			LOGGER.error("Error in DB operation getAllSurvey %s " , e.getMessage());
			throw new AdminException("Error in DB operation getAllSurvey");
			
		}
		return surveyList;

	}

	/**
	 * @param code
	 * @param version
	 * @return
	 */
	@Loggable
	public Survey getByCodeByVersion(String code, Long version) {

		Survey survey = null;
		try {
			LOGGER.debug("Survey find cal with code %s and version %d ", code, version);
			survey = surveyRepository.findByCodeAndVersion(code, version);
		} catch (Exception e) {
			LOGGER.error("Error in DB operation getByCodeByVersion %s " , e.getMessage());
			throw new AdminException("Error in DB operation getByCodeByVersion");
		}
		return survey;

	}

	/**
	 * @param code
	 * @return
	 */
	@Loggable
	public List<Survey> getByCode(String code) {
		List<Survey> surveyList = null;
		try {
			LOGGER.debug("Survey find cal with code %s ", code);
			surveyList = surveyRepository.findByCode(code);
		} catch (Exception e) {
			LOGGER.error("Error in DB operation getByCode %s" , e.getMessage());
			throw new AdminException("Error in DB operation", e);
		}
		return surveyList;
	}

	/**
	 * @param code
	 * @return
	 */
	@Loggable
	public List<Survey> findByCodeAndOrderByVersionDesc(String code) {
		List<Survey> surveyList = null;
		try {
			LOGGER.debug("Survey find cal with code %s ", code);
			surveyList = surveyRepository.findByCodeOrderByVersionDesc(code);
		} catch (Exception e) {
			LOGGER.error("Error in DB operation findByCodeAndOrderByVersionDesc %s ", e.getMessage());
			throw new AdminException("Error in DB operation", e);
		}
		return surveyList;
	}

}
