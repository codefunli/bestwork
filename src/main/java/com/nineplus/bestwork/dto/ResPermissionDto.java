package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResPermissionDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("canAccess")
    private String canAccess;
    @JsonProperty("canAdd")
    private String canAdd;
    @JsonProperty("canEdit")
    private String canEdit;
    @JsonProperty("canDelete")
    private String canDelete;
    @JsonProperty("status")
    private String status;
    @JsonProperty("monitorId")
    private String monitorId;
    @JsonProperty("roleId")
    private String roleId;
}
