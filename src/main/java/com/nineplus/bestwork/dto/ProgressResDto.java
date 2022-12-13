package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProgressResDto extends BaseDto {

	/**
	 * @author TuanNA
	 */
	private static final long serialVersionUID = 2837534167302839503L;

	@JsonProperty("id")
	private Long id;

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

	@JsonProperty("createBy")
	private String createBy;

	@JsonProperty("createDate")
	private String createDate;

	@JsonProperty("fileBefore")
	private List<FileStorageResDto> fileBefore;
	
	@JsonProperty("fileAfter")
	private List<FileStorageResDto> fileAfter;

}
