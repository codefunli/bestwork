package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CompanyUserResDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8429579747573510709L;

	@JsonProperty("company")
	private CompanyResDto company;

	@JsonProperty("user")
	private UserResDto user;
}
