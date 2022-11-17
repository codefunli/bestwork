package com.nineplus.bestwork.dto;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
public class ApiResponseDto {
	private String code;

	private String message;

	private Object data;

	private String status;

}
