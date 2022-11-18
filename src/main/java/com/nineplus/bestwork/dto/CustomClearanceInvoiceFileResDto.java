package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CustomClearanceInvoiceFileResDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1153868903525572281L;

	@JsonProperty("postInvoiceId")
	private long postInvoiceId;

	@JsonProperty("fileId")
	private long fileId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("type")
	private String type;

	@JsonProperty("postType")
	private String postType;
	
	@JsonProperty("content")
	private byte[] content;
}
