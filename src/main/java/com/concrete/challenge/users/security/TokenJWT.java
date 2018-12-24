package com.concrete.challenge.users.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.concrete.challenge.users.security.SecurityConstants.EXPIRATION_TIME;
import static com.concrete.challenge.users.security.SecurityConstants.SECRET;
import static com.concrete.challenge.users.security.SecurityConstants.TOKEN_PREFIX;

import java.util.Date;

import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.security.web.session.InvalidSessionAccessDeniedHandler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;

public class TokenJWT {
	
	public static String createToken(String username) {
		return TOKEN_PREFIX + JWT.create()
        .withSubject(username)
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .sign(HMAC512(SECRET.getBytes()));
	}
	
	public static String validateToken(String token) throws NonceExpiredException{
		String s = "";
		s = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
			        .build()
			        .verify(token.replace(TOKEN_PREFIX, ""))
			        .getSubject();
		return s;
	}
}
