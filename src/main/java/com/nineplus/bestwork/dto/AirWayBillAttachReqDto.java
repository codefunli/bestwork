package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class AirWayBillAttachReqDto extends BaseDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1693385259110927641L;
	@JsonProperty("postInvoice")
	PostInvoiceReqDto postInvoiceReqDto;

}
