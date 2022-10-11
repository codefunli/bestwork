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
	
	@JsonProperty("id")
    private String id;

	@JsonProperty("companyId")
	private Long companyId;

	@JsonProperty("companyName")
	private String companyName;

	@JsonProperty("email")
	private String email;

	@JsonProperty("telNo")
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

	@JsonProperty("detailAddress")
	private String detailAddress;

	@JsonProperty("startDate")
	private String startDate;

	@JsonProperty("expiredDate")
	private String expiredDate;
}
