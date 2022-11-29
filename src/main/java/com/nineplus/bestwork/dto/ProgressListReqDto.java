package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProgressListReqDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7058050785855346638L;
	
	@JsonProperty("ids")
	private Long[] lstProgressId;
}
