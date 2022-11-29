package com.nineplus.bestwork.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FileStorageDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9170073873460967635L;

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("type")
	private String type;

	@JsonProperty("createDate")
	private Timestamp createDate;

	@JsonProperty("updateDate")
	private Timestamp updateDate;

}
