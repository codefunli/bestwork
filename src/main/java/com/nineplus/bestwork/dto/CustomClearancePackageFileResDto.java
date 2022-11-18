package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CustomClearancePackageFileResDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3810669170789475982L;

	@JsonProperty("postPackageId")
	private long postPackageId;

	@JsonProperty("fileId")
	private long fileId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("type")
	private String type;

	@JsonProperty("postType")
	private String postType;

}
