package com.nineplus.bestwork.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ProjectDeleteByIdDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 214503171219975448L;

//	private String id;
	private List<String> id;
}
