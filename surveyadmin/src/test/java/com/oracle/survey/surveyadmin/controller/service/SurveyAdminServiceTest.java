package com.oracle.survey.surveyadmin.controller.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
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
import com.oracle.survey.surveyadmin.dao.SurveyAdminDAO;
import com.oracle.survey.surveyadmin.dto.AdminViewDTO;
import com.oracle.survey.surveyadmin.dto.SurveyDTO;
import com.oracle.survey.surveyadmin.entity.Survey;
import com.oracle.survey.surveyadmin.exception.AdminException;
import com.oracle.survey.surveyadmin.exception.AdminValidationException;
import com.oracle.survey.surveyadmin.service.SurveyAdminService;

@RunWith(SpringJUnit4ClassRunner.class)
public class SurveyAdminServiceTest {

	@Mock
	SurveyAdminDAO surveyAdminDAO;

	@InjectMocks
	SurveyAdminService surveyAdminService;

	SurveyDTO surveyDTO;

	@Before
	public void init() {
		surveyDTO = TestUtils.createTestSurveyDTO();
	}

	@Test
	public void testCreateSurvey() {
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenReturn(TestUtils.createTestSurveyEntity());
		SurveyDTO response = surveyAdminService.createSurvey(surveyDTO);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}

	@Test(expected = AdminValidationException.class)
	public void testCreateSurveyEmptyQuestion() {
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenReturn(TestUtils.createTestSurveyEntity());
		surveyDTO.setQuestions(null);
		surveyAdminService.createSurvey(surveyDTO);
	}

	@Test(expected = AdminValidationException.class)
	public void testCreateSurveyEmptyQuestion_answer() {
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenReturn(TestUtils.createTestSurveyEntity());
		surveyDTO.getQuestions().get(0).setAnswers(null);
		surveyAdminService.createSurvey(surveyDTO);
	}

	@Test(expected = AdminValidationException.class)
	public void testCreateSurveyDuplicate() {
		List<Survey> list = new ArrayList<>();
		list.add(TestUtils.createTestSurveyEntity());
		Mockito.when(surveyAdminDAO.getByCode(surveyDTO.getCode())).thenReturn(list);
		surveyAdminService.createSurvey(surveyDTO);
	}

	@Test(expected = AdminValidationException.class)
	public void testCreateSurveyAdminExceptionSave() {
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenThrow(new AdminException(""));
		surveyDTO.getQuestions().get(0).setAnswers(null);
		surveyAdminService.createSurvey(surveyDTO);
	}

	@Test(expected = AdminException.class)
	public void testCreateSurveyExceptionSave() {
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenThrow(new RuntimeException(""));
		surveyAdminService.createSurvey(surveyDTO);
	}
	
	@Test
	public void testUpdateSurvey() {
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenReturn(TestUtils.createTestSurveyEntity());
		Mockito.when(surveyAdminDAO.getByCodeByVersion(any(), any()))
				.thenReturn(TestUtils.createTestSurveyEntity());
		SurveyDTO response = surveyAdminService.updateSurvey(surveyDTO, 1l);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}

	@Test(expected = AdminValidationException.class)
	public void testUpdateSurveyNoRecord() {
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenReturn(TestUtils.createTestSurveyEntity());
		SurveyDTO response = surveyAdminService.updateSurvey(surveyDTO, 1l);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}
	
	@Test(expected = AdminException.class)
	public void testUpdateSurveyError() {
		Survey value=new Survey();
		Mockito.when(surveyAdminDAO.getByCodeByVersion(any(),any())).thenReturn(value);
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenThrow(new RuntimeException());
		SurveyDTO response = surveyAdminService.updateSurvey(surveyDTO, 1l);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}

	@Test(expected = AdminValidationException.class)
	public void testUpdateSurveyNoQuestion() {
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenReturn(TestUtils.createTestSurveyEntity());
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenReturn(TestUtils.createTestSurveyEntity());
		surveyDTO.setQuestions(null);
		SurveyDTO response = surveyAdminService.updateSurvey(surveyDTO, 1l);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}

	@Test(expected = AdminValidationException.class)
	public void testUpdateSurveyNoQuestion_answer() {
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenReturn(TestUtils.createTestSurveyEntity());
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenReturn(TestUtils.createTestSurveyEntity());
		surveyDTO.getQuestions().get(0).setAnswers(null);
		SurveyDTO response = surveyAdminService.updateSurvey(surveyDTO, 1l);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}

	@Test
	public void testGetSurveysList() {
		List<Survey> list = new ArrayList<>();
		list.add(TestUtils.createTestSurveyEntity());
		Mockito.when(surveyAdminDAO.getAllSurvey()).thenReturn(list);
		List<SurveyDTO> responseList = surveyAdminService.getSurveysList();
		assertNotNull(responseList.get(0));
	}

	@Test
	public void testGetSurveysEmptyList() {
		List<Survey> list = new ArrayList<>();
		Mockito.when(surveyAdminDAO.getAllSurvey()).thenReturn(list);
	    surveyAdminService.getSurveysList();
	}
	
	@Test(expected=AdminException.class)
	public void testGetSurveysError() {
		Mockito.when(surveyAdminDAO.getAllSurvey()).thenThrow(new RuntimeException());
		List<SurveyDTO> responseList = surveyAdminService.getSurveysList();
		assertNotNull(responseList.get(0));
	}
	
	@Test(expected = AdminException.class)
	public void testGetSurveysListError() {
		Mockito.when(surveyAdminDAO.getAllSurvey()).thenThrow(new AdminException(""));
		List<SurveyDTO> responseList = surveyAdminService.getSurveysList();
		assertNull(responseList);
	}

	@Test
	public void testGetSurvey() {
		Mockito.when(surveyAdminDAO.getByCodeByVersion(Mockito.anyString(), Mockito.anyLong()))
				.thenReturn(TestUtils.createTestSurveyEntity());
		SurveyDTO response = surveyAdminService.getSurvey("code", 1l);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}
	
	@Test(expected = AdminValidationException.class)
	public void testGetSurveyError1() {
		Mockito.when(surveyAdminDAO.getByCodeByVersion(Mockito.anyString(), Mockito.anyLong()))
				.thenReturn(null);
		SurveyDTO response = surveyAdminService.getSurvey("code", 1l);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}
	

	@Test(expected = AdminValidationException.class)
	public void testGetSurveyError() {
		Mockito.when(surveyAdminDAO.getByCodeByVersion(Mockito.anyString(), Mockito.anyLong()))
				.thenThrow(new AdminValidationException(""));
		SurveyDTO response = surveyAdminService.getSurvey("code", 1l);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}
	
	@Test(expected = AdminException.class)
	public void testGetSurveyExp() {
		Mockito.when(surveyAdminDAO.getByCodeByVersion(Mockito.anyString(), Mockito.anyLong()))
				.thenThrow(new RuntimeException(""));
		SurveyDTO response = surveyAdminService.getSurvey("code", 1l);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}

	@Test
	public void testCreateNextVersion() {
		List<Survey> list = new ArrayList<>();
		list.add(TestUtils.createTestSurveyEntity());
		Mockito.when(surveyAdminDAO.findByCodeAndOrderByVersionDesc(any())).thenReturn(list);
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenReturn(TestUtils.createTestSurveyEntity());
		SurveyDTO response = surveyAdminService.createNextVersion(surveyDTO);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}

	@Test(expected = AdminValidationException.class)
	public void testCreateNextVersionNoRecord() {
		List<Survey> list = new ArrayList<>();
		list.add(TestUtils.createTestSurveyEntity());
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenReturn(TestUtils.createTestSurveyEntity());
		SurveyDTO response = surveyAdminService.createNextVersion(surveyDTO);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}

	@Test(expected = AdminException.class)
	public void testCreateNextVersionError() {
		List<Survey> list = new ArrayList<>();
		list.add(TestUtils.createTestSurveyEntity());
		Mockito.when(surveyAdminDAO.findByCodeAndOrderByVersionDesc(any())).thenReturn(list);
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenThrow(new RuntimeException());
		SurveyDTO response = surveyAdminService.createNextVersion(surveyDTO);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}
	
	@Test(expected = AdminValidationException.class)
	public void testCreateNextVersionNoQuestion() {
		List<Survey> list = new ArrayList<>();
		list.add(TestUtils.createTestSurveyEntity());
		Mockito.when(surveyAdminDAO.findByCodeAndOrderByVersionDesc(any())).thenReturn(list);
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenReturn(TestUtils.createTestSurveyEntity());
		SurveyDTO input = surveyDTO;
		input.setQuestions(null);
		SurveyDTO response = surveyAdminService.createNextVersion(input);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}

	@Test(expected = AdminValidationException.class)
	public void testCreateNextVersionNoAnswer() {
		List<Survey> list = new ArrayList<>();
		list.add(TestUtils.createTestSurveyEntity());
		Mockito.when(surveyAdminDAO.findByCodeAndOrderByVersionDesc(any())).thenReturn(list);
		Mockito.when(surveyAdminDAO.saveOrUpdateSurvey(any())).thenReturn(TestUtils.createTestSurveyEntity());
		SurveyDTO input = surveyDTO;
		input.getQuestions().get(0).setAnswers(null);
		SurveyDTO response = surveyAdminService.createNextVersion(input);
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}

	@Test
	public void testgetSurveyURLEmpty() {
		List<AdminViewDTO> response = surveyAdminService.getSurveyURLList();
		assertNotNull(response);
	}
	
	@Test
	public void testgetSurveyURL() {
		List<Survey> dataList=new ArrayList<>();
		Survey data=new Survey();
		dataList.add(data);
		Mockito.when(surveyAdminDAO.getAllSurvey()).thenReturn(dataList);
		List<AdminViewDTO> response = surveyAdminService.getSurveyURLList();
		assertNotNull(response);
	}
	

	@Test(expected = AdminValidationException.class)
	public void testgetSurveyURLError() {
		Mockito.when(surveyAdminDAO.getAllSurvey())
				.thenThrow(new AdminValidationException(""));
		surveyAdminService.getSurveyURLList();
	}

	@Test(expected = AdminException.class)
	public void testgetSurveyURLAminError() {
		Mockito.when(surveyAdminDAO.getAllSurvey())
				.thenThrow(new AdminException(""));
		surveyAdminService.getSurveyURLList();
	}

}
