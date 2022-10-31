package com.nineplus.bestwork.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectRoleUserResDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1159648122714585414L;
	
	@JsonProperty("companyId")
	private long companyId;
	
	@JsonProperty("userId")
	private long userId;

	@JsonProperty("userName")
	private String userName;

	@JsonProperty("canView")
	private boolean canView;

	@JsonProperty("canEdit")
	private boolean canEdit;

}
