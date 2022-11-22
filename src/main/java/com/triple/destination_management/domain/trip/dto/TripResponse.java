package com.triple.destination_management.domain.trip.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.triple.destination_management.domain.trip.entity.Trip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripResponse {

	private Long id;

	private String startPoint;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private String name;

	private String country;

	private String area;

	public static TripResponse entityToDto(Trip trip) {
		return TripResponse.builder()
			.id(trip.getId())
			.startPoint(trip.getStartPoint())
			.startDate(trip.getStartDate())
			.endDate(trip.getEndDate())
			.name(trip.getDestination().getName())
			.country(trip.getDestination().getCountry())
			.area(trip.getDestination().getArea())
			.build();
	}
}
