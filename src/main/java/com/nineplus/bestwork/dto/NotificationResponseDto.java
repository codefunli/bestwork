package com.nineplus.bestwork.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class NotificationResponseDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7019926932215436516L;

	private long id;

	private String title;

	private String content;

	private String createDate;

	private int isRead;

	private long userId;

}
