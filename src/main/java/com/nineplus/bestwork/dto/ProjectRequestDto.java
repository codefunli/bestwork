package com.nineplus.bestwork.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ProjectRequestDto {

	@NotBlank(message = "Enter project name")
	private String projectName;

	@NotBlank(message = "Enter project description")
	private String description;

	private Integer projectType;

	private Integer notificationFlag;

	private Integer isPaid;

	private Integer status;

	private String comment;

}
