package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class CountLocationDto {
    @JsonProperty("location")
    private String location;
    @JsonProperty("count")
    private BigInteger count;
}
