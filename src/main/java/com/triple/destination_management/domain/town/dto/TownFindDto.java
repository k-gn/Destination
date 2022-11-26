package com.triple.destination_management.domain.town.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TownFindDto {

	private Long userId;

	private List<Long> ids;

	private Integer size;

	public static TownFindDto getTownFindDto(
		Long userId,
		List<Long> ids,
		Integer size
	) {
		return TownFindDto.builder()
			.userId(userId)
			.ids(ids)
			.size(size)
			.build();
	}
}
