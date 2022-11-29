package com.nineplus.bestwork.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author DiepTT
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ForgotPasswordResDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5694349271552512291L;

	private String username;

	private String firstname;

	private String lastname;

	private String email;
}
