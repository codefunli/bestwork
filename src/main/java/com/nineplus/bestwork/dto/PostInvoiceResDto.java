package com.nineplus.bestwork.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class PostInvoiceResDto extends BaseDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2766380575224594531L;
	
	@JsonProperty("id")
	private long id;

	@JsonProperty("postInvoiceCode")
	private String postInvoiceCode;

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

	@JsonProperty("fileStorages")
	private List<FileStorageResDto> fileStorages;

}
