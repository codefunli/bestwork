package com.nineplus.bestwork.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.ChangePasswordReqDto;
import com.nineplus.bestwork.entity.TUser;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.services.SysUserService;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.UserAuthUtils;

@RestController
@RequestMapping("/api/v1")
public class PasswordController extends BaseController {

	@Autowired
	UserAuthUtils userAuthUtils;

	@Autowired
	UserService userService;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	SysUserService sysUserService;

	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordReqDto changePasswordReqDto,
			BindingResult bindingResult) throws BestWorkBussinessException {

		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		String username = userAuthRoleReq.getUsername();

		TUser currentUser = userService.getUserByUsername(username);

		if (bindingResult.hasErrors()) {
			return failedWithError(CommonConstants.MessageCode.SU0003, bindingResult.getFieldErrors().toArray(), null);
		}
		if (currentUser == null) {
			return failed(CommonConstants.MessageCode.ECU0005, null);
		}
		if (!bCryptPasswordEncoder.matches(changePasswordReqDto.getCurrentPassword(), currentUser.getPassword())) {
			return failedWithError(CommonConstants.MessageCode.ECU0006, changePasswordReqDto.getCurrentPassword(),
					null);
		}
		String newPassword = changePasswordReqDto.getNewPassword();
		String confirmPassword = changePasswordReqDto.getConfirmPassword();

		if (newPassword.equals(confirmPassword)) {
			currentUser.setUpdateDate(LocalDateTime.now());
			currentUser.setUpdateBy(username);
			this.sysUserService.updatePassword(currentUser, newPassword);
			return success(CommonConstants.MessageCode.SU0004, null, null);
		} else {
			return failedWithError(CommonConstants.MessageCode.SU0005, null, null);
		}
	}
}
