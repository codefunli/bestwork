package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ProjectStatusReqDto extends BaseDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7961138034376264011L;

	@JsonProperty("toStatus")
	private String toStatus;

}
