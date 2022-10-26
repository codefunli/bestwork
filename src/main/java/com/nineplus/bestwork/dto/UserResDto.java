package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.nineplus.bestwork.entity.TRole;
import lombok.Data;

@Data
public class UserResDto {
	
	@JsonProperty("id")
    private long id;
	
	@JsonProperty("userName")
    private String userName;

	@JsonProperty("firstName")
    private String firstName;

	@JsonProperty("lastName")
    private String lastName;

	@JsonProperty("uEmail")
    private String email;

	@JsonProperty("enable")
    private int enable;
	
	@JsonProperty("uTelNo")
    private String telNo;

	@JsonProperty("role")
    private TRole role;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("updateDate")
    private String updateDate;

}
