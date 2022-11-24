package com.nineplus.bestwork.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.header.Header;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nineplus.bestwork.entity.UserEntity;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.CommonConstants;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

	private String PREFIX_TOKEN;

	private String SECRET_KEY;

	private int JWT_EXPIRATION;

	public String PUBLIC_URL[];

	UserService userService;

	public CustomAuthorizationFilter(String prefixToken, String secretKey, String jwtExpiration, String[] publicUrl) {
		super();
		PREFIX_TOKEN = prefixToken;
		SECRET_KEY = secretKey;
		JWT_EXPIRATION = Integer.parseInt(jwtExpiration);
		this.PUBLIC_URL = Stream.of(publicUrl).map(item -> item.replace("/**", "")).toList()
				.toArray(new String[publicUrl.length]);
	}

	private Boolean isPublicUrl(String path) {
		for (String string : PUBLIC_URL) {
			if (path.startsWith(string)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String accessToken = request.getHeader(CommonConstants.Authentication.ACCESS_TOKEN);
		String refreshToken = request.getHeader(CommonConstants.Authentication.REFRESH_TOKEN);
		if (userService == null) {
			ServletContext servletContext = request.getServletContext();
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			userService = webApplicationContext.getBean(UserService.class);
		}
		if (isPublicUrl(request.getServletPath())) {
			this.addTokenToHeader(response, accessToken, refreshToken);
		} else {
			if (accessToken != null && request.getHeader(CommonConstants.Authentication.PREFIX_TOKEN) != null
					&& request.getHeader(CommonConstants.Authentication.PREFIX_TOKEN).startsWith(PREFIX_TOKEN)) {
				try {
					Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = verifier.verify(accessToken);
					String username = decodedJWT.getSubject();
					UserEntity user = userService.getUserByUsername(username);
					if (ObjectUtils.isEmpty(user) || userService.isBlocked(user.getLoginFailedNum())) {
						throw new Exception();
					}
					String[] roles = decodedJWT.getClaim(CommonConstants.Authentication.ROLES).asArray(String.class);
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
					for (String role : roles) {
						authorities.add(new SimpleGrantedAuthority(role));
					}
					UsernamePasswordAuthenticationToken authenToken = new UsernamePasswordAuthenticationToken(username,
							null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenToken);
					this.addTokenToHeader(response, accessToken, refreshToken);
				} catch (Exception exception) {
					response.setHeader("error", exception.getMessage());
					response.sendError(HttpStatus.FORBIDDEN.value());
				}
			} else if (refreshToken != null
					&& request.getHeader(CommonConstants.Authentication.PREFIX_TOKEN).startsWith(PREFIX_TOKEN)) {
				try {
					Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = verifier.verify(refreshToken);
					String username = decodedJWT.getSubject();
					UserEntity user = userService.getUserByUsername(username);
					if (ObjectUtils.isEmpty(user) || userService.isBlocked(user.getLoginFailedNum())) {
						throw new Exception();
					}
					String newAccessToken = JWT.create().withSubject(user.getUserName())
							.withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION * 1000L))
							.withIssuer(request.getRequestURL().toString())
							.withClaim(CommonConstants.Authentication.ROLES,
									new SimpleGrantedAuthority(user.getRole().getRoleName()).getAuthority())
							.sign(algorithm);
					this.addTokenToHeader(response, newAccessToken, refreshToken);
					response.addHeader(CommonConstants.Authentication.PREFIX_TOKEN, PREFIX_TOKEN);
				} catch (Exception exception) {
					response.setHeader("error", exception.getMessage());
					response.sendError(HttpStatus.FORBIDDEN.value());
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	private void addTokenToHeader(HttpServletResponse response, String accessToken, String refreshToken){
		response.setHeader(CommonConstants.Authentication.REFRESH_TOKEN,refreshToken);
		response.setHeader(CommonConstants.Authentication.ACCESS_TOKEN,accessToken);
		response.setHeader("Access-Control-Expose-Headers", CommonConstants.Authentication.REFRESH_TOKEN + "," +
				CommonConstants.Authentication.ACCESS_TOKEN + ", x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
				"Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
	}

}
