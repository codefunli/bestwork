package com.nineplus.bestwork.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author DiepTT
 *
 */
@Data
@EqualsAndHashCode
public class ForgotPasswordResDto {

	private String username;

	private String firstname;

	private String lastname;

	private String email;
}
