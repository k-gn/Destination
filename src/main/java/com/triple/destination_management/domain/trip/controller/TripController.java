package com.triple.destination_management.domain.trip.controller;

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

import com.triple.destination_management.domain.trip.dto.TripRequest;
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
	public ResponseEntity<?> registerTrip(@Valid @RequestBody TripRequest tripRequest) {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}

	/**
	 * 여행 수정하기
	 */
	@PutMapping("/{tripId}")
	public ResponseEntity<?> modifyTrip(
		@PathVariable Long tripId,
		@Valid @RequestBody TripRequest tripRequest
	) {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}

	/**
	 * 여행 삭제하기
	 */
	@DeleteMapping("/{tripId}")
	public ResponseEntity<?> removeTrip(@PathVariable Long tripId) {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}

	/**
	 * 단일 여행 조회하기
	 */
	@GetMapping("/{tripId}")
	public ResponseEntity<?> findTrip(@PathVariable Long tripId) {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}
}
