package com.nineplus.bestwork.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.PageSearchUserDto;
import com.nineplus.bestwork.dto.UserReqDto;
import com.nineplus.bestwork.dto.UserResDto;
import com.nineplus.bestwork.entity.TUser;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.TokenUtils;
import com.nineplus.bestwork.utils.UserAuthUtils;

@PropertySource("classpath:application.properties")
@RequestMapping(value = "api/v1/users")
@RestController
public class UserController extends BaseController {

	@Autowired
	UserService userService;

	@Autowired
	TokenUtils tokenUtils;

	@Autowired
	UserAuthUtils userAuthUtils;

	@Value("${app.login.jwtPrefix}")
	private String PRE_STRING;

	@GetMapping("/isCheckLogin")
	public ResponseEntity<? extends Object> isCheckLogin(HttpServletRequest request, HttpServletResponse response) {
		return success(CommonConstants.MessageCode.S1X0010, null, null);
	}

	/**
	 * Get list company
	 * 
	 * @return list company
	 */

	@PostMapping("/list")
	public ResponseEntity<? extends Object> getAllUser(@RequestBody PageSearchUserDto pageCondition) {
		PageResponseDto<UserResDto> pageUser = null;
		try {
			if (pageCondition.getKeyword().isEmpty() && pageCondition.getCompany() <= 0 && pageCondition.getRole() <= 0
					&& pageCondition.getStatus() < 0) {
				pageUser = userService.getUserPageWithoutCondition(pageCondition);
			} else {
				pageUser = userService.getUserPageWithCondition(pageCondition);
			}

		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sU0006, pageUser, null);
	}

	@PostMapping("/create")
	public ResponseEntity<? extends Object> registerUser(@Valid @RequestBody UserReqDto userReqDto,
			BindingResult bindingResult) throws BestWorkBussinessException {

		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);

		if (userAuthRoleReq.getIsSysAdmin()) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
		long companyId = this.userService.findCompanyIdByAdminUsername(userAuthRoleReq);
		List<TUser> existsUsers = this.userService.findAllUsersByCompanyId(companyId);
		for (TUser user : existsUsers) {
			if (user.getUserName().equals(userReqDto.getUserName())) {
				bindingResult.rejectValue("username", "ExistedUsername", "Username already exists in the company.");
			}
		}

		if (bindingResult.hasErrors()) {
			return failedWithError(CommonConstants.MessageCode.ECU0001, bindingResult.getFieldErrors().toArray(), null);
		}
		TUser createdUser = new TUser();
		try {
			createdUser = userService.createUser(userReqDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.SCU0001, createdUser, null);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<? extends Object> getUserById(@PathVariable long userId) throws BestWorkBussinessException {
		UserResDto userRes = userService.getUserById(userId);
		if (userRes == null) {
			return failed(CommonConstants.MessageCode.ECU0002, null);
		} else {
			return success(CommonConstants.MessageCode.SCU0002, userRes, null);
		}
	}

}
