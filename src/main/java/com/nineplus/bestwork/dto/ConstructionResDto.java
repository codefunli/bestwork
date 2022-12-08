package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author DiepTT
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConstructionResDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6038101338958770371L;

	@JsonProperty("id")
	private long id;

	@JsonProperty("constructionName")
	private String constructionName;

	@JsonProperty("description")
	private String description;

	@JsonProperty("startDate")
	private String startDate;

	@JsonProperty("endDate")
	private String endDate;

	@JsonProperty("nation")
	private String nation;

	@JsonProperty("location")
	private String location;

	@JsonProperty("createBy")
	private String createBy;

	@JsonProperty("status")
	private String status;

	@JsonProperty("projectCode")
	private String projectCode;

	@JsonProperty("projectName")
	private String projectName;

	@JsonProperty("awbCodes")
	private List<AirWayBillResDto> awbCodes;

	@JsonProperty("fileStorages")
	private List<FileStorageResDto> fileStorages;
}
