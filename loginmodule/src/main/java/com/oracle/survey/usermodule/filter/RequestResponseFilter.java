package com.oracle.survey.usermodule.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.survey.usermodule.dto.BaseResponseDTO;
import com.oracle.survey.usermodule.exception.GlobalExceptionHandler;
import com.oracle.survey.usermodule.exception.UserException;
import com.oracle.survey.usermodule.service.LoginService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
/**
 * This is the Filter method to handle all the inflow request
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
@WebFilter(urlPatterns = { "/user/*", "/logout","/validate/session"})
@Order(1)
public class RequestResponseFilter implements Filter {
	private static final Logger LOGGER = LogManager.getLogger(RequestResponseFilter.class);
	
	@Autowired
	LoginService loginService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		LOGGER.info("Logging Request  {} : {}", req.getMethod(), req.getRequestURI());

		try {
			String jwt = req.getHeader("Auth-JWT");
			String uri = req.getRequestURI();
			if (!ObjectUtils.isEmpty(jwt)) {
				Jws<Claims> jwtClaims = LoginService.validateJWT(jwt);
				String roles = jwtClaims.getBody().get("roles").toString();
				String token = jwtClaims.getBody().get("token").toString();
				String userId = jwtClaims.getBody().get("userId").toString();
				if (uri.contains("/validate/session")) {
					chain.doFilter(request, response);
				} else {
					loginService.validateUserSession(userId, token);
					if (roles.contains("ROLE_ADMIN") || uri.contains("/logout")) {
						chain.doFilter(request, response);
					} else {
						sendErrorResponse(res, "Please contact Admin, you dont have access to this resource", HttpStatus.UNAUTHORIZED);
						return;
					}
				}
				
			} else if (uri.contains("/login")) {
				chain.doFilter(request, response);
			} else {
				sendErrorResponse(res, "Invalid session please log in", HttpStatus.UNAUTHORIZED);
				return;
			}
		} catch (UserException e) {
			sendErrorResponse(res, e.getMessage(), HttpStatus.BAD_REQUEST);
			return;
		}

		LOGGER.info("Logging Response :{}", res.getContentType());
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