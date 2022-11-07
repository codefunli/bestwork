package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PrjConditionSearchDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2784166687865472982L;

	@JsonProperty("keyword")
	private String keyword;

	@JsonProperty("status")
	private Integer status;

}
