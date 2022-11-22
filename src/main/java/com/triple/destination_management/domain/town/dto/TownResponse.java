package com.triple.destination_management.domain.town.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.triple.destination_management.domain.town.entity.Town;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TownResponse {

	private Long id;

	private String country;

	private String area;

	private String name;

	public static TownResponse entityToDto(Town town) {
		return TownResponse.builder()
			.id(town.getId())
			.country(town.getCountry())
			.area(town.getArea())
			.name(town.getName())
			.build();
	}
}
