package com.nineplus.bestwork.dto;

import java.sql.Timestamp;
import java.util.List;

import com.nineplus.bestwork.entity.ProjectEntity;

import lombok.Data;

/** 
 * 
 * @author DiepTT
 *
 */

@Data
public class PostResponseDto {
	private String id;

	private String description;

	private Timestamp createDate;

	private ProjectEntity project;

	private List<FileStorageResponseDto> fileStorages;
}
