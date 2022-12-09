package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountPrjConsDto {
    @JsonProperty("prjCount")
    private Integer prjCount;
    @JsonProperty("consCount")
    private Integer consCount;
}
