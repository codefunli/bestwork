package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ConstructionReqDto {

	@JsonProperty("constructionName")
	private String constructionName;

	@JsonProperty("description")
	private String description;

	@JsonProperty("startDate")
	private String startDate;

	@JsonProperty("endDate")
	private String endDate;

	@JsonProperty("location")
	private String location;

	@JsonProperty("status")
	private String status;
	
	@JsonProperty("projectCode")
	private String projectCode;

	@JsonProperty("awbCodes")
	private String[] awbCodes;
}
