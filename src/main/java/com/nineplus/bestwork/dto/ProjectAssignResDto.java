package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectAssignResDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3407114215478346735L;
	
	@JsonProperty("companyId")
	private long companyId;
	
	@JsonProperty("companyName")
	private long companyName;

	@JsonProperty("userList")
	private List<ProjectRoleUserReqDto> userList;

}
