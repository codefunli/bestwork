package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PackagePostReqDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5373302457283153662L;

	@JsonProperty("description")
	private String description;

	@JsonProperty("comment")
	private String comment;

}
