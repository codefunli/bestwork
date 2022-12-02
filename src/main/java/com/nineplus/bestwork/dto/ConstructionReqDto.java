package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ConstructionReqDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9031950757474875938L;

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
	private List<AirWayBillReqDto> awbCodes;
}
