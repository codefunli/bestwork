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
public class AirWayBillResDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7103442123970671441L;

	@JsonProperty("id")
	private long id;

	@JsonProperty("code")
	private String code;

	@JsonProperty("note")
	private String note;

	@JsonProperty("status")
	private int status;

	@JsonProperty("createDate")
	private String createDate;

	@JsonProperty("updateDate")
	private String updateDate;

	@JsonProperty("createBy")
	private String createBy;

	@JsonProperty("updateBy")
	private String updateBy;

}
