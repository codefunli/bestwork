package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ConstructionStatusResDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1870982360279983747L;

	@JsonProperty("id")
	private int id;

	@JsonProperty("status")
	private String status;
}
