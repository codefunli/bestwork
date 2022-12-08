package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author TuanNA
 *
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class EvidenceAfterReqDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8950939805008081581L;
	
	@JsonProperty("awbId")
	private long awbId;

	@JsonProperty("description")
	private String description;

}
