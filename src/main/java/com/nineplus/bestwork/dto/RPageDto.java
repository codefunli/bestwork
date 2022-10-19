package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RPageDto extends BaseDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7969015153524943561L;

	@JsonProperty("totalPages")
	private int totalPages;

	@JsonProperty("totalElements")
	private long totalElements;

	@JsonProperty("size")
	private int size;

	@JsonProperty("number")
	private int number;

}
