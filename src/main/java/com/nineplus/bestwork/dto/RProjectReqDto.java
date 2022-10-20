package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RProjectReqDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3900695676476137796L;

	@JsonProperty("pageConditon")
	private PageSearchDto pageConditon;

	@JsonProperty("projectCondition")
	private PrjConditionSearchDto projectCondition;

}
