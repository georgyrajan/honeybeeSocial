package com.oracle.survey.surveycustomer.service;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oracle.survey.surveycustomer.dao.SurveyCustomerAnswerDAO;
import com.oracle.survey.surveycustomer.dto.BaseResponseDTO;
import com.oracle.survey.surveycustomer.dto.CustomerAnswerDTO;
import com.oracle.survey.surveycustomer.dto.SurveyDTO;
import com.oracle.survey.surveycustomer.dto.SurveySpecificDetailsDTO;
import com.oracle.survey.surveycustomer.dto.SurveysCompletionDataResponseDTO;
import com.oracle.survey.surveycustomer.entity.CustomerAnswer;
import com.oracle.survey.surveycustomer.exception.CustomerException;
import com.oracle.survey.surveycustomer.util.SurveyCustomerAnswerUtils;
import com.oracle.survey.surveycustomer.utils.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
public class SurveyCustomerAnswerServiceTest {

	@Mock
	SurveyCustomerAnswerDAO surveyCustomerAnswerDAO;

	@Mock
	SurveyCustomerAnswerUtils surveyCustomerAnswerUtils;

	@InjectMocks
	SurveyCustomerAnswerService surveyCustomerAnswerService;

	@Test
	public void testCreateCustomerAnswer() {
		CustomerAnswerDTO customerAnswerDTO = TestUtils.createTestCustomerAnswerDTO();
		Mockito.when(surveyCustomerAnswerDAO.createCustomerAnswer(Mockito.any()))
				.thenReturn(TestUtils.createTestCustomerAnswerEntity());
		CustomerAnswerDTO response = surveyCustomerAnswerService.createCustomerAnswer(customerAnswerDTO);
		TestUtils.assertCustomerAnswerDTO(response, customerAnswerDTO);
	}

	@Test(expected = CustomerException.class)
	public void testCreateCustomerAnswer_duplicate() {
		CustomerAnswerDTO customerAnswerDTO = TestUtils.createTestCustomerAnswerDTO();
		Mockito.when(surveyCustomerAnswerDAO.findBySurveyCodeAndSurveyVersionAndUserId(Mockito.any(), Mockito.any(),
				Mockito.any())).thenReturn(TestUtils.createTestCustomerAnswerEntity());
		Mockito.when(surveyCustomerAnswerDAO.createCustomerAnswer(Mockito.any()))
				.thenReturn(TestUtils.createTestCustomerAnswerEntity());
		CustomerAnswerDTO response = surveyCustomerAnswerService.createCustomerAnswer(customerAnswerDTO);
		TestUtils.assertCustomerAnswerDTO(response, customerAnswerDTO);
	}

	@Test(expected = CustomerException.class)
	public void testCreateCustomerAnswer_error() {
		CustomerAnswerDTO customerAnswerDTO = TestUtils.createTestCustomerAnswerDTO();
		Mockito.when(surveyCustomerAnswerDAO.createCustomerAnswer(Mockito.any()))
				.thenThrow(new RuntimeException("test"));
		CustomerAnswerDTO response = surveyCustomerAnswerService.createCustomerAnswer(customerAnswerDTO);
		TestUtils.assertCustomerAnswerDTO(response, customerAnswerDTO);
	}

	@Test
	public void testUpdateCustomerAnswer() {
		CustomerAnswerDTO customerAnswerDTO = TestUtils.createTestCustomerAnswerDTO();
		Mockito.when(surveyCustomerAnswerDAO.createCustomerAnswer(Mockito.any()))
				.thenReturn(TestUtils.createTestCustomerAnswerEntity());
		CustomerAnswerDTO response = surveyCustomerAnswerService.updateCustomerAnswer(customerAnswerDTO);
		TestUtils.assertCustomerAnswerDTO(response, customerAnswerDTO);
	}
	
	@Test
	public void testUpdateCustomerAnswer_else() {
		CustomerAnswerDTO customerAnswerDTO = TestUtils.createTestCustomerAnswerDTO();
		Mockito.when(surveyCustomerAnswerDAO.findBySurveyCodeAndSurveyVersionAndUserId(Mockito.any(), Mockito.any(),
				Mockito.any())).thenReturn(TestUtils.createTestCustomerAnswerEntity());
		Mockito.when(surveyCustomerAnswerDAO.createCustomerAnswer(Mockito.any()))
				.thenReturn(TestUtils.createTestCustomerAnswerEntity());
		CustomerAnswerDTO response = surveyCustomerAnswerService.updateCustomerAnswer(customerAnswerDTO);
		TestUtils.assertCustomerAnswerDTO(response, customerAnswerDTO);
	}
	

	@Test(expected = CustomerException.class)
	public void testUpdateCustomerAnswer_duplicate() {
		CustomerAnswerDTO customerAnswerDTO = TestUtils.createTestCustomerAnswerDTO();
		Mockito.when(surveyCustomerAnswerDAO.findBySurveyCodeAndSurveyVersionAndUserId(Mockito.any(), Mockito.any(),
				Mockito.any())).thenReturn(null).thenReturn(TestUtils.createTestCustomerAnswerEntity());
		Mockito.when(surveyCustomerAnswerDAO.createCustomerAnswer(Mockito.any()))
				.thenReturn(TestUtils.createTestCustomerAnswerEntity());
		CustomerAnswerDTO response = surveyCustomerAnswerService.updateCustomerAnswer(customerAnswerDTO);
		TestUtils.assertCustomerAnswerDTO(response, customerAnswerDTO);
	}

	@Test(expected = CustomerException.class)
	public void testUpdateCustomerAnswer_error() {
		CustomerAnswerDTO customerAnswerDTO = TestUtils.createTestCustomerAnswerDTO();
		Mockito.when(surveyCustomerAnswerDAO.createCustomerAnswer(Mockito.any()))
				.thenThrow(new RuntimeException("test"));
		CustomerAnswerDTO response = surveyCustomerAnswerService.updateCustomerAnswer(customerAnswerDTO);
		TestUtils.assertCustomerAnswerDTO(response, customerAnswerDTO);
	}
	
	
	@Test
	public void testGetCustomerAnswer() {
		CustomerAnswerDTO customerAnswerDTO = TestUtils.createTestCustomerAnswerDTO();
		Mockito.when(surveyCustomerAnswerDAO.findBySurveyCodeAndSurveyVersionAndUserId(Mockito.any(), Mockito.any(),
				Mockito.any())).thenReturn(TestUtils.createTestCustomerAnswerEntity());
		CustomerAnswerDTO response = surveyCustomerAnswerService.getCustomerAnswer("userid", "code", 1l , "jwt");
		TestUtils.assertCustomerAnswerDTO(response, customerAnswerDTO);
	}
	
	@Test
	public void testGetCustomerAnswer_empty_customerAnswer() {
		CustomerAnswerDTO customerAnswerDTO = TestUtils.createTestCustomerAnswerDTO();
		Mockito.when(surveyCustomerAnswerDAO.findBySurveyCodeAndSurveyVersionAndUserId(Mockito.any(), Mockito.any(),
				Mockito.any())).thenReturn(null);
		BaseResponseDTO<SurveyDTO> data = new BaseResponseDTO<>();
		data.setBody(new SurveyDTO());
		data.getBody().setCode(customerAnswerDTO.getSurveyDetails().getCode());
		data.getBody().setDescription(customerAnswerDTO.getSurveyDetails().getDescription());
		data.getBody().setName(customerAnswerDTO.getSurveyDetails().getName());
		Mockito.when(surveyCustomerAnswerUtils.callAdminModuleAndGetSurvey(Mockito.any(), Mockito.any(),
				Mockito.any(),Mockito.any())).thenReturn(data);
		CustomerAnswerDTO response = surveyCustomerAnswerService.getCustomerAnswer("userid", "code", 1l , "jwt");
		assertNotNull(response);
	}
	
	@Test(expected=CustomerException.class)
	public void testGetCustomerAnswer_error() {
		Mockito.when(surveyCustomerAnswerDAO.findBySurveyCodeAndSurveyVersionAndUserId(Mockito.any(), Mockito.any(),
				Mockito.any())).thenThrow(new RuntimeException());
		 surveyCustomerAnswerService.getCustomerAnswer("userid", "code", 1l , "jwt");
	}
	
	@Test
	public void testGetSurveyDetails() {
		List<Object[]> data = new ArrayList<Object[]>();
		Mockito.when(surveyCustomerAnswerDAO.getSurveyDetails(Mockito.any(), Mockito.any())).thenReturn(data);
		List<SurveySpecificDetailsDTO> response = surveyCustomerAnswerService.getSurveyDetails("code", 1l);
		assertNotNull(response);
	}
	
	@Test
	public void testGetAllData() {
		List<Object[]> data = new ArrayList<Object[]>();
		Mockito.when(surveyCustomerAnswerDAO.getAllData()).thenReturn(data);
		SurveysCompletionDataResponseDTO response = surveyCustomerAnswerService.getAllData();
		assertNotNull(response);
	}
	
	@Test
	public void testGetCustomerAnswerList() {
		CustomerAnswer CustomerAnswerDTOUpdated = TestUtils.createTestCustomerAnswerEntity();
		CustomerAnswerDTOUpdated.setSurveyCode("code");
		CustomerAnswerDTOUpdated.setUserId("updated");
		List<CustomerAnswer> surveyList = new ArrayList<>();
		surveyList.add(CustomerAnswerDTOUpdated);
		Mockito.when(surveyCustomerAnswerDAO.getCustomerAnswerList(Mockito.any())).thenReturn(surveyList);
		surveyCustomerAnswerService.getCustomerAnswerList("user");
	}
}
