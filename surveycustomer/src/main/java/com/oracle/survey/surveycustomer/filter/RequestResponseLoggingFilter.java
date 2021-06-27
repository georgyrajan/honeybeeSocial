package com.oracle.survey.surveycustomer.filter;

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

import com.oracle.survey.surveycustomer.constant.AppConstants;
import com.oracle.survey.surveycustomer.exception.CustomerException;
import com.oracle.survey.surveycustomer.util.SurveyCustomerAnswerUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
/**
*
* @author Georgy Rajan
* @version 1.0
* @since 2021-06-22
*/
@Order(1)
public class RequestResponseLoggingFilter implements Filter {
	private static final Logger LOGGER = LogManager.getLogger(RequestResponseLoggingFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		try {
			LOGGER.info("Logging Request  {} : {}", req.getMethod(), req.getRequestURI());
			String jwt = req.getHeader(AppConstants.AUTH_JWT);
			String url = req.getRequestURI();
			if (!ObjectUtils.isEmpty(jwt)) {
				Jws<Claims> jwtClaims = SurveyCustomerAnswerUtils.validateJWT(jwt);
				String roles = jwtClaims.getBody().get(AppConstants.ROLES).toString();
				String token = jwtClaims.getBody().get(AppConstants.TOKEN).toString();
				String userId = jwtClaims.getBody().get("userId").toString();
				SurveyCustomerAnswerUtils.validateSession(jwt,userId,token);
				MDC.put(AppConstants.MCDTOKENKEY, token);
				if (roles.contains(AppConstants.ROLE_CUSTOMER)) {
					chain.doFilter(request, response);
				} else if (roles.contains(AppConstants.ROLE_BA) && url.contains("/analytics/all")) {
					chain.doFilter(request, response);
				}  else {
					SurveyCustomerAnswerUtils.setResponseBody(res, "Please contact Admin, you dont have access to this resource", 401);
					return;
				}
			} else if (url.contains(AppConstants.CREATE_URL)) {
				chain.doFilter(request, response);
			} else {
				SurveyCustomerAnswerUtils.setResponseBody(res, "Please contact Admin, you dont have access to this resource", 401);
				return;
			}
			LOGGER.info("Logging Response :{}", res.getContentType());
		} catch (CustomerException e) {
			SurveyCustomerAnswerUtils.setResponseBody(res, e.getMessage(), 401);
			return;
		} catch (ExpiredJwtException ex) {
			SurveyCustomerAnswerUtils.setResponseBody(res, "Invalid session please log in", 401);
			return;
		} finally {
			MDC.remove(AppConstants.MCDTOKENKEY);

		}

	}

}