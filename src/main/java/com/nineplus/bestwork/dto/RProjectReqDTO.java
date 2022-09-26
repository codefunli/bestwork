package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RProjectReqDTO {
	
	@JsonProperty("pageConditon")
    private PageSearchDTO pageConditon;

    @JsonProperty("projectCondition")
    private PrjConditionSearchDTO projectCondition;
    

}
