package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ConditionSearchDto {

    @JsonProperty("id")
    Long id;
    @JsonProperty("name")
    String name;
    @JsonProperty("roleName")
    String roleName;
    @JsonProperty("monitorName")
    String monitorName;
}
