package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class FileStorageReqDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4732663469808071724L;

	@JsonProperty("id")
	private long id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("data")
	private String data;

	@JsonProperty("type")
	private String type;

	@JsonProperty("createDate")
	private String createDate;

}
