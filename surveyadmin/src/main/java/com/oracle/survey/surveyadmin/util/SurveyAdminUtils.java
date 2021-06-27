package com.oracle.survey.surveyadmin.util;

import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.survey.surveyadmin.dto.AnswerDTO;
import com.oracle.survey.surveyadmin.dto.BaseResponseDTO;
import com.oracle.survey.surveyadmin.dto.QuestionDTO;
import com.oracle.survey.surveyadmin.dto.SurveyDTO;
import com.oracle.survey.surveyadmin.entity.Question;
import com.oracle.survey.surveyadmin.entity.Survey;
import com.oracle.survey.surveyadmin.exception.AdminException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * This is the Util class to handle admin util methods
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
@Component
public class SurveyAdminUtils {
	private static final Logger LOGGER = LogManager.getLogger(SurveyAdminUtils.class);
	
	private static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
			false);

	static String baseUrl;

	static String secret;

	@Value("${app.survey.jwt.secret}")
	public void setSecret(String value) {
		SurveyAdminUtils.secret = value;
	}

	@Value("${app.survey.base.url}")
	public void setBaseUrl(String value) {
		SurveyAdminUtils.baseUrl = value;
	}

	static RestTemplate restTemplate;

	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		SurveyAdminUtils.restTemplate = restTemplate;
	}

	/**
	 * This method is used to check if the jwt is valid
	 * 
	 * @param jwtString
	 * @return
	 */
	public static Jws<Claims> validateJWT(String jwtString) {

		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
		Jws<Claims> jwt = null;
		try {
			jwt = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(jwtString);

		} catch (Exception e) {
			throw new AdminException("Invalid session please log in");
		}
		return jwt;
	}

	/**
	 * This method is used to create the survey entity
	 * 
	 * @param surveyDTO
	 * @return
	 */
	public static Survey createSurveyEntity(SurveyDTO surveyDTO) {
		Survey survey = new Survey();
		List<Question> questionlist = new ArrayList<>();
		survey.setId(UUID.randomUUID().toString().toUpperCase().replace("-", ""));
		survey.setCode(surveyDTO.getCode());
		survey.setDescription(surveyDTO.getDescription());
		survey.setVersion(1l);
		survey.setUrl(baseUrl + "?code=" + survey.getCode() + "&version=" + survey.getVersion());
		survey.setName(surveyDTO.getName());
		survey.setReady(surveyDTO.getReady());
		Survey surveyUpdated = survey;
		surveyDTO.getQuestions().forEach(question -> {
			Question currentQuestion = new Question();
			currentQuestion.setId(UUID.randomUUID().toString().toUpperCase().replace("-", ""));
			currentQuestion.setSurvey(surveyUpdated);
			currentQuestion.setCode(question.getCode());
			currentQuestion.setDescription(question.getDescription());
			currentQuestion.setInputtype(question.getInputtype());
			currentQuestion.setName(question.getName());
			currentQuestion.setRequired(question.getRequired());
			currentQuestion.setText(question.getText());
			AtomicInteger counter = new AtomicInteger(0);
			question.getAnswers().forEach(answer -> answer.setId(String.valueOf(counter.getAndIncrement())));

			try {
				currentQuestion.setAnswers(mapper.writeValueAsString(question.getAnswers()));
			} catch (JsonProcessingException e) {
				LOGGER.error("Error while parsing the json in createSurveyEntity %s :",e.getMessage());
			}
			questionlist.add(currentQuestion);
		});
		surveyUpdated.setQuestions(questionlist);
		return surveyUpdated;
	}

	/**
	 * This method is used to create the survey dto
	 * 
	 * @param survey
	 * @return
	 */
	public static SurveyDTO createSurveyDTO(Survey survey) {
		SurveyDTO surveyDTO = new SurveyDTO();
		List<QuestionDTO> questionlist = new ArrayList<>();
		surveyDTO.setCode(survey.getCode());
		surveyDTO.setDescription(survey.getDescription());
		surveyDTO.setName(survey.getName());
		surveyDTO.setReady(survey.getReady());
		SurveyDTO surveyUpdated = surveyDTO;
		survey.getQuestions().forEach(question -> {
			QuestionDTO currentQuestion = new QuestionDTO();
			currentQuestion.setCode(question.getCode());
			currentQuestion.setDescription(question.getDescription());
			currentQuestion.setInputtype(question.getInputtype());
			currentQuestion.setName(question.getName());
			currentQuestion.setRequired(question.getRequired());
			currentQuestion.setText(question.getText());

			try {
				currentQuestion.setAnswers(mapper.readValue(question.getAnswers(),
						mapper.getTypeFactory().constructCollectionType(List.class, AnswerDTO.class)));
			} catch (JsonProcessingException e) {
				LOGGER.error("Error while parsing the json in createSurveyDTO %s :",e.getMessage());
			}
			questionlist.add(currentQuestion);
		});
		surveyUpdated.setQuestions(questionlist);
		return surveyUpdated;
	}

	/**
	 * This method is used to create new version of already present survey
	 * 
	 * @param surveyDTO
	 * @param survey
	 * @return
	 */
	public static Survey createNewVersionSurveyEntity(SurveyDTO surveyDTO, Survey survey) {
		Survey surveyNewVersion = new Survey();
		List<Question> questionlist = new ArrayList<>();
		Long version = survey.getVersion() + 1;
		surveyNewVersion.setId(UUID.randomUUID().toString().toUpperCase().replace("-", ""));
		surveyNewVersion.setCode(surveyDTO.getCode());
		surveyNewVersion.setDescription(surveyDTO.getDescription());
		surveyNewVersion.setVersion(version);
		surveyNewVersion.setUrl(baseUrl + "?code=" + survey.getCode() + "&version=" + version);
		surveyNewVersion.setName(surveyDTO.getName());
		surveyNewVersion.setReady(surveyDTO.getReady());
		Survey surveyUpdated = surveyNewVersion;
		surveyDTO.getQuestions().forEach(question -> {
			Question currentQuestion = new Question();
			currentQuestion.setId(UUID.randomUUID().toString().toUpperCase().replace("-", ""));
			currentQuestion.setSurvey(surveyUpdated);
			currentQuestion.setCode(question.getCode());
			currentQuestion.setDescription(question.getDescription());
			currentQuestion.setInputtype(question.getInputtype());
			currentQuestion.setName(question.getName());
			currentQuestion.setRequired(question.getRequired());
			currentQuestion.setText(question.getText());
			try {
				currentQuestion.setAnswers(mapper.writeValueAsString(question.getAnswers()));
			} catch (JsonProcessingException e) {
				LOGGER.error("Error while parsing the json %s :",e.getMessage());
			}
			questionlist.add(currentQuestion);
		});
		surveyUpdated.setQuestions(questionlist);
		return surveyUpdated;
	}

	/**
	 * This method is used to update the survey entity object
	 * 
	 * @param surveyDTO
	 * @param surveyToUpdate
	 * @return
	 */
	public static Survey updateSurveyEntity(SurveyDTO surveyDTO, Survey surveyToUpdate) {
		surveyToUpdate.setDescription(surveyDTO.getDescription());
		surveyToUpdate.setName(surveyDTO.getName());
		surveyToUpdate.setReady(surveyDTO.getReady());
		Survey surveyUpdated = surveyToUpdate;
		surveyDTO.getQuestions().forEach(questiondto -> {
			Question currentQuestion = null;
			boolean isNew = false;
			try {
				currentQuestion = surveyUpdated.getQuestions().stream()
						.filter(question -> question.getCode().equals(questiondto.getCode())).findFirst().get();
			} catch (NoSuchElementException exception) {
				currentQuestion = new Question();
				currentQuestion.setId(UUID.randomUUID().toString().toUpperCase().replace("-", ""));
				currentQuestion.setCode(questiondto.getCode());
				isNew = true;
			}

			currentQuestion.setSurvey(surveyUpdated);
			currentQuestion.setDescription(questiondto.getDescription());
			currentQuestion.setInputtype(questiondto.getInputtype());
			currentQuestion.setName(questiondto.getName());
			currentQuestion.setRequired(questiondto.getRequired());
			currentQuestion.setText(questiondto.getText());
			try {
				currentQuestion.setAnswers(mapper.writeValueAsString(questiondto.getAnswers()));
			} catch (JsonProcessingException e) {
				LOGGER.error("Error while parsing the json in createNewVersionSurveyEntity %s :",e.getMessage());
			}
			if (isNew) {
				surveyUpdated.getQuestions().add(currentQuestion);
			}

		});
		return surveyUpdated;
	}

	/**
	 * create the response object
	 * 
	 * @param body
	 * @param status
	 * @return
	 */
	public static <T> BaseResponseDTO<T> wrapResponse(T body, HttpStatus status) {
		BaseResponseDTO<T> baseResponse = new BaseResponseDTO<>();
		baseResponse.setStatus(status);
		baseResponse.setBody(body);
		return baseResponse;
	}

	/**
	 * This method is used to create exception response
	 * 
	 * @param body
	 * @param message
	 * @param badRequest
	 * @return
	 */
	public static <T> BaseResponseDTO<T> wrapErrorResponse(T body, String message, HttpStatus badRequest) {
		BaseResponseDTO<T> baseResponse = new BaseResponseDTO<>();
		baseResponse.setStatus(badRequest);
		baseResponse.setBody(body);
		baseResponse.setError(message);
		return baseResponse;
	}

	public static <T> BaseResponseDTO<T> wrapErrorResponse(String message, HttpStatus badRequest) {
		BaseResponseDTO<T> baseResponse = new BaseResponseDTO<>();
		baseResponse.setStatus(badRequest);
		baseResponse.setBody(null);
		baseResponse.setError(message);
		return baseResponse;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void validateSession(String jwt, String code, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Auth-JWT", jwt);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://oracle-survey/auth/validate/session");
		builder.queryParam("userId", code);
		builder.queryParam("token", token);

		ResponseEntity<String> response;
		try {
			response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(headers),
					new ParameterizedTypeReference<String>() {
					});
			if (!"valid".equals(response.getBody())) {
				throw new AdminException("Invalid session");
			}
		} catch (Exception e) {
			if(! (e instanceof AdminException)){
				throw new AdminException("Login module call failed");
			} else {
				throw e;
			}
			
		}

	}

}
