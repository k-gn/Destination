package com.triple.destination_management.domain.user.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.triple.destination_management.domain.user.dto.UserRequest;
import com.triple.destination_management.domain.user.service.UserService;
import com.triple.destination_management.global.dto.ApiDataResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	/**
	 * 유저 회원가입하기
	 */
	@PostMapping
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest userRequest) {
		return ResponseEntity.ok(ApiDataResponse.of(userService.registerUser(userRequest)));
	}
}
