package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompanyUserResDto {
	
	@JsonProperty("company")
    private CompanyResDto company;

    @JsonProperty("user")
    private UserResDto user;
}
