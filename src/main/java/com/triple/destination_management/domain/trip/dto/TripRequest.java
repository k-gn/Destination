package com.triple.destination_management.domain.trip.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
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

	@NotBlank(message = "출발지를 입력해주세요.")
	private String startPoint;

	@FutureOrPresent(message = "여행 시작일은 현재 혹은 미래 날짜만 선택이 가능합니다.")
	private LocalDateTime startDate;

	@Future(message = "여행 종료일은 미래 날짜만 선택이 가능합니다.")
	private LocalDateTime endDate;

	public static Trip dtoToEntity(TripRequest tripRequest) {
		return Trip.builder()
			.startDate(tripRequest.getStartDate())
			.endDate(tripRequest.getEndDate())
			.build();
	}
}
