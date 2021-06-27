package com.oracle.survey.surveyadmin.controller.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.survey.surveyadmin.dto.BaseResponseDTO;
import com.oracle.survey.surveyadmin.dto.SurveyDTO;
import com.oracle.survey.surveyadmin.entity.Survey;
import com.oracle.survey.surveyadmin.util.SurveyAdminUtils;

@Component
public class TestUtils {

	public static void assertUpdateSurveyDTO(BaseResponseDTO<SurveyDTO> response, SurveyDTO surveyDTO) {

		assertEquals(response.getBody().getCode(), surveyDTO.getCode());
		assertNotSame(response.getBody().getDescription(), surveyDTO.getDescription());
		assertNotSame(response.getBody().getName(), surveyDTO.getName());
		assertEquals(response.getBody().getReady(), surveyDTO.getReady());

	}

	public static void assertCreateSurveyDTO(BaseResponseDTO<SurveyDTO> response, SurveyDTO surveyDTO) {
		assertEquals(response.getBody().getCode(), surveyDTO.getCode());
		assertEquals(response.getBody().getDescription(), surveyDTO.getDescription());
		assertEquals(response.getBody().getName(), surveyDTO.getName());
		assertEquals(response.getBody().getReady(), surveyDTO.getReady());
	}

	public static SurveyDTO createTestSurveyDTO() {
		SurveyDTO surveyDTO = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			surveyDTO = mapper.readValue(
					"{ \"name\": \"My First Vaccine Shot\", \"description\": \"Survey to get covid vaccination Status of Employees\", \"code\": \"CovidEmpVacc\", \"ready\": true, \"questions\": [{ \"name\": \"location\", \"description\": \"Employee location\", \"code\": \"locationcode\", \"text\": \"What is ur current location\", \"required\": true, \"inputtype\": \"text\", \"answers\": [{ \"id\": \"1\", \"title\": \"location\", \"text\": \"please enter your current location\", \"priority\": \"1\", \"description\": \"What is your current location\" }] }] }",
					SurveyDTO.class);
		} catch (Exception e) {

		}
		return surveyDTO;
	}

	public static Survey createTestSurveyEntity() {
		Survey survey = null;
		try {
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			SurveyDTO surveydto = mapper.readValue(
					"{ \"name\": \"My First Vaccine Shot\", \"description\": \"Survey to get covid vaccination Status of Employees\", \"code\": \"CovidEmpVacc\", \"ready\": true, \"questions\": [{ \"name\": \"location\", \"description\": \"Employee location\", \"code\": \"locationcode\", \"text\": \"What is ur current location\", \"required\": true, \"inputtype\": \"text\", \"answers\": [{ \"id\": \"1\", \"title\": \"location\", \"text\": \"please enter your current location\", \"priority\": \"1\", \"description\": \"What is your current location\" }] }] }",
					SurveyDTO.class);

			survey = SurveyAdminUtils.createSurveyEntity(surveydto);
		} catch (Exception e) {
		}
		return survey;
	}

	public static void assertCreateSurveyDTO(SurveyDTO response, SurveyDTO surveyDTO) {
		assertEquals(response.getCode(), surveyDTO.getCode());
		assertEquals(response.getDescription(), surveyDTO.getDescription());
		assertEquals(response.getName(), surveyDTO.getName());
		assertEquals(response.getReady(), surveyDTO.getReady());
	}

	public static void assertCreateSurveyDTO(Survey response, Survey survey) {
		assertEquals(response.getCode(), survey.getCode());
		assertEquals(response.getDescription(), survey.getDescription());
		assertEquals(response.getName(), survey.getName());
		assertEquals(response.getReady(), survey.getReady());

	}
}
