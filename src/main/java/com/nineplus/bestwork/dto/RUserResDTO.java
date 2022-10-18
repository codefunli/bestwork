package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RUserResDTO {
	
	@JsonProperty("userName")
    private String userName;

	@JsonProperty("firstNm")
    private String firstNm;

	@JsonProperty("lastNm")
    private String lastNm;

	@JsonProperty("email")
    private String email;

	@JsonProperty("isEnable")
    private boolean isEnable;
	
	@JsonProperty("uTelNo")
    private String telNo;

	@JsonProperty("role")
    private String role;

}
