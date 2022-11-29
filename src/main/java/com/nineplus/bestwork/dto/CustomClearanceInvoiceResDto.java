package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CustomClearanceInvoiceResDto extends BaseDto {
	/**
	* 
	*/
	private static final long serialVersionUID = 6603567567285679292L;

	@JsonProperty("listFileInvoice")
	private List<CustomClearanceInvoiceFileResDto> listFile;

}
