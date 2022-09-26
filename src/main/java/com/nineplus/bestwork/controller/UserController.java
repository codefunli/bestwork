package com.nineplus.bestwork.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.TUserResponseDTO;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.TokenUtils;

@PropertySource("classpath:application.properties")
@RequestMapping("/api/v1")
@RestController
public class UserController extends BaseController {

	@Autowired
	UserService userService;

	@Autowired
	TokenUtils tokenUtils;

	@Value("${app.login.jwtPrefix}")
	private String PRE_STRING;

	@GetMapping("/users")
	public ResponseEntity<? extends Object> getUserInfo(HttpServletRequest request, HttpServletResponse response) {
		Cookie accessCookie = tokenUtils.getCookieFromRequest(request, CommonConstants.Authentication.ACCESS_COOKIE);
		if (accessCookie != null) {
			try {
				String username = tokenUtils.getUserNameFromCookie(accessCookie);
				TUserResponseDTO user = userService.convertUserToUserDto(userService.getUserByUsername(username));
				return user != null ? success(CommonConstants.MessageCode.S1X0003, user, null)
						: failed(CommonConstants.MessageCode.E1X0003, null);
			} catch (Exception ex) {
				return failed(CommonConstants.MessageCode.E1X0003, null);
			}
		}
		return success(CommonConstants.MessageCode.S1X0003, null, null);
	}

}
