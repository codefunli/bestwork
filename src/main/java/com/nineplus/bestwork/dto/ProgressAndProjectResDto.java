package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProgressAndProjectResDto extends BaseDto {
	/**
	* 
	*/
	private static final long serialVersionUID = 1268385730131250682L;

	@JsonProperty("project")
	private ProjectResDto project;

	@JsonProperty("progress")
	private List<ProgressResDto> progress;

}
