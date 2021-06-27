package com.oracle.survey.usermodule.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class LoginRequestDTO {
	@NotEmpty(message = "User name required")
    private String userName;
	@NotEmpty(message = "Password required")
    private String password;
}
