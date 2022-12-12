package com.nineplus.bestwork.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MonitorResDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8006165214386093285L;

	@JsonProperty("id")
	private Long id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("icon")
	private String icon;

	@JsonProperty("url")
	private String url;

	private String createdUser;

	private Timestamp createdDate;

	private String updatedUser;

	private Timestamp updatedDate;
}
