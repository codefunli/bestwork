package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CompanyUserReqDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5118509427317719306L;

	@JsonProperty("company")
	private CompanyReqDto company;

	@JsonProperty("user")
	private UserCompanyReqDto user;

}
