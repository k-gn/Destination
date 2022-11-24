package com.triple.destination_management.domain.trip.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import com.triple.destination_management.domain.trip.entity.Trip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripRequest {

	@NotNull(message = "목적지 아이디를 입력해주세요.")
	private Long townId;

	@NotNull(message = "날짜를 입력해주세요.")
	@FutureOrPresent(message = "올바른 날짜를 입력해주세요.")
	private LocalDateTime startDate;

	@NotNull(message = "날짜를 입력해주세요.")
	@Future(message = "올바른 날짜를 입력해주세요.")
	private LocalDateTime endDate;

	public static Trip dtoToEntity(TripRequest tripRequest) {
		return Trip.builder()
			.startDate(tripRequest.getStartDate())
			.endDate(tripRequest.getEndDate())
			.build();
	}
}
