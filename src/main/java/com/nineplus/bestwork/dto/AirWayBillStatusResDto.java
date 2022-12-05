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
public class AirWayBillStatusResDto extends BaseDto {

	/**
	* 
	*/
	private static final long serialVersionUID = 7956159140255338524L;

	@JsonProperty("id")
	private int id;

	@JsonProperty("status")
	private String status;
}
