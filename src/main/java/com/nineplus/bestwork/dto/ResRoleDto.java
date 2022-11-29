package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResRoleDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1751393852040347549L;

	@JsonProperty("id")
	private Long id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description;

}
