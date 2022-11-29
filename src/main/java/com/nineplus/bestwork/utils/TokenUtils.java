package com.nineplus.bestwork.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class TokenUtils {
	@Value("${app.login.jwtSecretKey}")
	public String SECRET_KEY;

	public String getTokenFromRequest(HttpServletRequest request, String tokenName) {
		return request.getHeader(tokenName);
	}

	public String getUserNameFromToken(String token) {
		Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = verifier.verify(token);
		return decodedJWT.getSubject() != null ? decodedJWT.getSubject() : "";
	}

}
