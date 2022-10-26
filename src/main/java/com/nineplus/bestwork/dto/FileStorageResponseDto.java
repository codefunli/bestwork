package com.nineplus.bestwork.dto;

import java.sql.Timestamp;

import lombok.Data;

/**
 * 
 * @author DiepTT
 *
 */

@Data
public class FileStorageResponseDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6658098507436726037L;

	private String id;

	private String name;

	private String data;

	private String type;

	private Timestamp createDate;
	
	private long progressId; 

}
