package com.oracle.survey.surveyadmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.survey.surveyadmin.entity.Survey;
/**
 *  Repository class which have all DB access methods
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
@Repository
public interface SurveyRepository extends JpaRepository<Survey, String> {

	List<Survey> findByCodeOrderByVersionDesc(String code);

	Survey findByCodeAndVersion(String code, Long version);

	List<Survey> findByCode(String code);

}
