package com.triple.destination_management.global.config.security.jwt.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import com.triple.destination_management.global.config.security.jwt.JwtProperties;
import com.triple.destination_management.global.config.security.jwt.JwtProvider;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final JwtProvider jwtProvider;

	public JwtAuthorizationFilter(
		AuthenticationManager authenticationManager,
		JwtProvider jwtProvider
	) {
		super(authenticationManager);
		this.jwtProvider = jwtProvider;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain
	) throws IOException, ServletException {
		String accessToken = getAccessToken(request);
		if (idValidAccessToken(accessToken))
			addAuthenticationTokenInSecurityContext(accessToken);
		chain.doFilter(request, response);
	}

	private void addAuthenticationTokenInSecurityContext(String accessToken) {
		Long userId = jwtProvider.getUserId(accessToken);
		String role = jwtProvider.getUserRole(accessToken);
		SecurityContextHolder.getContext().setAuthentication(getAuthenticationToken(userId, role));
	}

	private UsernamePasswordAuthenticationToken getAuthenticationToken(
		Long userId,
		String role
	) {
		if (userId != null)
			return new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority(role)));
		return null;
	}

	private String getAccessToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(JwtProperties.JWT_ACCESS_HEADER);
		if (verifyTextAndPrefix(bearerToken))
			return bearerToken.substring(7);
		else
			return null;
	}

	private boolean verifyTextAndPrefix(String bearerToken) {
		return StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtProperties.TOKEN_PREFIX);
	}

	private boolean idValidAccessToken(String accessToken) {
		return accessToken != null && jwtProvider.validateToken(accessToken);
	}
}
