package com.nineplus.bestwork.dto;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author DiepTT
 *
 */

@Data
@EqualsAndHashCode
public class ProjectRequestDto {

	@NotBlank(message = "Enter project name")
	private String projectName;

	@NotBlank(message = "Enter project description")
	private String description;

	private Integer notificationFlag;

	private Integer isPaid;

	private Integer status;

	@Range(min = 1, max = 4, message = "Enter type in range 1 to 4")
	private Integer projectType;

}
