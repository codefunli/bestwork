package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nineplus.bestwork.entity.ProjectEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author DiepTT
 *
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class PostResponseDto extends BaseDto{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2854403837236797129L;
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("eqBill")
	private String eqBill;
	
	@JsonProperty("createDate")
	private String createDate;
	
	@JsonProperty("comment")
	private String comment;
	
	@JsonProperty("project")
	private ProjectEntity project;
	
	@JsonProperty("fileStorages")
	private List<FileStorageResponseDto> fileStorages;
}
