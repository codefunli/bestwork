package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SearchDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8706003761081458181L;

	@JsonProperty("pageConditon")
	private PageSearchDto pageConditon;

	@JsonProperty("conditionSearch")
	private ConditionSearchDto conditionSearchDto;

}
