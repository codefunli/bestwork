package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ActionResDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7720676603252278206L;

	@JsonProperty("id")
	private Long id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("url")
	private String url;

	@JsonProperty("icon")
	private String icon;

	@JsonProperty("status")
	private String status;

	@JsonProperty("method")
	private String method;
}
