package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NationResDto extends BaseDto {
	/**
	* 
	*/
	private static final long serialVersionUID = 3575086149246911913L;

	@JsonProperty("id")
	private long id;

	@JsonProperty("name")
	private String name;

}
