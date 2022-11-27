package com.triple.destination_management.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.triple.destination_management.domain.user.service.UserSearchService;
import com.triple.destination_management.global.dto.ApiDataResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class UserSearchController {

	private final UserSearchService userSearchService;

	/**
	 * 최근 검색도시 저장하기
	 */
	@PostMapping("/{townId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> registerSearch(
		@PathVariable Long townId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(userSearchService.registerSearch(townId, userId)));
	}

	/**
	 * 최근 검색도시 조회하기
	 */
	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> findSearch(@AuthenticationPrincipal Long userId) {
		return ResponseEntity.ok(ApiDataResponse.of(userSearchService.findSearch(userId)));
	}
}
