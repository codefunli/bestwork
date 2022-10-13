package com.nineplus.bestwork.dto;

import java.sql.Timestamp;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nineplus.bestwork.entity.TFileStorage;
import com.nineplus.bestwork.model.ProjectStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TProjectResponseDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7969015153524943561L;

	@JsonProperty("id")
	private String id;

	@JsonProperty("projectName")
	private String projectName;

	@JsonProperty("description")
	private String description;

	@JsonProperty("projectType")
	private Integer projectType;

	@JsonProperty("notificationFlag")
	private Integer notificationFlag;

	@JsonProperty("isPaid")
	private Integer isPaid;

	@JsonProperty("status")
	private ProjectStatus status;

	@JsonProperty("createDate")
	private Timestamp createDate;

	@JsonProperty("updateDate")
	private Timestamp updateDate;

	@JsonProperty("comment")
	private String comment;

	@JsonProperty("fileStorages")
	private Collection<TFileStorage> fileStorages;

}
