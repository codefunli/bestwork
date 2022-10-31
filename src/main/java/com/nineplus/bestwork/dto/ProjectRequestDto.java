package com.nineplus.bestwork.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author DiepTT
 *
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectRequestDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3716543566647577097L;

	@NotBlank(message = "Enter project name")
	private String projectName;

	@NotBlank(message = "Enter project description")
	private String description;

	private Integer notificationFlag;

	private Integer isPaid;

	private Integer status;

	private Integer projectType;

}
