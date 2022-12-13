package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author TuanNA
 *
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class EvidenceBeforeResDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2766380575224594531L;

	@JsonProperty("evidenceBeforeId")
	private long id;

	@JsonProperty("description")
	private String description;

	@JsonProperty("comment")
	private String comment;

	@JsonProperty("createDate")
	private String createDate;

	@JsonProperty("updateDate")
	private String updateDate;

	@JsonProperty("createBy")
	private String createBy;

	@JsonProperty("updateBy")
	private String updateBy;

	@JsonProperty("postType")
	private String postType;

	@JsonProperty("fileStorages")
	private List<FileStorageResDto> fileStorages;

}
