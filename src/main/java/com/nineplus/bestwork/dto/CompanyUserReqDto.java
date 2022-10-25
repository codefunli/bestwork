package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CompanyUserReqDto {
	
	 @JsonProperty("company")
	    private CompanyReqDto company;

	 @JsonProperty("user")
	    private UserReqDto user;

}
