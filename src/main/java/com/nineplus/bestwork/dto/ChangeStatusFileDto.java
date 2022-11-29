package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ChangeStatusFileDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3471076428358846580L;

	@JsonProperty("postType")
	private String postType;

	@JsonProperty("toStatus")
	private boolean destinationStatus;

	@JsonProperty("postId")
	private long postId;

	@JsonProperty("fileId")
	private Long[] fileId;

}
