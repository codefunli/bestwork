package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PrjConditionSearchDTO {
	

	@JsonProperty("keyword")
	private String keyword;

	@JsonProperty("status")
	private Integer status;
	
}
