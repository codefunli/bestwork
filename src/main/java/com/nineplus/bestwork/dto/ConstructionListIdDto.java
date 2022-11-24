package com.nineplus.bestwork.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ConstructionListIdDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2427327634839223923L;

	private long[] listId;
}
