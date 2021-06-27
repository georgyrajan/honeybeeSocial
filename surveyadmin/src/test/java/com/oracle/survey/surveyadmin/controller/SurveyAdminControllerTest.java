package com.oracle.survey.surveyadmin.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.mockito.ArgumentMatchers.any;
import com.oracle.survey.surveyadmin.controller.utils.TestUtils;
import com.oracle.survey.surveyadmin.dto.BaseResponseDTO;
import com.oracle.survey.surveyadmin.dto.SurveyDTO;
import com.oracle.survey.surveyadmin.service.SurveyAdminService;

@RunWith(SpringJUnit4ClassRunner.class)
public class SurveyAdminControllerTest {

	@Mock
	SurveyAdminService surveyAdminService;

	@InjectMocks
	SurveyAdminController surveyAdminController;

	SurveyDTO surveyDTO;

	@Before
	public void init() {
		surveyDTO = TestUtils.createTestSurveyDTO();
	}

	@Test
	public void testcreateSurvey() {
		Mockito.when(surveyAdminService.createSurvey(any())).thenReturn(surveyDTO);
		BaseResponseDTO<SurveyDTO> response = surveyAdminController.createSurvey(surveyDTO, "asSa", "aSASasa");
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}

	@Test
	public void testUpdateSurvey() {
		SurveyDTO surveyDTOUpdated = TestUtils.createTestSurveyDTO();
		surveyDTOUpdated.setDescription("updatedcode");
		surveyDTOUpdated.setName("updated");
		Mockito.when(surveyAdminService.updateSurvey(any(), any())).thenReturn(surveyDTOUpdated);
		BaseResponseDTO<SurveyDTO> response = surveyAdminController.updateSurvey(surveyDTO, 1l, "aSASasa");
		TestUtils.assertUpdateSurveyDTO(response, surveyDTO);
	}

	@Test
	public void testCreateNextVersion() {
		Mockito.when(surveyAdminService.createNextVersion(any())).thenReturn(surveyDTO);
		BaseResponseDTO<SurveyDTO> response = surveyAdminController.createNextVersion(surveyDTO, "jwttoken");
		TestUtils.assertCreateSurveyDTO(response, surveyDTO);
	}

	@Test
	public void testGetAllSurvey() {
		List<SurveyDTO> responseList = new ArrayList<>();
		responseList.add(surveyDTO);
		Mockito.when(surveyAdminService.getSurveysList()).thenReturn(responseList);
		BaseResponseDTO<List<SurveyDTO>> response = surveyAdminController.getAllSurvey("jwttoken");
		TestUtils.assertCreateSurveyDTO(response.getBody().get(0), surveyDTO);
	}

}
