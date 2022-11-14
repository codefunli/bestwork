package com.nineplus.bestwork.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class AirWayBillAllInfoResDto {
	

	@JsonProperty("AWB")
	private AirWayBillResDto airWayBill;

	@JsonProperty("invoices")
	private PostInvoiceResDto postInvoice;

}
