package com.nineplus.bestwork.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectRoleUserResDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1159648122714585414L;

	@JsonProperty("userId")
	private long userId;

	@JsonProperty("canView")
	private boolean canView;

	@JsonProperty("canEdit")
	private boolean canEdit;

}
