package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CompanyBriefResDto extends BaseDto {
	/**
	* 
	*/
	private static final long serialVersionUID = -4011837442800319636L;

	@JsonProperty("id")
	private long id;

	@JsonProperty("companyName")
	private String companyName;

}
