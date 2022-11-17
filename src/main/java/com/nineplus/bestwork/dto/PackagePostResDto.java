package com.nineplus.bestwork.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PackagePostResDto extends BaseDto {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6721145972241639891L;

	@JsonProperty("packageId")
	private long id;

	@JsonProperty("description")
	private String description;

	@JsonProperty("comment")
	private String comment;

	@JsonProperty("createDate")
	private LocalDateTime createDate;

	@JsonProperty("updateDate")
	private LocalDateTime updateDate;

	@JsonProperty("createBy")
	private String createBy;

	@JsonProperty("updateBy")
	private String updateBy;
	
	@JsonProperty("postType")
	private String postType;
	

	@JsonProperty("fileStorages")
	private List<FileStorageResDto> fileStorages;

}
