package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class PackageFileDownLoadReqDto extends BaseDto{/**
	 * 
	 */
	private static final long serialVersionUID = 6282151443315158333L;
	
	@JsonProperty("packagePostId")
	private long packagePostId;

	@JsonProperty("fileId")
	private long fileId;

}
