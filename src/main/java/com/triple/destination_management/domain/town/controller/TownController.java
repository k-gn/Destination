package com.triple.destination_management.domain.town.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.triple.destination_management.domain.town.dto.TownRequest;
import com.triple.destination_management.domain.town.service.TownService;
import com.triple.destination_management.global.dto.ApiDataResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/towns")
public class TownController {

	private final TownService townService;

	/**
	 * 도시 등록하기
	 */
	@PostMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> registerTown(@Valid @RequestBody TownRequest townRequest) {
		return ResponseEntity.ok(ApiDataResponse.of(townService.registerTown(townRequest)));
	}

	/**
	 * 도시 수정하기
	 */
	@PutMapping("/{townId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> modifyTown(
		@PathVariable Long townId,
		@Valid @RequestBody TownRequest townRequest
	) {
		return ResponseEntity.ok(ApiDataResponse.of(townService.modifyTown(townId, townRequest)));
	}

	/**
	 * 도시 삭제하기
	 */
	@DeleteMapping("/{townId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> removeTown(@PathVariable Long townId) {
		return ResponseEntity.ok(ApiDataResponse.of(townService.removeTown(townId)));
	}

	/**
	 * 단일 도시 조회하기
	 */
	@GetMapping("/{townId}")
	public ResponseEntity<?> findTown(@PathVariable Long townId) {
		return ResponseEntity.ok(ApiDataResponse.of(townService.findTown(townId)));
	}

	/**
	 * 사용자별 도시 목록 조회하기
	 */
	@GetMapping
	public ResponseEntity<?> findAll(@AuthenticationPrincipal Long userId) {
		return ResponseEntity.ok(ApiDataResponse.of(townService.findTownsByUser(userId)));
	}
}
