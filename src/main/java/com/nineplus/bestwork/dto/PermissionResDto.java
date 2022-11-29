package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionResDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4550556951762793090L;

	@JsonProperty("id")
	private Long id;

	@JsonProperty("canAccess")
	private Boolean canAccess;

	@JsonProperty("canAdd")
	private Boolean canAdd;

	@JsonProperty("canEdit")
	private Boolean canEdit;

	@JsonProperty("canDelete")
	private Boolean canDelete;

	@JsonProperty("status")
	private Integer status;

	@JsonProperty("monitorId")
	private Long monitorId;

	@JsonProperty("monitorName")
	private String monitorName;

	@JsonProperty("roleId")
	private Long roleId;
}
