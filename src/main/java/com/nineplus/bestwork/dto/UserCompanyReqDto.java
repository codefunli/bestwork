package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UserCompanyReqDto extends BaseDto {
	/**
	* 
	*/
	private static final long serialVersionUID = 1524915304620701462L;

	@JsonProperty("userName")
	private String userName;

	@JsonProperty("password")
	private String password;

	@JsonProperty("enabled")
	private boolean enabled;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("uEmail")
	private String email;

	@JsonProperty("uTelNo")
	private String telNo;

	@JsonIgnore
	private String createBy;

	@JsonIgnore
	private String updateBy;

}
