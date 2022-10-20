package com.nineplus.bestwork.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class TFileStorageDto {
	@JsonProperty("id")
	private Integer id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("type")
	private String type;

	@JsonProperty("createDate")
	private Timestamp createDate;

	@JsonProperty("updateDate")
	private Timestamp updateDate;

}
