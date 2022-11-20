package com.nineplus.bestwork.dto;



import java.io.ByteArrayOutputStream;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author DiepTT
 *
 */

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
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
	private byte[] content;

	@JsonProperty("isChoosen")
	private boolean isChoosen;

}
