package com.triple.destination_management.domain.town.dto;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

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
public class TownRequest {

	@NotBlank(message = "국가를 입력해주세요!")
	private String country;

	private String area;

	@NotBlank(message = "도시명을 입력해주세요!")
	private String name;

	public static Town dtoToEntity(TownRequest townRequest) {
		return Town.builder()
			.country(townRequest.getCountry())
			.area(townRequest.getArea())
			.name(townRequest.getName())
			.code(getHashCode(townRequest))
			.build();
	}

	public static Integer getHashCode(TownRequest townRequest) {
		return Objects.hash(townRequest.getName(), townRequest.getArea(), townRequest.getCountry());
	}
}
