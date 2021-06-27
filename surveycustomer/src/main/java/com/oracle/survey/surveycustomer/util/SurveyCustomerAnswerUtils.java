package com.oracle.survey.surveycustomer.util;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.survey.surveycustomer.dto.BaseResponseDTO;
import com.oracle.survey.surveycustomer.dto.CountDTO;
import com.oracle.survey.surveycustomer.dto.CustomerAnswerDTO;
import com.oracle.survey.surveycustomer.dto.SurveyDTO;
import com.oracle.survey.surveycustomer.dto.SurveySpecificDetailsDTO;
import com.oracle.survey.surveycustomer.dto.SurveysCompletionDataResponseDTO;
import com.oracle.survey.surveycustomer.entity.CustomerAnswer;
import com.oracle.survey.surveycustomer.exception.CustomerException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
@Component
public class SurveyCustomerAnswerUtils {

	private static final Logger LOGGER = LogManager.getLogger(SurveyCustomerAnswerUtils.class);

	static RestTemplate restTemplate;

	private static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
			false);

	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		SurveyCustomerAnswerUtils.restTemplate = restTemplate;
	}

	static String secret;

	@Value("${app.survey.jwt.secret}")
	public void setSecret(String value) {
		SurveyCustomerAnswerUtils.secret = value;
	}

	/**
	 * Validate jwt
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
			throw new CustomerException("Invalid session please log in");
		}
		return jwt;
	}

	/**
	 * convert from customer answer dto to entity object
	 * 
	 * @param customerAnswerDTO
	 * @return
	 */
	public static CustomerAnswer createCustomerAnswerEntity(CustomerAnswerDTO customerAnswerDTO) {
		CustomerAnswer customerAnswer = new CustomerAnswer();
		customerAnswer.setId(UUID.randomUUID().toString().toUpperCase().replace("-", ""));
		customerAnswer.setSurveyCode(customerAnswerDTO.getSurveyCode());
		customerAnswer.setSurveyVersion(customerAnswerDTO.getSurveyVersion());
		customerAnswer.setUserId(customerAnswerDTO.getUserId());
		customerAnswer.setIsComplete(customerAnswerDTO.getIsComplete());
		customerAnswer.setIsFirstTime(false);
		try {
			customerAnswer.setUserAnswerInfo(mapper.writeValueAsString(customerAnswerDTO.getSurveyDetails()));
		} catch (JsonProcessingException e) {
			LOGGER.error("Error while converting your answer : %s" , e.getMessage());
			throw new CustomerException("Error while converting your answer");
		}

		return customerAnswer;
	}

	/**
	 * create dto from entity object
	 * 
	 * @param customerAnswer
	 * @return
	 */
	public static CustomerAnswerDTO createCustomerAnswerDTO(CustomerAnswer customerAnswer) {
		CustomerAnswerDTO customerAnswerDTO = new CustomerAnswerDTO();
		customerAnswerDTO.setSurveyCode(customerAnswer.getSurveyCode());
		customerAnswerDTO.setSurveyVersion(customerAnswer.getSurveyVersion());
		customerAnswerDTO.setUserId(customerAnswer.getUserId());
		customerAnswerDTO.setIsComplete(customerAnswer.getIsComplete());
		customerAnswerDTO.setIsFirstTime(customerAnswer.getIsComplete());
		try {
			customerAnswerDTO.setSurveyDetails(mapper.readValue(customerAnswer.getUserAnswerInfo(), SurveyDTO.class));
		} catch (JsonProcessingException e) {
			LOGGER.error("Error while converting your answer : %s" , e.getMessage());
			throw new CustomerException("Error while converting your answer");
		}

		return customerAnswerDTO;
	}

	/**
	 * @param res
	 * @param message
	 * @param code
	 * @throws IOException
	 */
	public static void setResponseBody(HttpServletResponse res, String message, int code) throws IOException {
		res.setStatus(code);
		String responseToClient = message;
		res.getWriter().write(responseToClient);
		res.getWriter().flush();
	}

	/**
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
	 * @param customerAnswer
	 * @return
	 */
	public static List<SurveySpecificDetailsDTO> convertToCustomerAnswerDTOList(List<Object[]> customerAnswer) {
		List<SurveySpecificDetailsDTO> listResult = new ArrayList<>();
		customerAnswer.stream().forEach(current -> {
			SurveySpecificDetailsDTO surveySpecificDetailsDTO = new SurveySpecificDetailsDTO();
			surveySpecificDetailsDTO.setCode(current[1].toString());
			surveySpecificDetailsDTO.setVersion((Long) current[2]);
			listResult.add(surveySpecificDetailsDTO);
		});
		return listResult;
	}

	/**
	 * @param customerAnswer
	 * @return
	 */
	public static SurveysCompletionDataResponseDTO convertToSurveysCompletionDataResponseDTO(
			List<Object[]> customerAnswer) {
		SurveysCompletionDataResponseDTO surveysCompletionDataResponseDTO = new SurveysCompletionDataResponseDTO();
		Map<String, SurveySpecificDetailsDTO> surveyMap = new HashMap<>();
		customerAnswer.stream().forEach(current -> {
			String key = current[1].toString() + current[2].toString();
			if (surveyMap.containsKey(key)) {
				CountDTO count = surveyMap.get(key).getCountDTO();
				checkAndIncrement((Boolean) current[3], count);
			} else {
				SurveySpecificDetailsDTO surveySpecificDetailsDTO = new SurveySpecificDetailsDTO();
				surveySpecificDetailsDTO.setCode(current[1].toString());
				surveySpecificDetailsDTO.setVersion((Long) current[2]);

				CountDTO count = new CountDTO();
				checkAndIncrement((Boolean) current[3], count);
				surveySpecificDetailsDTO.setCountDTO(count);
				surveyMap.put(key, surveySpecificDetailsDTO);
			}
		});
		if (!ObjectUtils.isEmpty(surveyMap)) {
			surveysCompletionDataResponseDTO.setListResult(surveyMap.values().stream().collect(Collectors.toList()));
		}
		return surveysCompletionDataResponseDTO;
	}

	/**
	 * @param current
	 * @param count
	 */
	private static void checkAndIncrement(boolean current, CountDTO count) {
		if (current) {
			count.setCompleted(count.getCompleted() + 1);
		} else {
			count.setInprogress(count.getInprogress() + 1);
		}
	}

	/**
	 * @param paramsMap
	 * @param headerMap
	 * @param method
	 * @param url
	 * @param parameterizedTypeReference
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T callRestTemplate(Map<String, Object> paramsMap, Map<String, String> headerMap,
			HttpMethod method, String url, ParameterizedTypeReference<T> parameterizedTypeReference) {
		HttpHeaders headers = new HttpHeaders();
		headerMap.forEach(headers::add);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		paramsMap.forEach(builder::queryParam);

		ResponseEntity<T> response = restTemplate.exchange(builder.toUriString(), method,
				new HttpEntity(headers), parameterizedTypeReference);
		return response.getBody();

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

	/**
	 * @param jwt
	 * @param code
	 * @param token
	 */
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
				throw new CustomerException("Invalid session please log in");
			}
		} catch (Exception e) {
			if (!(e instanceof CustomerException)) {
				throw new CustomerException("Login module call failed");
			} else {
				throw e;
			}

		}

	}

	/**
	 * @param code
	 * @param version
	 * @param jwt
	 * @param url
	 * @return
	 */
	@Cacheable(value = "cacheSurvey")
	public BaseResponseDTO<SurveyDTO> callAdminModuleAndGetSurvey(String code, Long version, String jwt, String url) {
		BaseResponseDTO<SurveyDTO> survey = new BaseResponseDTO<>();
		Map<String, Object> params = new HashMap<>();
		params.put("code", code);
		params.put("version", version);
		Map<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json");
		headers.put("Auth-JWT", jwt);

		try {
			survey = SurveyCustomerAnswerUtils.callRestTemplate(params, headers, HttpMethod.GET, url,
					new ParameterizedTypeReference<BaseResponseDTO<SurveyDTO>>() {
					});
		} catch (Exception e) {
			throw new CustomerException(e.getMessage());
		}
		return survey;
	}
}
