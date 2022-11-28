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
public class AirWayBillReqDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2697551299668063985L;
	
	@JsonProperty("projectId")
	private String projectId;

	@JsonProperty("code")
	private String code;

	@JsonProperty("note")
	private String note;

	@JsonProperty("status")
	private int status;

}
