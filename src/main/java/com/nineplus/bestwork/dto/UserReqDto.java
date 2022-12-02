package com.nineplus.bestwork.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserReqDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1726932600859444676L;

	@NotBlank(message = "Enter username.")
	@JsonProperty("userName")
	private String userName;

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
	private boolean enabled;

	@JsonProperty("role")
	private long role;

	@JsonProperty("avatar")
	private String avatar;

	@JsonProperty("updateDate")
	private String updateDate;

	@JsonProperty("company")
	private long company;

}
