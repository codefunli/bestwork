package com.nineplus.bestwork.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author DiepTT
 *
 */

@Data
@EqualsAndHashCode
public class PostRequestDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8482355323085970702L;

	private String projectId;

	private String description;

	private String[] images;
}
