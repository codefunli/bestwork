package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserReqDTO extends BaseDTO {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8012401348414743244L;

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("userId")
	private String userId;

	@JsonProperty("userName")
	private String userName;

	@JsonProperty("password")
	private String password;

	@JsonProperty("enabled")
	private Boolean enabled;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("email")
	private String email;

}
