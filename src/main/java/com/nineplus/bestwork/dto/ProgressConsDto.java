package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ProgressConsDto {
    @JsonProperty("constructionName")
    private String constructionName;
    @JsonProperty("title")
    private String title;
    @JsonProperty("createDate")
    private Timestamp createDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("constructionId")
    private BigInteger constructionId;
}
