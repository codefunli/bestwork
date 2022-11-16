package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author DiepTT
 *
 */

@Getter
@Setter
public class FileStorageResDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9071487189534200767L;

	@JsonProperty("fileId")
	private long id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("data")
	private String data;

	@JsonProperty("type")
	private String type;

	@JsonProperty("createDate")
	private String createDate;
	
	@JsonProperty("progressId")
	private long progressId; 
	
	@JsonProperty("content")
	private String content;

	@JsonProperty("isChoosen")
	private boolean isChoosen;

}
