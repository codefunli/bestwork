package com.nineplus.bestwork.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ResetPasswordReqDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@-_])(?=\\S)[a-zA-Z0-9@\\-_]{8,20}$", 
			message = "Enter from 8 to 20 characters, combine uppercase(s), lowercase(s), digit(s) and special character(s) (@-_).")
	private String password;
	

	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@-_])(?=\\S)[a-zA-Z0-9@\\-_]{8,20}$", 
			message = "Enter from 8 to 20 characters, combine uppercase(s), lowercase(s), digit(s) and special character(s) (@-_).")
	private String confirmPassword;

}
