package com.nineplus.bestwork.dto;

import java.sql.Timestamp;

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

	@JsonProperty("id")
	private long id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("data")
	private String data;

	@JsonProperty("type")
	private String type;

	@JsonProperty("createDate")
	private Timestamp createDate;
	
	@JsonProperty("progressId")
	private long progressId; 

}
