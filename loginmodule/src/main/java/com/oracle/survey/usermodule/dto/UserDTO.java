package com.oracle.survey.usermodule.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
@Data
public class UserDTO {

	@NotEmpty(message = "User id is required")
	private String userid;
	@NotEmpty(message = "First name is required")
	private String firstname;
	@NotEmpty(message = "Last name is required")
	private String lastname;
	@NotEmpty(message = "Email is required")
	private String email;
	@NotEmpty(message = "Password is required")
	private String password;
	@NotEmpty(message = "Mobile is required")
	private String mobile;
	
	private String token;
	@NotEmpty(message = "Roles is required")
	private List<RoleDTO> roles = new ArrayList<>();

}
