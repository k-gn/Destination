package com.triple.destination_management.domain.town.repository;

import java.util.List;

import com.triple.destination_management.domain.town.dto.TownFindDto;
import com.triple.destination_management.domain.town.dto.TownResponse;

public interface QueryDslTownRepository {

	List<TownResponse> findTownsByLimit();

	List<TownResponse> findTravelingTowns(Long userId);

	List<TownResponse> findScheduledTowns(TownFindDto getTownFindDto);

	List<TownResponse> findRecentSearchTowns(TownFindDto getTownFindDto);

	List<TownResponse> findRecentInsertTowns(TownFindDto getTownFindDto);

	List<TownResponse> findRandomTowns(Integer size);
}
