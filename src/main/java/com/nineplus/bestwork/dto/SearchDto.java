package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SearchDto {

    @JsonProperty("pageConditon")
    private PageSearchDTO pageConditon;

    @JsonProperty("conditionSearch")
    private ConditionSearchDto conditionSearchDto;


}
