package com.nineplus.bestwork.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserReqDto extends BaseDto {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8012401348414743244L;

	@NotBlank(message = "Enter username.")
	@JsonProperty("userName")
	private String userName;

	@NotBlank(message = "Enter password.")
//	@Pattern(regexp = "[0-9]{6,10}", message = "Enter from 6 to 10 digits.")
	@JsonProperty("password")
	private String password;

	@NotBlank(message = "Enter first name.")
	@JsonProperty("firstName")
	private String firstName;

	@NotBlank(message = "Enter last name.")
	@JsonProperty("lastName")
	private String lastName;

	@NotBlank(message = "Enter Email.")
	@Email(message = "Enter valid email")
	@JsonProperty("uEmail")
	private String email;

	@JsonProperty("uTelNo")
	private String telNo;
	
	@JsonProperty("enabled")
	private int enabled;

	@JsonProperty("role")
	private long role;

	@JsonProperty("avatar")
	private String avatar;

	@JsonProperty("updateDate")
	private String updateDate;

	@JsonProperty("company")
	private String company;

}
