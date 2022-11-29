package com.nineplus.bestwork.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author DiepTT
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ForgotPasswordReqDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3586819945558785378L;

	@NotBlank(message = "Enter your email.")
	@Email(message = "Invalid email.")
	private String email;
}
