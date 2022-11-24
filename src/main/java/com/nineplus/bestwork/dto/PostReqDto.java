package com.nineplus.bestwork.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author DiepTT
 *
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class PostReqDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8482355323085970702L;

//	private String projectId;

	private long constructionId;

	private String description;
	
	private String eqBill;

	private String[] images;
}
