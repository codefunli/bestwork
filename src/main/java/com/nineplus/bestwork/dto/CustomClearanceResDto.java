package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomClearanceResDto extends BaseDto {
	/**
	* 
	*/
	private static final long serialVersionUID = 2836785181757379376L;

	@JsonProperty("invoicesDoc")
	private List<CustomClearanceInvoiceFileResDto> invoicesDoc;

	@JsonProperty("packagesDoc")
	private List<CustomClearancePackageFileResDto> packagesDoc;
	
	@JsonProperty("imageBeforeDoc")
	private List<CustomClearanceImageFileResDto> imageBeforeDoc;

}
