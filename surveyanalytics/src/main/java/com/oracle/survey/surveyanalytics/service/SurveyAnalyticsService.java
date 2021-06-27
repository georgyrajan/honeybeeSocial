package com.oracle.survey.surveyanalytics.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.oracle.survey.surveyanalytics.dto.BaseResponseDTO;
import com.oracle.survey.surveyanalytics.dto.SurveysCompletionDataResponseDTO;
import com.oracle.survey.surveyanalytics.exception.AnalyticsException;
import com.oracle.survey.surveyanalytics.util.SurveyAnalyticsUtils;

/**
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
@Service
public class SurveyAnalyticsService {
	private static final Logger LOGGER = LogManager.getLogger(SurveyAnalyticsService.class);
	/**
	 * The method call customer application to get the current count of
	 * completed and in progress
	 * 
	 * @param jwt
	 * @return
	 */
	@Value("${app.survey.customer.base.url}")
	String url;

	public BaseResponseDTO<SurveysCompletionDataResponseDTO> getAllDataReport(String jwt) {
		BaseResponseDTO<SurveysCompletionDataResponseDTO> surveysCompletionDataResponseDTO = null;
		surveysCompletionDataResponseDTO = callCustomerAppAndGetStatus(jwt, url);
		return surveysCompletionDataResponseDTO;
	}

	private BaseResponseDTO<SurveysCompletionDataResponseDTO> callCustomerAppAndGetStatus(String jwt, String url) {
		BaseResponseDTO<SurveysCompletionDataResponseDTO> surveysCompletionDataResponseDTO;
		try {
			Map<String, Object> params = new HashMap<>();
			Map<String, String> headers = new HashMap<>();
			headers.put("Accept", "application/json");
			headers.put("Auth-JWT", jwt);

			surveysCompletionDataResponseDTO = SurveyAnalyticsUtils.callRestTemplate(params, headers, HttpMethod.GET,
					url, new ParameterizedTypeReference<BaseResponseDTO<SurveysCompletionDataResponseDTO>>() {
					});
		} catch (Exception e) {
			LOGGER.error("Error occured while call customer app : %s" , e.getMessage());
			throw new AnalyticsException("Error while processing data please try later");
		}
		return surveysCompletionDataResponseDTO;
	}

}
