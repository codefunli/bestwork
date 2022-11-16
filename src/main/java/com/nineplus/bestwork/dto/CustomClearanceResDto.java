package com.nineplus.bestwork.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomClearanceResDto extends BaseDto {/**
	 * 
	 */
	private static final long serialVersionUID = 2836785181757379376L;

	@JsonProperty("code")
	private String code;

	@JsonProperty("invoicesDoc")
	private CustomClearanceInvoiceResDto invoicesDoc;

	@JsonProperty("packagesDoc")
	private  CustomClearancePackageResDto packagesDoc;

}
