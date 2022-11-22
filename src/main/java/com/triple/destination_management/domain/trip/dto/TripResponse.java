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

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private String name;

	private String country;

	private String area;

	public static TripResponse entityToDto(Trip trip) {
		return TripResponse.builder()
			.id(trip.getId())
			.startDate(trip.getStartDate())
			.endDate(trip.getEndDate())
			.name(trip.getTown().getName())
			.country(trip.getTown().getCountry())
			.area(trip.getTown().getArea())
			.build();
	}
}
