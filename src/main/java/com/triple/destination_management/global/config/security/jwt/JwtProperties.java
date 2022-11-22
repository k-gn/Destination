package com.triple.destination_management.global.config.security.jwt;

public class JwtProperties {
	public static final int ACCESS_EXPIRATION_TIME = 600000 * 6 * 24 * 2; // 48 hour
	public static final String JWT_ACCESS_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
}
