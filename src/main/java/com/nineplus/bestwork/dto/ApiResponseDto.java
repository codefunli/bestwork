package com.nineplus.bestwork.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class ApiResponseDTO {
	private String code;

	private String message;

	private Object data;

	private String status;

}
