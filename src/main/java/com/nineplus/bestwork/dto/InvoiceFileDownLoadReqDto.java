package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class InvoiceFileDownLoadReqDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4249525930785030657L;

	@JsonProperty("invoicePostId")
	private long invoicePostId;

	@JsonProperty("fileId")
	private long fileId;

}
