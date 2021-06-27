package com.oracle.survey.surveyadmin.controller;

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
import com.oracle.survey.surveyadmin.dto.AdminViewDTO;
import com.oracle.survey.surveyadmin.dto.BaseResponseDTO;
import com.oracle.survey.surveyadmin.dto.SurveyDTO;
import com.oracle.survey.surveyadmin.service.SurveyAdminService;

@RunWith(SpringJUnit4ClassRunner.class)
public class SurveyControllerTest {

	@Mock
	SurveyAdminService surveyAdminService;

	@InjectMocks
	SurveyController surveyController;

	SurveyDTO surveyDTO;

	@Before
	public void init() {
		surveyDTO = TestUtils.createTestSurveyDTO();
	}

	@Test
	public void testGetSurvey() {
		Mockito.when(surveyAdminService.getSurvey("test", 1l)).thenReturn(surveyDTO);
		BaseResponseDTO<SurveyDTO> response = surveyController.getSurvey("test", 1l, "jwt");
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}

	@Test
	public void testGetSurveyURL() {
		Mockito.when(surveyAdminService.getSurveyURLList()).thenReturn(new ArrayList<AdminViewDTO>());
		BaseResponseDTO<List<AdminViewDTO>> response = surveyController.getSurveyURL("jwt");
		assertNotNull(response);
	}
}
