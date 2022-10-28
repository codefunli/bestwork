package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nineplus.bestwork.entity.FileStorageEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProgressReqDto {
	
	@JsonProperty("projectId")
	private String projectId;

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

	@JsonProperty("images")
	private List<FileStorageEntity> fileStorages;
}
