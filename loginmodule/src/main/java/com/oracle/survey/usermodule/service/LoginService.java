package com.oracle.survey.usermodule.service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.oracle.survey.usermodule.dto.LoginRequestDTO;
import com.oracle.survey.usermodule.dto.UserDTO;
import com.oracle.survey.usermodule.entity.User;
import com.oracle.survey.usermodule.exception.UserException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
/**
 * Service class for login flow
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
@Service
public class LoginService {

	@Autowired
	UserService userService;
	
	static String secret;

	@Value("${app.survey.jwt.secret}")
	public void setSecret(String value) {
		LoginService.secret = value;
	}
	
	public String authenticate(LoginRequestDTO loginRequest) {
		User user = userService.findByUseridAndPassword(loginRequest.getUserName(),loginRequest.getPassword());
		if (ObjectUtils.isEmpty(user)) {
			throw new UserException("Invalid username or password");
		}
		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());

		Instant now = Instant.now();
		String token= UUID.randomUUID().toString().toUpperCase().replace("-", "");
		String jwtToken = Jwts.builder().claim("userId", user.getUserid()).claim("roles", user.getRoles())
				.claim("token", token)
				.setSubject("login").setId(UUID.randomUUID().toString()).setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plus(50l, ChronoUnit.MINUTES))).signWith(hmacKey).compact();
		user.setToken(token);
		userService.updateUser(user);
		return jwtToken;
	}

	public static Jws<Claims> validateJWT(String jwtString) {
		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
		Jws<Claims> jwt = null;
		try {
			jwt = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(jwtString);
		} catch (Exception e) {
			throw new UserException("Error while validating jwt");
		}
		return jwt;
	}

	public String logout(String userId) {
		User user = userService.getUserWithId(userId);
		if (ObjectUtils.isEmpty(user)) {
			throw new UserException("Invalid username or password");
		}
		user.setToken(null);
		userService.updateUser(user);
		return userId;
		
	}

	public void validateUserSession(String userId, String token) {
		UserDTO user = userService.getUserWithIdAndToken(userId,token);
		if (ObjectUtils.isEmpty(user)) {
			throw new UserException("Invalid session please log in");
		}
		
	}

}
