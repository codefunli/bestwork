package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ResPermissionDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("canAccess")
    private Boolean canAccess;
    @JsonProperty("canAdd")
    private Boolean canAdd;
    @JsonProperty("canEdit")
    private Boolean canEdit;
    @JsonProperty("canDelete")
    private Boolean canDelete;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("monitorId")
    private Long monitorId;
    @JsonProperty("roleId")
    private Long roleId;
}
