package com.nineplus.bestwork.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineplus.bestwork.dto.LoginFailedResDto;
import com.nineplus.bestwork.dto.PermissionResDto;
import com.nineplus.bestwork.entity.UserEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.enumtype.Status;
import com.nineplus.bestwork.services.PermissionService;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.BestWorkBeanUtils;
import com.nineplus.bestwork.utils.CommonConstants;

@PropertySource("classpath:application.properties")
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private String SECRET_KEY;

	private int JWT_EXPIRATION;

	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static String currentUsername = "";

	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	BestWorkBeanUtils bestWorkBeanUtils;

	public CustomAuthenticationFilter(AuthenticationManager authenticationManage, String secretKey, String prefixToken,
			String jwtAge) {
		this.authenticationManager = authenticationManage;
		this.SECRET_KEY = secretKey;
		this.JWT_EXPIRATION = Integer.parseInt(jwtAge);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String requestData = "";
		String test = "";
		implementBean(request);
		try {
			requestData = request.getReader().lines().collect(Collectors.joining());
		} catch (IOException ex) {

		}
		JSONObject loginInfor = new JSONObject(requestData);
		String username = loginInfor.getString(CommonConstants.Authentication.USERNAME);
		String password = loginInfor.getString(CommonConstants.Authentication.PASSWORD);
		currentUsername = username;
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
		Authentication auth = authenticationManager.authenticate(authToken);
		return auth;
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		// set content type as json
		response.setContentType("application/json");
		String userName = currentUsername;
		implementBean(request);
		UserEntity tUserAuth = userService.getUserByUsername(userName);
		LoginFailedResDto loginFailedDTO = new LoginFailedResDto();
		if (ObjectUtils.isEmpty(tUserAuth)
				|| tUserAuth.getRole().getRoleName().equals(CommonConstants.RoleName.SYS_ADMIN)) {
			loginFailedDTO.setUsername(null);
			loginFailedDTO.setCountLoginFailed(0);
			loginFailedDTO.setLocked(false);
		} else {
			int countLoginFailed = tUserAuth.getLoginFailedNum();
			boolean isLocked = userService.isBlocked(countLoginFailed);
			if (!isLocked) {
				countLoginFailed += 1;
				tUserAuth.setLoginFailedNum(countLoginFailed);
				tUserAuth.setEnable(false);
				userService.saveUser(tUserAuth);
			}
			loginFailedDTO.setUsername(tUserAuth.getUserName());
			loginFailedDTO.setCountLoginFailed(countLoginFailed);
			loginFailedDTO.setLocked(isLocked);
		}
		try {
			response.getWriter().write(bestWorkBeanUtils.objectToJsonString(loginFailedDTO));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		implementBean(request);
		User user = (User) authResult.getPrincipal();
		List<Integer> lstStt = new ArrayList<>();
		lstStt.add(Status.ACTIVE.getValue());
		Map<String, Map<Long, List<PermissionResDto>>> mapRespon = new HashMap<>();
		Map<Long, List<PermissionResDto>> mapPermission;
		List<String> roleList = user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		try {
			mapPermission = permissionService.getMapPermissions(roleList, lstStt,((User) authResult.getPrincipal()).getUsername());
		} catch (BestWorkBussinessException e) {
			throw new RuntimeException(e);
		}
		Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
		String accessToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION * 1000L))
				.withIssuer(request.getRequestURL().toString())
				.withClaim(CommonConstants.Authentication.ROLES, roleList).sign(algorithm);
		String refreshToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION * 1000L))
				.withIssuer(request.getRequestURL().toString()).sign(algorithm);
		response.setHeader(CommonConstants.Authentication.ACCESS_TOKEN, accessToken);
		response.setHeader(CommonConstants.Authentication.REFRESH_TOKEN, refreshToken);
		response.setHeader("Access-Control-Expose-Headers",
				CommonConstants.Authentication.REFRESH_TOKEN + "," + CommonConstants.Authentication.ACCESS_TOKEN
						+ ", x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, "
						+ "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
		mapRespon.put("permissions", mapPermission);
		response.getWriter().write(objectMapper.writeValueAsString(mapRespon));
	}

	private void implementBean(HttpServletRequest request) {
		if (userService == null || bestWorkBeanUtils == null || permissionService == null || objectMapper == null) {
			ServletContext servletContext = request.getServletContext();
			WebApplicationContext webApplicationContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			assert webApplicationContext != null;
			userService = webApplicationContext.getBean(UserService.class);
			bestWorkBeanUtils = webApplicationContext.getBean(BestWorkBeanUtils.class);
			permissionService = webApplicationContext.getBean(PermissionService.class);
			objectMapper = webApplicationContext.getBean(ObjectMapper.class);
		}
	}

}
