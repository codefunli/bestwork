package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectStatusResDto extends BaseDto {
	/**
	* 
	*/
	private static final long serialVersionUID = 8765371192330948904L;

	@JsonProperty("id")
	private int id;

	@JsonProperty("status")
	private String status;

}
