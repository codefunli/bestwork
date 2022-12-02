package com.nineplus.bestwork.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserListIdDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3806120398074938870L;

	private Long[] userIdList;
}
