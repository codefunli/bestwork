package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MonitorResDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("icon")
    private String icon;
    private String url;
    private String createdUser;
    private Timestamp createdDate;
    private String updatedUser;
    private Timestamp updatedDate;
}
