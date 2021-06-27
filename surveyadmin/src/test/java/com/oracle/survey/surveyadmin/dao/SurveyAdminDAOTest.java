package com.oracle.survey.surveyadmin.dao;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oracle.survey.surveyadmin.controller.utils.TestUtils;
import com.oracle.survey.surveyadmin.dto.SurveyDTO;
import com.oracle.survey.surveyadmin.entity.Survey;
import com.oracle.survey.surveyadmin.exception.AdminException;
import com.oracle.survey.surveyadmin.repository.SurveyRepository;
import static org.mockito.ArgumentMatchers.any;
@RunWith(SpringJUnit4ClassRunner.class)
public class SurveyAdminDAOTest {

	@Mock
	SurveyRepository surveyRepository;

	@InjectMocks
	SurveyAdminDAO surveyAdminDAO;

	SurveyDTO surveyDTO;

	Survey survey;

	@Before
	public void init() {
		surveyDTO = TestUtils.createTestSurveyDTO();
		survey = TestUtils.createTestSurveyEntity();
	}

	@Test
	public void testCreateSurvey() {
		Mockito.when(surveyRepository.save(any())).thenReturn(TestUtils.createTestSurveyEntity());
		Survey response = surveyAdminDAO.saveOrUpdateSurvey(survey);
		TestUtils.assertCreateSurveyDTO(response, survey);
	}
	
	@Test(expected=AdminException.class)
	public void testCreateSurveyError() {
		Mockito.when(surveyRepository.save(any())).thenThrow(new AdminException(""));
		Survey response = surveyAdminDAO.saveOrUpdateSurvey(survey);
		TestUtils.assertCreateSurveyDTO(response, survey);
	}
	
	@Test
	public void testGetAllSurvey() {
		List<Survey> list = new ArrayList<>();
		list.add(TestUtils.createTestSurveyEntity());
		Mockito.when(surveyRepository.findAll()).thenReturn(list);
		List<Survey> responseList = surveyAdminDAO.getAllSurvey();
		assertNotNull(responseList.get(0).getCode());
	}
	
	@Test(expected=AdminException.class)
	public void testGetAllSurveyError() {
		Mockito.when(surveyRepository.findAll()).thenThrow(new AdminException(""));
		surveyAdminDAO.getAllSurvey();
	}
	
	@Test
	public void testFindByCodeAndOrderByVersionDesc() {
		List<Survey> list = new ArrayList<>();
		list.add(TestUtils.createTestSurveyEntity());
		Mockito.when(surveyRepository.findByCodeOrderByVersionDesc(any())).thenReturn(list);
		List<Survey> response  = surveyAdminDAO.findByCodeAndOrderByVersionDesc("code");
		assertNotNull(response);
	}
	
	@Test(expected=AdminException.class)
	public void testFindByCodeAndOrderByVersionDescError() {
		Mockito.when(surveyRepository.findByCodeOrderByVersionDesc(any())).thenThrow(new AdminException(""));
		 surveyAdminDAO.findByCodeAndOrderByVersionDesc("code");
	}
	
	
	@Test
	public void testGetByCodeByVersion() {
		Mockito.when(surveyRepository.findByCodeAndVersion(any(),any())).thenReturn(TestUtils.createTestSurveyEntity());
		Survey response  = surveyAdminDAO.getByCodeByVersion("code",1l);
		assertNotNull(response);
	}
	
	@Test(expected=AdminException.class)
	public void testGetByCodeByVersionError() {
		Mockito.when(surveyRepository.findByCodeAndVersion(any(),any())).thenThrow(new AdminException(""));
		 surveyAdminDAO.getByCodeByVersion("code",1l);
	}
	
	@Test
	public void testGetByCode() {
		List<Survey> list = new ArrayList<>();
		list.add(TestUtils.createTestSurveyEntity());
		Mockito.when(surveyRepository.findByCode(any())).thenReturn(list);
		List<Survey> response  = surveyAdminDAO.getByCode("code");
		assertNotNull(response);
	}
	
	@Test(expected=AdminException.class)
	public void testGetByCodeError() {
		Mockito.when(surveyRepository.findByCode(any())).thenThrow(new AdminException(""));
		 surveyAdminDAO.getByCode("code");
	}
	
}
