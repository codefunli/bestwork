package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CompanyUserReqDTO {
	
	 @JsonProperty("company")
	    private CompanyReqDto company;

	 @JsonProperty("user")
	    private UserReqDTO user;

}
