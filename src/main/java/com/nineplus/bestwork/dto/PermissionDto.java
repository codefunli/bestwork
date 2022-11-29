package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1027687385392946729L;

	@JsonProperty("id")
	private Long id;

	@JsonProperty("monitorId")
	private Long monitorId;

	@JsonProperty("monitorName")
	private String monitorName;

	@JsonProperty("canAdd")
	private Boolean canAdd;

	@JsonProperty("canEdit")
	private Boolean canEdit;

	@JsonProperty("canDelete")
	private Boolean canDelete;

	@JsonProperty("canAccess")
	private Boolean canAccess;

	@JsonProperty("status")
	private Integer status;
}
