package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UserWithProjectResDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4748363166444630568L;

	@JsonProperty("id")
	private String projectId;

	@JsonProperty("name")
	private String projectName;

	@JsonProperty("canView")
	private boolean canView;

	@JsonProperty("canEdit")
	private boolean canEdit;

}
