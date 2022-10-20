package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserResDto {
	
	@JsonProperty("id")
    private long id;
	
	@JsonProperty("userName")
    private String userName;

	@JsonProperty("firstNm")
    private String firstNm;

	@JsonProperty("lastNm")
    private String lastNm;

	@JsonProperty("uEmail")
    private String email;

	@JsonProperty("isEnable")
    private int isEnable;
	
	@JsonProperty("uTelNo")
    private String telNo;

	@JsonProperty("role")
    private String role;

}
