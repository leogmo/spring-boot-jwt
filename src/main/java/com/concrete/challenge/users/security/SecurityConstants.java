package com.concrete.challenge.users.security;

import com.concrete.challenge.users.Constants;

public class SecurityConstants {
	public static final String SECRET = "1b0f044fae9414b";
    public static final long EXPIRATION_TIME = Constants.ONE_DAY_IN_MILLISECONDS; 
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
}
