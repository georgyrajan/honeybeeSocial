package com.oracle.survey.surveycustomer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oracle.survey.surveycustomer.entity.CustomerAnswer;

@Repository
public interface CustomerAnswerRepository extends JpaRepository<CustomerAnswer, String> {

	CustomerAnswer findBySurveyCodeAndSurveyVersionAndUserId(String surveyCode, Long surveyVersion, String userId);

	@Query("SELECT c.userId,c.surveyCode,c.surveyVersion,c.isComplete FROM CustomerAnswer c WHERE c.surveyCode = ?1 and c.surveyVersion = ?2")
	List<Object[]> findBySurveyCodeAndSurveyVersion(String surveyCode, Long surveyVersion);

	@Query("SELECT c.userId,c.surveyCode,c.surveyVersion,c.isComplete FROM CustomerAnswer c")
	List<Object[]> findAllSurvey();

	List<CustomerAnswer> findByUserId(String userId);
}
