package com.oracle.survey.surveyui.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.survey.surveyui.util.dto.BaseResponse;
import com.oracle.survey.surveyui.util.dto.CustomerAnswerDTO;
import com.oracle.survey.surveyui.util.dto.LoginRequestDTO;
import com.oracle.survey.surveyui.util.dto.LogoutRequestDTO;
import com.oracle.survey.surveyui.util.dto.SurveyDTO;
import com.oracle.survey.surveyui.utils.SurveyUIUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
@RestController
public class ViewResolver {

	static RestTemplate restTemplate;

	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		ViewResolver.restTemplate = restTemplate;
	}

	@GetMapping(value = "/home")
	public ModelAndView home(@RequestParam(required = false) String code,
			@RequestParam(required = false) String version) {
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("code", code);
		modelAndView.addObject("version", version);
		return modelAndView;
	}

	@GetMapping(value = "/signup")
	public ModelAndView signup() {
		ModelAndView modelAndView = new ModelAndView("signup");
		return modelAndView;
	}

	@GetMapping(value = "/done")
	public ModelAndView done() {
		ModelAndView modelAndView = new ModelAndView("done");
		return modelAndView;
	}

	@PostMapping(value = "/logout")
	public ModelAndView logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {
		ModelAndView modelAndView = SurveyUIUtils.userLogout(logoutRequestDTO);
		return modelAndView;
	}


	
	@PostMapping(value = "/login", produces = { "application/json", "application/xml" }, consumes = {
			"application/x-www-form-urlencoded;charset=UTF-8" })
	public ModelAndView login(LoginRequestDTO loginRequestDTO, @RequestParam String code,
			@RequestParam String version) {
		ModelAndView modelAndView = null;
		Jws<Claims> claims = null;
		ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		String jwt = "";
		try {
			jwt = SurveyUIUtils.callUserLogin(loginRequestDTO);
		} catch (Exception e1) {
			modelAndView = new ModelAndView("home");
			modelAndView.addObject("error", "Invalid user name or password");
			return modelAndView;
		}
		claims = SurveyUIUtils.validateJWT(jwt);
		headers.add("Auth-JWT", jwt);

		String roles = claims.getBody().get("roles").toString();
		if (roles.contains("ADMIN")) {
			modelAndView = SurveyUIUtils.getAdminPageDetails(headers);
			modelAndView.addObject("AuthJWT", jwt);
			modelAndView.addObject("userId",  claims.getBody().get("userId").toString());
			return modelAndView;
		} else if (roles.contains("BA")) {
			modelAndView = SurveyUIUtils.getAnalystPageDetails(headers);
			modelAndView.addObject("AuthJWT", jwt);
			modelAndView.addObject("userId",  claims.getBody().get("userId").toString());
			return modelAndView;
		}
		
		if(ObjectUtils.isEmpty(code) && ObjectUtils.isEmpty(version ) && roles.contains("CUSTOMER")){
			List<CustomerAnswerDTO> customerAnswerList = SurveyUIUtils.getCustomerViewPageDetails(claims, headers);
			modelAndView = new ModelAndView("customerview"); 
			modelAndView.addObject("AuthJWT", jwt);
			modelAndView.addObject("userId",  claims.getBody().get("userId").toString());
			modelAndView.addObject("customerAnswerList",customerAnswerList);
			return modelAndView;
		}

		CustomerAnswerDTO customerAnswerDTO = SurveyUIUtils.getCustomerPageDetails(code, version, claims, headers);
		if (!customerAnswerDTO.getIsComplete()) {
			try {
				modelAndView = new ModelAndView("loginsurvey");
				modelAndView.addObject("AuthJWT", jwt);
				modelAndView.addObject("customerAnswerStr", mapper.writeValueAsString(customerAnswerDTO));
				modelAndView.addObject("customerAnswerDTO", customerAnswerDTO);
				modelAndView.addObject("questions", customerAnswerDTO.getSurveyDetails().getQuestions());
				return modelAndView;
			} catch (Exception e) {
				modelAndView = new ModelAndView("home"); // CustomerAnswerDTO.class);
				modelAndView.addObject("error", "Invalid user name or password");
				return modelAndView;
			}
		} else {
			modelAndView = new ModelAndView("completed");
			modelAndView.addObject("message", customerAnswerDTO.getSurveyDetails().getName()
					+ " survey is already completed by you. Thank you!!");
			return modelAndView;
		}

	}

	

	@PostMapping(value = "/saveanswer")
	public ModelAndView save(@RequestBody CustomerAnswerDTO customerAnswerDTO,
			@RequestHeader(name = "Auth-JWT", required = false) String jwt) {
		ModelAndView modelAndView = new ModelAndView("loginsurvey");
		ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Auth-JWT", jwt);
		headers.add("requestId", "haskjHKSJhj");
		HttpMethod method = HttpMethod.POST;
		SurveyUIUtils.findAndCallCustomerService(customerAnswerDTO, jwt, headers, method);
		try {

			modelAndView.addObject("customerAnswerStr", mapper.writeValueAsString(customerAnswerDTO));
			modelAndView.addObject("customerAnswerDTO", customerAnswerDTO);
			modelAndView.addObject("questions", customerAnswerDTO.getSurveyDetails().getQuestions());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modelAndView;

	}

	

	@GetMapping(value = "/unauthsurvey")
	public ModelAndView unAuthSurvey(@RequestParam String code, @RequestParam String version) {
		ModelAndView modelAndView = new ModelAndView("anonymoussurvey");
		ObjectMapper mapper = new ObjectMapper();

		ResponseEntity<BaseResponse<SurveyDTO>> resp = SurveyUIUtils.callAdminSurvey(code, version);
		SurveyDTO survey = resp.getBody().getBody();
		CustomerAnswerDTO customerAnswerDTO = new CustomerAnswerDTO();
		customerAnswerDTO.setIsComplete(false);
		customerAnswerDTO.setIsFirstTime(true);
		customerAnswerDTO.setSurveyCode(survey.getCode());
		customerAnswerDTO.setSurveyDetails(survey);
		customerAnswerDTO.setSurveyVersion(Long.valueOf(version));
		customerAnswerDTO.setUserId("ANONYMOUS_USER");
		try {
			modelAndView.addObject("customerAnswerDTO", customerAnswerDTO);
			modelAndView.addObject("customerAnswerStr", mapper.writeValueAsString(customerAnswerDTO));
			modelAndView.addObject("questions", customerAnswerDTO.getSurveyDetails().getQuestions());
		} catch (JsonProcessingException e) {
			modelAndView = new ModelAndView("home");
			modelAndView.addObject("code", code);
			modelAndView.addObject("version", version);
			return modelAndView;
		}

		return modelAndView;

	}


}
