package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RegPermissionDto {
    @JsonProperty("roleId")
    private Long roleId;
    @JsonProperty("monitorInfo")
    List<PermissionDto> monitorInfo;
}
