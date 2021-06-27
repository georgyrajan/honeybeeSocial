package com.oracle.survey.usermodule.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class RoleDTO {

	private String id;
	
	@NotEmpty(message = "Roles Name required")
	private String name;
	
}
