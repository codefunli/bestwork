package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author TuanNA
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectReqDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4302890498560102857L;

	@JsonProperty("projectName")
	private String projectName;

	@JsonProperty("description")
	private String description;

	@JsonProperty("notificationFlag")
	private Boolean notificationFlag;

	@JsonProperty("isPaid")
	private Boolean isPaid;

	@JsonProperty("status")
	private String status;

	@JsonProperty("projectType")
	private int projectType;

	@JsonProperty("startDate")
	private String startDate;

}
