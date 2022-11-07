package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProgressStatusResDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6649236760533637187L;

	@JsonProperty("id")
	private int id;

	@JsonProperty("status")
	private String status;

}
