package com.nineplus.bestwork.dto;

import java.sql.Timestamp;

import lombok.Data;

/**
 * 
 * @author DiepTT
 *
 */

@Data
public class FileStorageResponseDto {
	private String id;

	private String name;

	private String data;

	private String type;

	private Timestamp createDate;

}
