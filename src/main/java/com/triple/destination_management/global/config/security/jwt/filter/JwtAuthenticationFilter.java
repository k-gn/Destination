package com.triple.destination_management.global.config.security.jwt.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triple.destination_management.domain.user.dto.UserLoginRequest;
import com.triple.destination_management.global.config.security.auth.PrincipalDetails;
import com.triple.destination_management.global.config.security.jwt.JwtProperties;
import com.triple.destination_management.global.config.security.jwt.JwtProvider;
import com.triple.destination_management.global.constants.ResponseCode;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final JwtProvider jwtProvider;

	public JwtAuthenticationFilter(
		AuthenticationManager authenticationManager,
		JwtProvider jwtProvider
	) {
		this.authenticationManager = authenticationManager;
		this.jwtProvider = jwtProvider;
	}

	@Override
	public Authentication attemptAuthentication(
		HttpServletRequest request,
		HttpServletResponse response
	) throws AuthenticationException {
		return authenticationManager.authenticate(getUsernamePasswordAuthenticationToken(getAdminLoginDto(request)));
	}

	@Override
	protected void successfulAuthentication(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain,
		Authentication authResult
	) throws IOException, ServletException {
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		response.addHeader(JwtProperties.JWT_ACCESS_HEADER, jwtProvider.createAccessToken(principalDetails.getUser()));
	}

	@Override
	protected void unsuccessfulAuthentication(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException failed
	) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.getMessage());
	}

	private UserLoginRequest getAdminLoginDto(HttpServletRequest request) {
		try {
			return objectMapper.readValue(request.getInputStream(), UserLoginRequest.class);
		} catch (IOException exception) {
			throw new AuthenticationCredentialsNotFoundException(ResponseCode.BAD_REQUEST.getMessage());
		}
	}

	private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(
		UserLoginRequest userLoginRequest
	) {
		return new UsernamePasswordAuthenticationToken(
			userLoginRequest.getUsername(),
			userLoginRequest.getPassword(),
			new ArrayList<>()
		);
	}
}
