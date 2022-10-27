package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectTaskDto extends BaseDto {
	 /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2234703426292619983L;
	@JsonProperty("project")
	private ProjectReqDto project;

	 @JsonProperty("roleData")
	private List<ProjectAssignReqDto> roleData;
}
