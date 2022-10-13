package com.nineplus.bestwork.dto;

import java.sql.Timestamp;

import javax.validation.constraints.NotBlank;

import com.nineplus.bestwork.model.ProjectStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ProjectRequestDto {

	private String id;

	@NotBlank(message = "Enter project name")
	private String projectName;

	@NotBlank(message = "Enter project description")
	private String description;

	private Integer projectType;

	private Integer notificationFlag;

	private Integer isPaid;


	private Integer status;

	private Timestamp createDate;

	private Timestamp updateDate;

	private String comment;

}
