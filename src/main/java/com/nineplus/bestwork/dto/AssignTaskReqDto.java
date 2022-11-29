package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AssignTaskReqDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6419495335723750320L;

	@JsonProperty("companyId")
	private String companyId;

	@JsonProperty("projectId")
	private String projectId;
}
