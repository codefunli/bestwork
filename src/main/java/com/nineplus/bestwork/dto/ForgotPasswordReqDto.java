package com.nineplus.bestwork.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ForgotPasswordReqDto implements Serializable {

	/**
	 * DiepTT
	 */
	private static final long serialVersionUID = -6473445208447048668L;
	
	@NotBlank(message = "Enter your email.")
	@Email(message = "Invalid email.")
	private String email;
}
