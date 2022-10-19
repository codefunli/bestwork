package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RProjectReqDto {
	
	@JsonProperty("pageConditon")
    private PageSearchDto pageConditon;

    @JsonProperty("projectCondition")
    private PrjConditionSearchDto projectCondition;
    

}
