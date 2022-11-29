package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectTaskReqDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4820971195555758214L;

	@JsonProperty("project")
	private ProjectReqDto project;

	@JsonProperty("roleData")
	private List<ProjectAssignReqDto> roleData;
}
