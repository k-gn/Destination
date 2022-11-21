package com.triple.destination_management.domain.town.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.triple.destination_management.domain.town.dto.TownModifyRequest;
import com.triple.destination_management.domain.town.dto.TownRequest;
import com.triple.destination_management.domain.town.service.TownService;
import com.triple.destination_management.global.dto.ApiDataResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/town")
public class TownController {

	private final TownService townService;

	/**
	 * 도시 등록하기
	 */
	@PostMapping
	public ResponseEntity<?> register(@Valid @RequestBody TownRequest townRequest) {
		return ResponseEntity.ok(ApiDataResponse.of(townService.register(townRequest)));
	}

	/**
	 * 도시 수정하기
	 */
	@PutMapping("/{townId}")
	public ResponseEntity<?> modify(
		@PathVariable Long townId,
		@RequestBody TownModifyRequest townModifyRequest
	) {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}

	/**
	 * 도시 삭제하기
	 */
	@DeleteMapping("/{townId}")
	public ResponseEntity<?> remove(@PathVariable Long townId) {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}

	/**
	 * 단일 도시 조회하기
	 */
	@GetMapping("/{townId}")
	public ResponseEntity<?> findOne(@PathVariable Long townId) {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}

	/**
	 * 사용자별 도시 목록 조회하기
	 */
	@GetMapping
	public ResponseEntity<?> findAll() {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}

}
