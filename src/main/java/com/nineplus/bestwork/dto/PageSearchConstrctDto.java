package com.nineplus.bestwork.dto;

import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PageSearchConstrctDto extends BaseDto {
	/**
	* 
	*/
	private static final long serialVersionUID = -4829908859158037430L;
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

	@JsonProperty("status")
	private int status;

	@JsonProperty("companyId")
	private int companyId;

	@JsonProperty("nationId")
	private int nationId;

	@JsonProperty("location")
	private String location;

	@JsonProperty("projectId")
	private String projectId;
}
