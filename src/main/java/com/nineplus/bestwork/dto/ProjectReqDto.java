package com.nineplus.bestwork.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectReqDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3716543566647577097L;

	@JsonProperty("projectName")
	private String projectName;

	@JsonProperty("description")
	private String description;

	@JsonProperty("notificationFlag")
	private int notificationFlag;

	@JsonProperty("isPaid")
	private int isPaid;

	@JsonProperty("status")
	private int status;

	@JsonProperty("projectType")
	private int projectType;

	@JsonProperty("createDate")
	private String createDate;

}
