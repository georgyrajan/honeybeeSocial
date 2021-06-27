package com.oracle.survey.surveycustomer.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import com.oracle.survey.surveycustomer.entity.CustomerAnswer;
import com.oracle.survey.surveycustomer.repository.CustomerAnswerRepository;
import com.oracle.survey.surveycustomer.utils.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
public class SurveyCustomerAnswerDAOTest {

	@Mock
	CustomerAnswerRepository customerAnswerRepository;

	@InjectMocks
	SurveyCustomerAnswerDAO surveyCustomerAnswerDAO;

	@Test
	public void testCreateCustomerAnswer() {
		Mockito.when(customerAnswerRepository.save(Mockito.any()))
				.thenReturn(TestUtils.createTestCustomerAnswerEntity());
		CustomerAnswer customerAnswer = surveyCustomerAnswerDAO
				.createCustomerAnswer(TestUtils.createTestCustomerAnswerEntity());
		assertNotNull(customerAnswer);
	}

	@Test
	public void testFindBySurveyCodeAndSurveyVersionAndUserId() {
		Mockito.when(customerAnswerRepository.findBySurveyCodeAndSurveyVersionAndUserId(Mockito.any(), Mockito.any(),
				Mockito.any())).thenReturn(TestUtils.createTestCustomerAnswerEntity());
		CustomerAnswer customerAnswer = surveyCustomerAnswerDAO.findBySurveyCodeAndSurveyVersionAndUserId("test", 1l,
				"test");
		assertNotNull(customerAnswer);
	}

	@Test
	public void testGetSurveyDetails() {
		List<Object[]> data = new ArrayList<>();
		Mockito.when(customerAnswerRepository.findBySurveyCodeAndSurveyVersion(Mockito.any(), Mockito.any()))
				.thenReturn(data);
		List<Object[]> customerAnswer = surveyCustomerAnswerDAO.getSurveyDetails("test", 1l);
		assertNotNull(customerAnswer);
	}
	
	@Test
	public void testGetAllData() {
		List<Object[]> data = new ArrayList<>();
		Mockito.when(customerAnswerRepository.findAllSurvey())
				.thenReturn(data);
		List<Object[]> customerAnswer = surveyCustomerAnswerDAO.getAllData();
		assertNotNull(customerAnswer);
	}
}
