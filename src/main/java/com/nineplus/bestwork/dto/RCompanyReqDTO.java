package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RCompanyReqDTO extends BaseDTO {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7969015153524943561L;


	@JsonProperty("companyName")
	private String companyName;

	@JsonProperty("cpEmail")
	private String email;

	@JsonProperty("cpTelNo")
	private String telNo;

	@JsonProperty("taxNo")
	private String taxNo;

	@JsonProperty("city")
	private String city;

	@JsonProperty("district")
	private String district;

	@JsonProperty("ward")
	private String ward;

	@JsonProperty("street")
	private String street;

	@JsonProperty("startDate")
	private String startDate;

	@JsonProperty("expiredDate")
	private String expiredDate;
	
	private String createBy;
	
	private String updateBy;
}
