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
public class RoleResDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4029561917437379896L;
	
	@JsonProperty("id")
	private int id;

	@JsonProperty("role")
	private String role;


}
