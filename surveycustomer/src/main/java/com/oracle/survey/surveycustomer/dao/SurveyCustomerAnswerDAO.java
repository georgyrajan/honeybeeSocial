package com.oracle.survey.surveycustomer.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oracle.survey.surveycustomer.config.Loggable;
import com.oracle.survey.surveycustomer.entity.CustomerAnswer;
import com.oracle.survey.surveycustomer.repository.CustomerAnswerRepository;

@Service
public class SurveyCustomerAnswerDAO {

	@Autowired
	CustomerAnswerRepository customerAnswerRepository;

	@Loggable
	public CustomerAnswer createCustomerAnswer(CustomerAnswer createCustomerAnswerEntity) {
		return customerAnswerRepository.save(createCustomerAnswerEntity);
	}

	@Loggable
	public CustomerAnswer findBySurveyCodeAndSurveyVersionAndUserId(String surveyCode, Long surveyVersion,
			String userId) {
		return customerAnswerRepository.findBySurveyCodeAndSurveyVersionAndUserId(surveyCode, surveyVersion, userId);

	}

	@Loggable
	public void delete(CustomerAnswer customerAnswerInDB) {
		customerAnswerRepository.delete(customerAnswerInDB);
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
	public List<Object[]> getSurveyDetails(String surveyCode, Long surveyVersion) {
		return customerAnswerRepository.findBySurveyCodeAndSurveyVersion(surveyCode, surveyVersion);

	}

	/**
	 * This method is used to get static count of each survey completion status
	 * 
	 * @return
	 */
	@Loggable
	public List<Object[]> getAllData() {
		return customerAnswerRepository.findAllSurvey();
	}

	/**
	 * @param userId
	 * @return
	 */
	public List<CustomerAnswer> getCustomerAnswerList(String userId) {
		return customerAnswerRepository.findByUserId(userId);
	}

}
