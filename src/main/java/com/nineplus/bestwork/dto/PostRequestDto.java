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
public class PostRequestDto {

	private String projectId;

	private String description;

	private String[] images;
}
