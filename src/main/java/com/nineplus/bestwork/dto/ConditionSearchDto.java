package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ConditionSearchDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6601533301694706803L;
	@JsonProperty("id")
	Long id;

	@JsonProperty("name")
	String name;

	@JsonProperty("roleId")
	Long roleId;

	@JsonProperty("monitorId")
	Long monitorId;
}
