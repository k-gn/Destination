package com.triple.destination_management.domain.trip.controller;

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

import com.triple.destination_management.domain.trip.dto.TripRequest;
import com.triple.destination_management.domain.trip.dto.TripResponse;
import com.triple.destination_management.domain.trip.service.TripService;
import com.triple.destination_management.global.dto.ApiDataResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trips")
public class TripController {

	private final TripService tripService;

	/**
	 * 도시에 여행 등록하기
	 */
	@PostMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<ApiDataResponse<TripResponse>> registerTrip(
		@Valid @RequestBody TripRequest tripRequest,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(tripService.registerTrip(tripRequest, userId)));
	}

	/**
	 * 여행 수정하기
	 */
	@PutMapping("/{tripId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> modifyTrip(
		@PathVariable Long tripId,
		@AuthenticationPrincipal Long userId,
		@Valid @RequestBody TripRequest tripRequest
	) {
		return ResponseEntity.ok(ApiDataResponse.of(tripService.modifyTrip(tripId, userId, tripRequest)));
	}

	/**
	 * 여행 삭제하기
	 */
	@DeleteMapping("/{tripId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> removeTrip(
		@PathVariable Long tripId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(tripService.removeTrip(tripId, userId)));
	}

	/**
	 * 단일 여행 조회하기
	 */
	@GetMapping("/{tripId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> findTrip(
		@PathVariable Long tripId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(tripService.findTrip(tripId, userId)));
	}
}
