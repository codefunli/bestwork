package com.nineplus.bestwork.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ForgotPasswordResDto {

	private String username;
	
	private String fullname;
	
	private String email;
}
