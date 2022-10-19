package com.nineplus.bestwork.dto;

import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PageSearchDto {
	@JsonProperty("page")
	private String page;
	
	@JsonProperty("size")
	private String size;
	
	@JsonProperty("sortDirection")
	private Sort.Direction sortDirection;
	
	@JsonProperty("sortBy")
	private String sortBy;

	@JsonProperty("keyword")
	private String keyword;
}
