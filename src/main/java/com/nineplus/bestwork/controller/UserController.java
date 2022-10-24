package com.nineplus.bestwork.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.PageSearchUserDto;
import com.nineplus.bestwork.dto.UserResDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.TokenUtils;

@PropertySource("classpath:application.properties")
@RequestMapping(value = "api/v1/users")
@RestController
public class UserController extends BaseController {

	@Autowired
	UserService userService;

	@Autowired
	TokenUtils tokenUtils;

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

}
