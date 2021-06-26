package com.oracle.survey.surveyui.utils;

import java.security.Key;
import java.util.Base64;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.survey.surveyui.util.dto.AdminViewDTO;
import com.oracle.survey.surveyui.util.dto.BaseResponse;
import com.oracle.survey.surveyui.util.dto.CustomerAnswerDTO;
import com.oracle.survey.surveyui.util.dto.SurveysCompletionDataResponseDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class SurveyUIUtils {

	static RestTemplate restTemplate;
	private static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
			false);

	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		SurveyUIUtils.restTemplate = restTemplate;
	}

	static String secret;

	@Value("${app.survey.jwt.secret}")
	public void setSecret(String value) {
		SurveyUIUtils.secret = value;
	}

	public static Jws<Claims> validateJWT(String jwtString) {

		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
		Jws<Claims> jwt = null;
		try {
			jwt = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(jwtString);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jwt;
	}

	public static CustomerAnswerDTO getCustomerPageDetails(String code, String version, Jws<Claims> claims,
			HttpHeaders headers) {
		headers.add("Accept", "application/json");
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:7777/customer/answer/survey");
		builder.queryParam("code", code);
		builder.queryParam("version", version);
		builder.queryParam("userId", claims.getBody().get("userId").toString());
		ResponseEntity<BaseResponse<CustomerAnswerDTO>> customerAnswerResp = restTemplate.exchange(
				builder.toUriString(), HttpMethod.GET, new HttpEntity(headers),
				new ParameterizedTypeReference<BaseResponse<CustomerAnswerDTO>>() {
				});
		CustomerAnswerDTO customerAnswerDTO = customerAnswerResp.getBody().getBody();
		return customerAnswerDTO;
	}
	
	public static List<CustomerAnswerDTO> getCustomerViewPageDetails(Jws<Claims> claims,
			HttpHeaders headers) {
		headers.add("Accept", "application/json");
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:7777/customer/answer/list");
		builder.queryParam("userId", claims.getBody().get("userId").toString());
		ResponseEntity<BaseResponse< List<CustomerAnswerDTO>>> customerAnswerResp = restTemplate.exchange(
				builder.toUriString(), HttpMethod.GET, new HttpEntity(headers),
				new ParameterizedTypeReference<BaseResponse< List<CustomerAnswerDTO>>>() {
				});
		 List<CustomerAnswerDTO> customerAnswerList = customerAnswerResp.getBody().getBody();
		return customerAnswerList;
	}

	public static  ModelAndView getAnalystPageDetails(HttpHeaders headers) {
		ModelAndView modelAndView;
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:9999/report");
		modelAndView = new ModelAndView("analystview");
		ResponseEntity<BaseResponse<SurveysCompletionDataResponseDTO>> reportDetails = restTemplate.exchange(
				builder.toUriString(), HttpMethod.GET, new HttpEntity(headers),
				new ParameterizedTypeReference<BaseResponse<SurveysCompletionDataResponseDTO>>() {
				});
		modelAndView.addObject("reportDetails", reportDetails.getBody().getBody());
		return modelAndView;
	}

	public static ModelAndView getAdminPageDetails(HttpHeaders headers) {
		ModelAndView modelAndView;
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:5555/survey/urlmap");
		modelAndView = new ModelAndView("adminview");
		ResponseEntity<BaseResponse<List<AdminViewDTO>>> urlDetails = restTemplate.exchange(builder.toUriString(),
				HttpMethod.GET, new HttpEntity(headers),
				new ParameterizedTypeReference<BaseResponse<List<AdminViewDTO>>>() {
				});
		modelAndView.addObject("urlDetails", urlDetails.getBody().getBody());
		return modelAndView;
	}
}
