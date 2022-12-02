package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CustomClearancePackageResDto extends BaseDto {
	/**
	* 
	*/
	private static final long serialVersionUID = -2181095887777442371L;

	@JsonProperty("postPackageId")
	private long postPackageId;

	@JsonProperty("fileId")
	private long fileId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("type")
	private String type;

}
