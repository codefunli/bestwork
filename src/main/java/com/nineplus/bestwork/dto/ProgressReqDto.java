package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProgressReqDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4581409523912715993L;

	@JsonProperty("constructionId")
	private long constructionId;

	@JsonProperty("title")
	private String title;

	@JsonProperty("status")
	private String status;

	@JsonProperty("startDate")
	private String startDate;

	@JsonProperty("endDate")
	private String endDate;

	@JsonProperty("report")
	private String report;

	@JsonProperty("note")
	private String note;

}
