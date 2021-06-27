package com.oracle.survey.surveycustomer.controller;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oracle.survey.surveycustomer.dto.BaseResponseDTO;
import com.oracle.survey.surveycustomer.dto.SurveySpecificDetailsDTO;
import com.oracle.survey.surveycustomer.dto.SurveysCompletionDataResponseDTO;
import com.oracle.survey.surveycustomer.service.SurveyCustomerAnswerService;
import com.oracle.survey.surveycustomer.utils.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
public class SurveyAnalyticsControllerTest {
	@Mock
	SurveyCustomerAnswerService surveyCustomerService;
	
	@InjectMocks
	SurveyAnalyticsController surveyAnalyticsController;
	
	
	@Test
	public void testGetSurveyDetails(){
		
		List<SurveySpecificDetailsDTO> surveySpecificDetailsDTOList= new ArrayList<>();
		SurveySpecificDetailsDTO surveySpecificDetailsDTO = TestUtils.createTestSurveySpecificDetailsDTO();
		surveySpecificDetailsDTOList.add(surveySpecificDetailsDTO);
		Mockito.when(surveyCustomerService.getSurveyDetails(Mockito.any(), Mockito.any())).thenReturn(surveySpecificDetailsDTOList);
		BaseResponseDTO<List<SurveySpecificDetailsDTO>>  surveySpecificDetailsDTOListResponse = surveyAnalyticsController.getSurveyDetails("code", 1l, "token", "");
		TestUtils.assertSpecificDetailsDTO(surveySpecificDetailsDTOList.get(0),surveySpecificDetailsDTOListResponse);
		
	}
	
	@Test
	public void testGetAllData(){
		SurveysCompletionDataResponseDTO surveysCompletionDataResponseDTO = new SurveysCompletionDataResponseDTO();;
		Mockito.when(surveyCustomerService.getAllData()).thenReturn(surveysCompletionDataResponseDTO);
		BaseResponseDTO<SurveysCompletionDataResponseDTO>  surveySpecificDetailsDTOListResponse = surveyAnalyticsController.getAllData("token", "");
		assertNotNull(surveySpecificDetailsDTOListResponse.getBody());
	}
	
}
