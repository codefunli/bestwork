package com.nineplus.bestwork.dto;

import java.util.List;

import com.nineplus.bestwork.entity.ProjectEntity;

import lombok.Data;

/**
 * 
 * @author DiepTT
 *
 */

@Data
public class PostResponseDto extends BaseDto{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2854403837236797129L;

	private String id;

	private String description;

	private String createDate;

	private ProjectEntity project;

	private List<FileStorageResponseDto> fileStorages;
}
