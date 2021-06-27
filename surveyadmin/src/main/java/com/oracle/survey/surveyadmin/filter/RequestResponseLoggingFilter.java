package com.oracle.survey.surveyadmin.filter;

import java.io.IOException;
import java.io.PrintWriter;

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
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.survey.surveyadmin.dto.BaseResponseDTO;
import com.oracle.survey.surveyadmin.exception.AdminException;
import com.oracle.survey.surveyadmin.exception.GlobalExceptionHandler;
import com.oracle.survey.surveyadmin.util.SurveyAdminUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * This is the filter class used to filter request and do the needed
 * authentication and validation
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
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
			String url = req.getRequestURI();
			if (!ObjectUtils.isEmpty(jwt)) {
				Jws<Claims> jwtClaims = SurveyAdminUtils.validateJWT(jwt);
				String roles = jwtClaims.getBody().get("roles").toString();
				String token = jwtClaims.getBody().get("token").toString();
				String userId = jwtClaims.getBody().get("userId").toString();
				SurveyAdminUtils.validateSession(jwt,userId,token);
				MDC.put(mdcTokenKey, token);
				if (roles.contains("ROLE_ADMIN")) {
					chain.doFilter(request, response);
				} else if (url.contains("/survey") && roles.contains("ROLE_CUSTOMER")) {
					chain.doFilter(request, response);
				} else if (url.contains("/details")) {
					chain.doFilter(request, response);
				} else {
					sendErrorResponse(res, "Please contact Admin, you dont have access to this resource", HttpStatus.UNAUTHORIZED);
					return;
				}
			} else if (url.contains("/details")) {
				chain.doFilter(request, response);
			} else {
				sendErrorResponse(res, "Invalid session please log in", HttpStatus.UNAUTHORIZED);
				return;
			}
			LOGGER.info("Logging Response :{}", res.getContentType());
		} catch (AdminException e) {
			sendErrorResponse(res, e.getMessage(), HttpStatus.UNAUTHORIZED);
		} finally {
			MDC.remove(mdcTokenKey);
		}

	}

	private void sendErrorResponse(HttpServletResponse res, String message, HttpStatus status)
			throws IOException {
		res.setStatus(status.value());
		res.setContentType("application/json");
		BaseResponseDTO<Object> responseObj = GlobalExceptionHandler.wrapErrorResponse(null, message, status);
		ObjectMapper mapper = new ObjectMapper();
		PrintWriter out = res.getWriter();
		out.print(mapper.writeValueAsString(responseObj));
		out.flush();
	}

}