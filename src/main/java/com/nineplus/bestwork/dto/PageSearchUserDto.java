package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class PageSearchUserDto {

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
