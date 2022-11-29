package com.nineplus.bestwork.dto;

import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResetPasswordReqDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6795176570305696156L;

	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^`<>&+=\"!ºª·#~%&'¿¡€,:;*/+-.=_\\[\\]\\(\\)\\|\\_\\?\\\\])(?=\\S+$).{8,100}$", message = "Enter from 8 to 100 characters, combine uppercase(s), lowercase(s), digit(s) and special character(s).")
	private String password;

	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^`<>&+=\"!ºª·#~%&'¿¡€,:;*/+-.=_\\[\\]\\(\\)\\|\\_\\?\\\\])(?=\\S+$).{8,100}$", message = "Enter from 8 to 100 characters, combine uppercase(s), lowercase(s), digit(s) and special character(s).")
	private String confirmPassword;

}
