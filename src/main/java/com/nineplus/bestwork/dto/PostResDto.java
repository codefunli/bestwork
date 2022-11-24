package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nineplus.bestwork.entity.ConstructionEntity;
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
public class PostResDto extends BaseDto{
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
	
//	@JsonProperty("project")
//	private ProjectEntity project;

	@JsonProperty("construction")
	private ConstructionEntity construction;
	
	@JsonProperty("fileStorages")
	private List<FileStorageResDto> fileStorages;
}
