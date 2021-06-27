package com.oracle.survey.surveyanalytics.util;

import java.security.Key;
import java.util.Base64;
import java.util.Map;

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

import com.oracle.survey.surveyanalytics.dto.BaseResponseDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class SurveyAnalyticsUtils {
	
	private static final Logger LOGGER = LogManager.getLogger(SurveyAnalyticsUtils.class);
	
	static RestTemplate restTemplate;

	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		SurveyAnalyticsUtils.restTemplate = restTemplate;
	}

	static String secret;

	@Value("${app.survey.jwt.secret}")
	public void setSecret(String value) {
		SurveyAnalyticsUtils.secret = value;
	}

	public static Jws<Claims> validateJWT(String jwtString) {

		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
		Jws<Claims> jwt = null;
		try {
			jwt = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(jwtString);

		} catch (Exception e) {
			LOGGER.error("Error occured while validating jwt %s",e.getMessage());
		}
		return jwt;
	}

	public static <T> BaseResponseDTO<T> wrapResponse(T body, HttpStatus status) {
		BaseResponseDTO<T> baseResponse = new BaseResponseDTO<>();
		baseResponse.setStatus(status);
		baseResponse.setBody(body);
		return baseResponse;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T callRestTemplate(Map<String, Object> paramsMap, Map<String, String> headerMap,
			HttpMethod method, String url, ParameterizedTypeReference<T> parameterizedTypeReference) {
		HttpHeaders headers = new HttpHeaders();
		headerMap.forEach(headers::add);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		paramsMap.forEach(builder::queryParam);

		ResponseEntity<T> response = restTemplate.exchange(builder.toUriString(), method, new HttpEntity(headers),
				parameterizedTypeReference);
		return response.getBody();

	}

}
