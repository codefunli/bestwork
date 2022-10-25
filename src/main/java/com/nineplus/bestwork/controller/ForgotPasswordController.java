package com.nineplus.bestwork.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.ForgotPasswordReqDto;
import com.nineplus.bestwork.dto.ForgotPasswordResDto;
import com.nineplus.bestwork.dto.ResetPasswordReqDto;
import com.nineplus.bestwork.entity.TUser;
import com.nineplus.bestwork.exception.SysUserNotFoundException;
import com.nineplus.bestwork.services.MailSenderService;
import com.nineplus.bestwork.services.SysUserService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.MessageUtils;

import net.bytebuddy.utility.RandomString;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class ForgotPasswordController extends BaseController {

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private MessageUtils messageUtils;

	@Autowired
	private MailSenderService mailService;

	@PostMapping("/forgot-password")
	public ResponseEntity<? extends Object> processForgotPassword(
			@Valid @RequestBody ForgotPasswordReqDto forgotPasswordReqDto, BindingResult bindingResult)
			throws Exception {

		if (bindingResult.hasErrors()) {
			return failedWithError(CommonConstants.MessageCode.SU0003, bindingResult.getFieldErrors().toArray(), null);
		}

		String emailReq = forgotPasswordReqDto.getEmail();
		TUser sysUserReq = this.sysUserService.getUserByEmail(emailReq);
		if (sysUserReq == null) {
			return failedWithError(CommonConstants.MessageCode.SU0002, forgotPasswordReqDto, null);
		}

		String token = RandomString.make(45);

		try {
			sysUserService.updateResetPasswordToken(token, emailReq);
			String resetPasswordLink = messageUtils.getMessage(CommonConstants.Url.URL0001, null)
					+ "/auth/reset-password/" + token;
			String username = sysUserReq.getUserName();
			mailService.sendMailResetPassword(emailReq, username, resetPasswordLink);

		} catch (SysUserNotFoundException e) {
			e.printStackTrace();
		}

		ForgotPasswordResDto forgotPasswordResDto = new ForgotPasswordResDto();
		forgotPasswordResDto.setUsername(sysUserReq.getUserName());
		forgotPasswordResDto.setEmail(sysUserReq.getEmail());
		forgotPasswordResDto.setFirstname(sysUserReq.getFirstNm());
		forgotPasswordResDto.setLastname(sysUserReq.getLastNm());
		return success(CommonConstants.MessageCode.SU0001, forgotPasswordResDto, null);
	}

	@PostMapping("/reset-password/{token}")
	public ResponseEntity<?> changePassword(@PathVariable String token,
			@Valid @RequestBody ResetPasswordReqDto resetPasswordReqDto, BindingResult bindingResult)
			throws IOException {

		if (bindingResult.hasErrors()) {
			return failedWithError(CommonConstants.MessageCode.SU0003, bindingResult.getFieldErrors().toArray(), null);
		}

		TUser sysUser = this.sysUserService.get(token);
		if (sysUser == null) {
			return failedWithError(CommonConstants.MessageCode.SU0002, sysUser, null);
		}

		if (resetPasswordReqDto.getPassword().equals(resetPasswordReqDto.getConfirmPassword())) {

			String newPassword = resetPasswordReqDto.getPassword();
			sysUser.setUpdateDate(LocalDateTime.now());
			sysUser.setUpdateBy(sysUser.getUserName());

			this.sysUserService.updatePassword(sysUser, newPassword);

			return success(CommonConstants.MessageCode.SU0004, null, null);
		} else {
			return failedWithError(CommonConstants.MessageCode.SU0005, resetPasswordReqDto, null);
		}
	}

}
