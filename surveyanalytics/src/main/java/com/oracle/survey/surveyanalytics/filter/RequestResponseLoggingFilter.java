package com.oracle.survey.surveyanalytics.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.util.ObjectUtils;

import com.oracle.survey.surveyanalytics.exception.AnalyticsException;
import com.oracle.survey.surveyanalytics.util.SurveyAnalyticsUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Order(1)
public class RequestResponseLoggingFilter implements Filter {
	private static final Logger LOGGER = LogManager.getLogger(RequestResponseLoggingFilter.class);

	private String mdcTokenKey = "Slf4jMDCFilter.UUID";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		try {
			LOGGER.info("Logging Request  {} : {}", req.getMethod(), req.getRequestURI());
			String jwt = req.getHeader("Auth-JWT");
			if (!ObjectUtils.isEmpty(jwt)) {
				Jws<Claims> jwtClaims = SurveyAnalyticsUtils.validateJWT(jwt);
				String roles = jwtClaims.getBody().get("roles").toString();
				String token = jwtClaims.getBody().get("token").toString();
				MDC.put(mdcTokenKey, token);
				if (roles.contains("ROLE_BA")) {
					chain.doFilter(request, response);
				} else {
					throw new AnalyticsException("Please contact Admin, you dont have access to this resource");
				}
			} else {
				throw new AnalyticsException("Invalid session please log in");
			}
			LOGGER.info("Logging Response :{}", res.getContentType());
		} finally {
			MDC.remove(mdcTokenKey);
		}

	}

}