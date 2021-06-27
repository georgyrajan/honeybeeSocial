package com.oracle.survey.usermodule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.survey.usermodule.config.Loggable;
import com.oracle.survey.usermodule.dto.LoginRequestDTO;
import com.oracle.survey.usermodule.exception.UserException;
import com.oracle.survey.usermodule.service.LoginService;

/**
 * Rest contoller to handle the login related flow
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
@RestController
public class LoginController {

	@Autowired
	LoginService loginService;

	/**
	 * Login authentication happen here which return jwt
	 * 
	 * @param loginRequest
	 * @return
	 */
	@PostMapping("/login")
	public String getSignup(@RequestBody LoginRequestDTO loginRequest) {
		return loginService.authenticate(loginRequest);
	}

	/**
	 * session is removed in logout
	 * 
	 * @param userId
	 * @param jwt
	 * @return
	 */
	@Loggable
	@PostMapping(value = "/logout")
	public String logoutUser(@RequestParam String userId, @RequestHeader(name = "Auth-JWT") String jwt) {
		loginService.logout(userId);
		return "redirect:/login";
	}

	/**
	 * check if the session exist
	 * 
	 * @param userId
	 * @param token
	 * @return
	 */
	@Loggable
	@GetMapping(value = "/validate/session")
	public ResponseEntity<String> validateSession(@RequestParam String userId, @RequestParam String token,
			@RequestHeader(name = "Auth-JWT") String jwt) {

		try {
			loginService.validateUserSession(userId, token);
		} catch (UserException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}

		return new ResponseEntity<>("valid", HttpStatus.OK);

	}
}
