package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RCompanyUserResDTO {
	
	@JsonProperty("company")
    private CompanyResDto company;

    @JsonProperty("user")
    private RUserResDTO user;
}
