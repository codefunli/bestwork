package com.nineplus.bestwork.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ForgotPasswordReqDto {

	@NotBlank(message = "Enter your email.")
	@Email(message = "Invalid email.")
	private String email;
}
