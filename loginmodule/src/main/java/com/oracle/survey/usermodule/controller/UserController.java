package com.oracle.survey.usermodule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.survey.usermodule.config.Loggable;
import com.oracle.survey.usermodule.dto.UserDTO;
import com.oracle.survey.usermodule.service.UserService;
/**
 *Controller to handle all the user flow
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
@RequestMapping("/user")
@RestController
public class UserController 
{
	
	@Autowired
	UserService userService;
	
	/**
	 * method is used to create a user
	 * @param user
	 * @param jwt
	 * @return
	 */
	@Loggable
	@PostMapping("/create")
	public UserDTO createUser(@RequestBody UserDTO user,@RequestHeader(name="Auth-JWT") String jwt)
	{
		return userService.createUser(user);
	}
	
	/**
	 * Method is used to update user
	 * @param user
	 * @param jwt
	 * @return
	 */
	@Loggable
	@PutMapping("/update")
	public UserDTO updateUser(@RequestBody UserDTO user,@RequestHeader(name="Auth-JWT") String jwt)
	{
		return userService.updateUser(user);
	}
	
	/**
	 * method list all the users
	 * @param jwt
	 * @return
	 */
	@Loggable
	@GetMapping("/listall")
	public List<UserDTO> getAlluser(@RequestHeader(name="Auth-JWT") String jwt)
	{
		return userService.getUsersList();
	}
	
}
