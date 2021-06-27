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
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.oracle.survey.surveyui.util.dto.AdminViewDTO;
import com.oracle.survey.surveyui.util.dto.BaseResponse;
import com.oracle.survey.surveyui.util.dto.CustomerAnswerDTO;
import com.oracle.survey.surveyui.util.dto.LoginRequestDTO;
import com.oracle.survey.surveyui.util.dto.LogoutRequestDTO;
import com.oracle.survey.surveyui.util.dto.SurveyDTO;
import com.oracle.survey.surveyui.util.dto.SurveysCompletionDataResponseDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SurveyUIUtils {

	static RestTemplate restTemplate;

	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		SurveyUIUtils.restTemplate = restTemplate;
	}

	static String secret;

	@Value("${app.survey.jwt.secret}")
	public void setSecret(String value) {
		SurveyUIUtils.secret = value;
	}
	
	static String baseurl;

	@Value("${app.survey.baseurl}")
	public void setBaseurl(String baseurl) {
		SurveyUIUtils.baseurl = baseurl;
	}
	

	public static Jws<Claims> validateJWT(String jwtString) {

		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
		Jws<Claims> jwt = null;
		try {
			jwt = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(jwtString);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jwt;
	}

	public static CustomerAnswerDTO getCustomerPageDetails(String code, String version, Jws<Claims> claims,
			HttpHeaders headers) {
		headers.add("Accept", "application/json");
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseurl+"customer/customer/answer/survey");
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
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseurl+"customer/customer/answer/list");
		builder.queryParam("userId", claims.getBody().get("userId").toString());
		ResponseEntity<BaseResponse< List<CustomerAnswerDTO>>> customerAnswerResp = restTemplate.exchange(
				builder.toUriString(), HttpMethod.GET, new HttpEntity(headers),
				new ParameterizedTypeReference<BaseResponse< List<CustomerAnswerDTO>>>() {
				});
		return customerAnswerResp.getBody().getBody();
	}

	public static  ModelAndView getAnalystPageDetails(HttpHeaders headers) {
		ModelAndView modelAndView;
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseurl+"analyst/report");
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
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseurl+"admin/survey/urlmap");
		modelAndView = new ModelAndView("adminview");
		ResponseEntity<BaseResponse<List<AdminViewDTO>>> urlDetails = restTemplate.exchange(builder.toUriString(),
				HttpMethod.GET, new HttpEntity(headers),
				new ParameterizedTypeReference<BaseResponse<List<AdminViewDTO>>>() {
				});
		modelAndView.addObject("urlDetails", urlDetails.getBody().getBody());
		return modelAndView;
	}
	
	public static ModelAndView userLogout(LogoutRequestDTO logoutRequestDTO) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Auth-JWT", logoutRequestDTO.getJwt());
		restTemplate.postForObject(baseurl+"auth/logout?userId="+logoutRequestDTO.getUserId(),new HttpEntity(headers) , String.class);
		return new ModelAndView("home");
	}
	
	
	public static  String callUserLogin(LoginRequestDTO loginRequestDTO) {
		String jwt;
		jwt = restTemplate.postForObject(baseurl+"auth/login", loginRequestDTO, String.class);
		return jwt;
	}
	
	public static void findAndCallCustomerService(CustomerAnswerDTO customerAnswerDTO, String jwt, HttpHeaders headers,
			HttpMethod method) {
		UriComponentsBuilder builder;
		if (ObjectUtils.isEmpty(jwt)) {
			builder = UriComponentsBuilder.fromHttpUrl(baseurl+"customer/customer/answer/create");
		} else if (!customerAnswerDTO.getIsComplete()
				|| (customerAnswerDTO.getIsComplete() && !customerAnswerDTO.getIsFirstTime())) {
			builder = UriComponentsBuilder.fromHttpUrl(baseurl+"customer/customer/answer/update");
			method = HttpMethod.PUT;

		} else {
			builder = UriComponentsBuilder.fromHttpUrl(baseurl+"customer/customer/answer/create");
		}

		HttpEntity<CustomerAnswerDTO> requestEntity = new HttpEntity<>(customerAnswerDTO, headers);
		restTemplate.exchange(
				builder.toUriString(), method, requestEntity,
				new ParameterizedTypeReference<BaseResponse<CustomerAnswerDTO>>() {
				});
	}
	
	
	public static ResponseEntity<BaseResponse<SurveyDTO>> callAdminSurvey(String code, String version) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseurl+"admin/survey/details");
		builder.queryParam("code", code);
		builder.queryParam("version", version);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		ResponseEntity<BaseResponse<SurveyDTO>> resp = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				new HttpEntity(headers), new ParameterizedTypeReference<BaseResponse<SurveyDTO>>() {
				});
		return resp;
	}
	
}
