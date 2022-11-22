package com.triple.destination_management.global.config.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.triple.destination_management.domain.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

	private final String USER_ID = "uid";
	private final String USER_ROLE = "role";

	private final Key key;

	public JwtProvider(@Value("${jwt.secret}") String secretKey) {
		byte[] bytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(bytes);
	}

	public Long getUserId(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get(USER_ID, Long.class);
	}

	public String createAccessToken(User user) {
		Date now = new Date();
		return Jwts.builder()
			.setClaims(getClaims(user))
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + JwtProperties.ACCESS_EXPIRATION_TIME))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	private Claims getClaims(User user) {
		Claims accessTokenClaims = Jwts.claims().setSubject(String.valueOf(user.getId()));
		accessTokenClaims.put(USER_ID, user.getId());
		accessTokenClaims.put(USER_ROLE, user.getRole().name());
		return accessTokenClaims;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getUserRole(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get(USER_ROLE, String.class);
	}
}
