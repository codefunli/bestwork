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

	@JsonProperty("userName")
	private String userName;

	@JsonProperty("password")
	private String password;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

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
