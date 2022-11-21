package com.triple.destination_management.domain.town.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TownResponse {

	private Long id;

	private String country;

	private String name;
}
