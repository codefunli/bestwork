package com.nineplus.bestwork.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nineplus.bestwork.entity.ProjectTypeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author DiepTT
 *
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectResponseDto extends BaseDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7969015153524943561L;

	@JsonProperty("id")
	private String id ;

	@JsonProperty("projectName")
	private String projectName;

	@JsonProperty("description")
	private String description;

	@JsonProperty("projectType")
	private ProjectTypeEntity projectType;

	@JsonProperty("notificationFlag")
	private Boolean notificationFlag;

	@JsonProperty("isPaid")
	private Boolean isPaid;

	@JsonProperty("status")
	private String status;

	@JsonProperty("createDate")
	private String createDate;

	@JsonProperty("updateDate")
	private LocalDateTime updateDate;

//	@JsonProperty("posts")
//	private Collection<PostEntity> posts;

}
