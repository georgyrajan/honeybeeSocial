package com.oracle.survey.surveycustomer.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oracle.survey.surveycustomer.dto.BaseResponseDTO;
import com.oracle.survey.surveycustomer.dto.CustomerAnswerDTO;
import com.oracle.survey.surveycustomer.service.SurveyCustomerAnswerService;
import com.oracle.survey.surveycustomer.utils.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
public class SurveyCustomerAnswerControllerTest {

	@Mock
	SurveyCustomerAnswerService surveyCustomerService;
	
	@InjectMocks
	SurveyCustomerAnswerController surveyCustomerAnswerController;
	
	CustomerAnswerDTO CustomerAnswerDTO;

	@Before
	public void init() {
		CustomerAnswerDTO = TestUtils.createTestCustomerAnswerDTO();
	}

	@Test
	public void testcreateSurvey() {
		Mockito.when(surveyCustomerService.createCustomerAnswer(Mockito.any())).thenReturn(CustomerAnswerDTO);
		BaseResponseDTO<CustomerAnswerDTO> response = surveyCustomerAnswerController.createCustomerAnswer(CustomerAnswerDTO, "asSa", "aSASasa");
		TestUtils.assertCreateCustomerAnswerDTO(response, CustomerAnswerDTO);
	}

	@Test
	public void testUpdateCustomerAnswer() {
		CustomerAnswerDTO CustomerAnswerDTOUpdated = TestUtils.createTestCustomerAnswerDTO();
		CustomerAnswerDTOUpdated.getSurveyDetails().setCode("updatedcode");
		CustomerAnswerDTOUpdated.getSurveyDetails().setName("updated");
		Mockito.when(surveyCustomerService.updateCustomerAnswer(Mockito.any())).thenReturn(CustomerAnswerDTOUpdated);
		BaseResponseDTO<CustomerAnswerDTO> response = surveyCustomerAnswerController.updateCustomerAnswer(CustomerAnswerDTO,"");
		TestUtils.assertUpdateCustomerAnswerDTO(response, CustomerAnswerDTO);
	}


	@Test
	public void testGetCustomerAnswer() {
		CustomerAnswerDTO CustomerAnswerDTOUpdated = TestUtils.createTestCustomerAnswerDTO();
		CustomerAnswerDTOUpdated.getSurveyDetails().setCode("updatedcode");
		CustomerAnswerDTOUpdated.getSurveyDetails().setName("updated");
		Mockito.when(surveyCustomerService.getCustomerAnswer(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(CustomerAnswerDTOUpdated);
		surveyCustomerAnswerController.getCustomerAnswer("user", "code", 1l, "jwt token");
	}
	
	@Test
	public void testGetCustomerAnswerList() {
		CustomerAnswerDTO CustomerAnswerDTOUpdated = TestUtils.createTestCustomerAnswerDTO();
		CustomerAnswerDTOUpdated.getSurveyDetails().setCode("updatedcode");
		CustomerAnswerDTOUpdated.getSurveyDetails().setName("updated");
		List<CustomerAnswerDTO> surveyList = new ArrayList<>();
		surveyList.add(CustomerAnswerDTOUpdated);
		Mockito.when(surveyCustomerService.getCustomerAnswerList(Mockito.any())).thenReturn(surveyList);
		surveyCustomerAnswerController.getCustomerAnswerList("user", "code");
	}
}
