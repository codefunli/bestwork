package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProgressReqDto {
	
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

//	@JsonProperty("fileStorages")
//	private List<FileStorageReqDto> fileStorages;
}
