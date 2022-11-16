package com.nineplus.bestwork.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class FileListDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5142195130132848676L;
	private Long[] listFileId;

}
