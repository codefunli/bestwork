package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class AirWayBillStatusReqDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5019277631020080543L;
	@JsonProperty("destinationStatus")
	private int destinationStatus;

}
