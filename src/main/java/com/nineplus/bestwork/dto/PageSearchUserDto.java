package com.nineplus.bestwork.dto;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PageSearchUserDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3339752387422986735L;

	@NotNull
	@JsonProperty("page")
	private String page;

	@NotNull
	@JsonProperty("size")
	private String size;

	@NotNull
	@JsonProperty("sortDirection")
	private Sort.Direction sortDirection;

	@NotNull
	@JsonProperty("sortBy")
	private String sortBy;

	@NotNull
	@JsonProperty("keyword")
	private String keyword;

	@NotNull
	@JsonProperty("role")
	private String role;

	@NotNull
	@JsonProperty("status")
	private String status;
}
