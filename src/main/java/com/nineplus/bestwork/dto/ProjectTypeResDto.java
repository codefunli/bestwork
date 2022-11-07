package com.nineplus.bestwork.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectTypeResDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8051310103533555915L;

	private Integer id;

	private String name;

	private String description;

}
