package com.nineplus.bestwork.dto;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountLocationDto {
    @JsonProperty("location")
    private String location;
    @JsonProperty("count")
    private BigInteger count;
}
