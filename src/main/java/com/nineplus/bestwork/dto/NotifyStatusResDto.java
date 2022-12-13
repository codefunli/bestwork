package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NotifyStatusResDto extends BaseDto {
	/**
	* 
	*/
	private static final long serialVersionUID = -879326725514934702L;
	@JsonProperty("id")
	private int id;

	@JsonProperty("status")
	private String status;
}
