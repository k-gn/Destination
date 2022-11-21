package com.triple.destination_management.domain.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripRequest {

	private String startDateTime;

	private String endDateTime;
}
