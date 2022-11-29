package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PageResDto<T> extends BaseDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7969015153524943561L;

	@JsonProperty("content")
	private List<T> content;

	@JsonProperty("metaData")
	private RPageDto metaData;

}
