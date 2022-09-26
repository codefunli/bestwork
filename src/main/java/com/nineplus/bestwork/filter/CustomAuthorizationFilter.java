package com.nineplus.bestwork.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nineplus.bestwork.entity.TUser;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.CommonConstants;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

	private String PREFIX_TOKEN;

	private String SECRET_KEY;

	private int JWT_EXPIRATION;

	public String PUBLIC_URL[];

	@Autowired
	UserService userService;

	public CustomAuthorizationFilter(String prefixToken, String secretKey, String jwtExpiration, String publicUrl[]) {
		super();
		PREFIX_TOKEN = prefixToken;
		SECRET_KEY = secretKey;
		JWT_EXPIRATION = Integer.parseInt(jwtExpiration);
		this.PUBLIC_URL = Stream.of(publicUrl).map(item -> item.replace("/**", "")).collect(Collectors.toList())
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

		if (isPublicUrl(request.getServletPath())) {
			filterChain.doFilter(request, response);
		} else {
			Cookie[] requestCookie = request.getCookies();
			Cookie accessCookie = null;
			Cookie refreshCookie = null;
			if (requestCookie != null) {
				for (Cookie cookie : requestCookie) {
					if (cookie.getName().equals(CommonConstants.Authentication.ACCESS_COOKIE)) {
						accessCookie = cookie;
					}
					if (cookie.getName().equals(CommonConstants.Authentication.REFRESH_COOKIE)) {
						refreshCookie = cookie;
					}

				}
			}
			if (accessCookie != null && request.getHeader(CommonConstants.Authentication.PREFIX_TOKEN) != null
					&& request.getHeader(CommonConstants.Authentication.PREFIX_TOKEN).startsWith(PREFIX_TOKEN)) {
				try {
					String token = accessCookie.getValue();
					Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = verifier.verify(token);
					String username = decodedJWT.getSubject();
					TUser user = userService.getUserByUsername(username);
					if (ObjectUtils.isEmpty(user) || userService.isBlocked(user.getCountLoginFailed())) {
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
					filterChain.doFilter(request, response);
				} catch (Exception exception) {
					response.setHeader("error", exception.getMessage());
					response.sendError(HttpStatus.FORBIDDEN.value());
				}
			} else if (refreshCookie != null
					&& request.getHeader(CommonConstants.Authentication.PREFIX_TOKEN).startsWith(PREFIX_TOKEN)) {
				try {
					String token = refreshCookie.getValue();
					Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = verifier.verify(token);
					String username = decodedJWT.getSubject();
					TUser user = userService.getUserByUsername(username);
					if (ObjectUtils.isEmpty(user) || userService.isBlocked(user.getCountLoginFailed())) {
						throw new Exception();
					}
					String accessToken = JWT.create().withSubject(user.getUserName())
							.withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION * 1000))
							.withIssuer(request.getRequestURL().toString())
							.withClaim(CommonConstants.Authentication.ROLES,
									new SimpleGrantedAuthority(user.getRole().getRoleName()).getAuthority())
							.sign(algorithm);
					Cookie accessCookieN = new Cookie(CommonConstants.Authentication.ACCESS_COOKIE, accessToken);
					accessCookieN.setHttpOnly(true);
					accessCookieN.setSecure(false);
					accessCookieN.setMaxAge(JWT_EXPIRATION * 1000);
					response.addCookie(refreshCookie);
					response.addCookie(accessCookieN);
					response.addHeader(CommonConstants.Authentication.PREFIX_TOKEN, PREFIX_TOKEN);
				} catch (Exception exception) {
					response.setHeader("error", exception.getMessage());
					response.sendError(HttpStatus.FORBIDDEN.value());
				}
			} else {
				filterChain.doFilter(request, response);
			}
		}

	}

}
