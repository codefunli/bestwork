package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class ResMonitorDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;
    @JsonProperty("parentId")
    private String parentId;
    @JsonProperty("displayOrder")
    private String displayOrder;
    @JsonProperty("icon")
    private String icon;
    @JsonProperty("showAccess")
    private String showAccess;
    @JsonProperty("showAdd")
    private String showAdd;
    @JsonProperty("showEdit")
    private String showEdit;
    @JsonProperty("showDelete")
    private String showDelete;


}
