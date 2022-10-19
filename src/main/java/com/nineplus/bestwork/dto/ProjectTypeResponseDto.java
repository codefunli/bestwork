package com.nineplus.bestwork.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ProjectTypeResponseDto {
	private Integer id;

	private String name;

	private String description;

}
