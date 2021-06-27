package com.oracle.survey.surveycustomer.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.survey.surveycustomer.dto.BaseResponseDTO;
import com.oracle.survey.surveycustomer.dto.CustomerAnswerDTO;
import com.oracle.survey.surveycustomer.dto.SurveyDTO;
import com.oracle.survey.surveycustomer.dto.SurveySpecificDetailsDTO;
import com.oracle.survey.surveycustomer.entity.CustomerAnswer;
import com.oracle.survey.surveycustomer.util.SurveyCustomerAnswerUtils;

@Component
public class TestUtils {

	public static void assertCreateCustomerAnswerDTO(BaseResponseDTO<CustomerAnswerDTO> response,
			CustomerAnswerDTO customerAnswerDTO) {

		assertEquals(response.getBody().getIsComplete(), customerAnswerDTO.getIsComplete());
		assertEquals(response.getBody().getSurveyCode(), customerAnswerDTO.getSurveyCode());
		assertEquals(response.getBody().getSurveyDetails(), customerAnswerDTO.getSurveyDetails());
		assertEquals(response.getBody().getSurveyVersion(), customerAnswerDTO.getSurveyVersion());
		assertEquals(response.getBody().getUserId(), customerAnswerDTO.getUserId());
		assertCreateSurveyDTO(response.getBody().getSurveyDetails(), customerAnswerDTO.getSurveyDetails());

	}

	public static void assertCreateSurveyDTO(BaseResponseDTO<SurveyDTO> response, SurveyDTO surveyDTO) {
		assertEquals(response.getBody().getCode(), surveyDTO.getCode());
		assertEquals(response.getBody().getDescription(), surveyDTO.getDescription());
		assertEquals(response.getBody().getName(), surveyDTO.getName());
		assertEquals(response.getBody().getReady(), surveyDTO.getReady());
	}

	public static void assertCustomerAnswerDTO(CustomerAnswerDTO response, CustomerAnswerDTO customerAnswerDTO) {

		assertEquals(response.getIsComplete(), customerAnswerDTO.getIsComplete());
		assertEquals(response.getSurveyCode(), customerAnswerDTO.getSurveyCode());
		assertEquals(response.getSurveyDetails(), customerAnswerDTO.getSurveyDetails());
		assertEquals(response.getSurveyVersion(), customerAnswerDTO.getSurveyVersion());
		assertEquals(response.getUserId(), customerAnswerDTO.getUserId());
		assertCreateSurveyDTO(response.getSurveyDetails(), customerAnswerDTO.getSurveyDetails());

	}

	public static CustomerAnswerDTO createTestCustomerAnswerDTO() {
		CustomerAnswerDTO customerAnswerDTO = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			customerAnswerDTO = mapper.readValue(
					"{ \"userId\": \"1\", \"surveyCode\": \"CovidEmpVacc\", \"surveyVersion\": 1, \"isComplete\": false, \"isFirstTime\": null, \"surveyDetails\": { \"name\": \"My First Vaccine Shot\", \"description\": \"Survey to get covid vaccination Status of Employees\", \"code\": \"CovidEmpVacc\", \"ready\": true, \"questions\": [{ \"name\": \"location\", \"description\": \"Employee location\", \"code\": \"locationcode\", \"text\": \"What is ur current location\", \"required\": true, \"inputtype\": \"text\", \"answers\": [{ \"id\": \"0\", \"title\": \"location\", \"text\": \"please enter your current location\", \"priority\": \"1\", \"description\": \"What is your current location\", \"answer\": \"kottayam\" }] }] } }",
					CustomerAnswerDTO.class);
		} catch (Exception e) {

		}
		return customerAnswerDTO;
	}

	public static CustomerAnswer createTestCustomerAnswerEntity() {
		CustomerAnswer customerAnswer = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			CustomerAnswerDTO customerAnswerDTO = mapper.readValue(
					"{ \"userId\": \"1\", \"surveyCode\": \"CovidEmpVacc\", \"surveyVersion\": 1, \"isComplete\": false, \"isFirstTime\": null, \"surveyDetails\": { \"name\": \"My First Vaccine Shot\", \"description\": \"Survey to get covid vaccination Status of Employees\", \"code\": \"CovidEmpVacc\", \"ready\": true, \"questions\": [{ \"name\": \"location\", \"description\": \"Employee location\", \"code\": \"locationcode\", \"text\": \"What is ur current location\", \"required\": true, \"inputtype\": \"text\", \"answers\": [{ \"id\": \"0\", \"title\": \"location\", \"text\": \"please enter your current location\", \"priority\": \"1\", \"description\": \"What is your current location\", \"answer\": \"kottayam\" }] }] } }",
					CustomerAnswerDTO.class);

			customerAnswer = SurveyCustomerAnswerUtils.createCustomerAnswerEntity(customerAnswerDTO);
		} catch (Exception e) {

		}
		return customerAnswer;
	}

	public static void assertCreateSurveyDTO(SurveyDTO response, SurveyDTO surveyDTO) {
		assertEquals(response.getCode(), surveyDTO.getCode());
		assertEquals(response.getDescription(), surveyDTO.getDescription());
		assertEquals(response.getName(), surveyDTO.getName());
		assertEquals(response.getReady(), surveyDTO.getReady());
	}

	public static SurveySpecificDetailsDTO createTestSurveySpecificDetailsDTO() {
		SurveySpecificDetailsDTO surveySpecificDetailsDTO = new SurveySpecificDetailsDTO();
		surveySpecificDetailsDTO.setCode("test code");
		surveySpecificDetailsDTO.setVersion(1l);
		return surveySpecificDetailsDTO;
	}

	public static void assertSpecificDetailsDTO(SurveySpecificDetailsDTO surveySpecificDetailsDTO,
			BaseResponseDTO<List<SurveySpecificDetailsDTO>> surveySpecificDetailsDTOList) {
		assertEquals(surveySpecificDetailsDTOList.getBody().get(0).getCode(), surveySpecificDetailsDTO.getCode());
		assertEquals(surveySpecificDetailsDTOList.getBody().get(0).getVersion(), surveySpecificDetailsDTO.getVersion());
	}

	public static void assertUpdateCustomerAnswerDTO(BaseResponseDTO<CustomerAnswerDTO> response,
			CustomerAnswerDTO customerAnswerDTO) {
		assertNotSame(response.getBody().getSurveyDetails().getCode(), customerAnswerDTO.getSurveyDetails().getCode());
		assertNotSame(response.getBody().getSurveyDetails().getName(), customerAnswerDTO.getSurveyDetails().getName());
	}

}
