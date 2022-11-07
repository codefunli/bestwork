package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.nineplus.bestwork.entity.CompanyEntity;
import com.nineplus.bestwork.entity.RoleEntity;
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

	@JsonProperty("enabled")
    private int isEnable;
	
	@JsonProperty("uTelNo")
    private String telNo;

	@JsonProperty("role")
    private RoleEntity role;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("updateDate")
    private String updateDate;

    @JsonProperty("countLoginFailed")
    private String countLoginFailed;

    @JsonProperty("company")
    CompanyEntity company;

}
