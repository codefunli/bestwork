package com.nineplus.bestwork.dto;

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
	    private ProjectAssignReqDto roleData;
}
