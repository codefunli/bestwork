package com.nineplus.bestwork.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DiepTT
 */

@Data
@EqualsAndHashCode
public class ChangePasswordReqDto implements Serializable {

	private static final long serialVersionUID = 4432556268398475664L;

	@NotBlank(message = "Enter your current password")
	private String currentPassword;

	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^`<>&+=\"!ºª·#~%&'¿¡€,:;*/+-.=_\\[\\]\\(\\)\\|\\_\\?\\\\])(?=\\S+$).{8,100}$", 
			message = "Enter from 8 to 100 characters, combine uppercase(s), lowercase(s), digit(s) and special character(s).")
	private String newPassword;
	

	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^`<>&+=\"!ºª·#~%&'¿¡€,:;*/+-.=_\\[\\]\\(\\)\\|\\_\\?\\\\])(?=\\S+$).{8,100}$", 
			message = "Enter from 8 to 100 characters, combine uppercase(s), lowercase(s), digit(s) and special character(s).")
	private String confirmPassword;
}
