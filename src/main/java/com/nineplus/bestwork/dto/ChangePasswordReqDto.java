package com.nineplus.bestwork.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author DiepTT
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ChangePasswordReqDto extends BaseDto {

	/**
		 * 
		 */
	private static final long serialVersionUID = 2489379081966449543L;

	@NotBlank(message = "Enter your current password")
	private String currentPassword;

	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^`<>&+=\"!ºª·#~%&'¿¡€,:;*/+-.=_\\[\\]\\(\\)\\|\\_\\?\\\\])(?=\\S+$).{8,100}$", message = "Enter from 8 to 100 characters, combine uppercase(s), lowercase(s), digit(s) and special character(s).")
	private String newPassword;

	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^`<>&+=\"!ºª·#~%&'¿¡€,:;*/+-.=_\\[\\]\\(\\)\\|\\_\\?\\\\])(?=\\S+$).{8,100}$", message = "Enter from 8 to 100 characters, combine uppercase(s), lowercase(s), digit(s) and special character(s).")
	private String confirmPassword;
}
