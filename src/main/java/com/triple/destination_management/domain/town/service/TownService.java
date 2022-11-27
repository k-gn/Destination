package com.triple.destination_management.domain.town.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.triple.destination_management.domain.town.dto.TownFindDto;
import com.triple.destination_management.domain.town.dto.TownRequest;
import com.triple.destination_management.domain.town.dto.TownResponse;
import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.town.exception.TownDependencyException;
import com.triple.destination_management.domain.town.exception.TownDuplicatedException;
import com.triple.destination_management.domain.town.exception.TownNotFoundException;
import com.triple.destination_management.domain.town.repository.TownRepository;
import com.triple.destination_management.domain.trip.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TownService {

	private final TownRepository townRepository;

	private final TripRepository tripRepository;

	private final Integer BASE_SIZE = 10;

	/**
	 * 도시 등록하기
	 */
	@Transactional
	public TownResponse registerTown(TownRequest townRequest) {
		Town town = TownRequest.dtoToEntity(townRequest);

		if (isDuplicatedTown(town))
			throw new TownDuplicatedException();

		Town savedTown = townRepository.save(town);
		return TownResponse.entityToDto(savedTown);
	}

	private boolean isDuplicatedTown(Town town) {
		return townRepository.findTownByCode(town.getCode()).isPresent();
	}

	/**
	 * 도시 수정하기
	 */
	@Transactional
	public TownResponse modifyTown(
		Long townId,
		TownRequest townRequest
	) {
		if (isDuplicatedTown(TownRequest.dtoToEntity(townRequest)))
			throw new TownDuplicatedException();

		Town town = getTownById(townId);
		town.setCountry(townRequest.getCountry());
		town.setArea(townRequest.getArea());
		town.setName(townRequest.getName());
		return TownResponse.entityToDto(town);
	}

	/**
	 * 도시 삭제하기
	 */
	@Transactional
	public Long removeTown(Long townId) {
		Town town = getTownById(townId);
		if (tripRepository.findFirstByTown(town).isEmpty()) {
			townRepository.delete(town);
			return town.getId();
		} else {
			throw new TownDependencyException();
		}
	}

	/**
	 * 단일 도시 조회하기
	 */
	public TownResponse findTown(Long townId) {
		Town town = getTownById(townId);
		return TownResponse.entityToDto(town);
	}

	private Town getTownById(Long townId) {
		return townRepository.findById(townId).orElseThrow(TownNotFoundException::new);
	}

	/**
	 * 사용자별 도시 목록 조회하기
	 */
	public List<TownResponse> findTownsByUser(Long userId) {
		if (userId == null) // 익명 사용자일 경우 기본 상위 10개 도시 반환
			return townRepository.findTownsByLimit();

		// 여행중인 도시
		List<TownResponse> travelingTowns = townRepository.findTravelingTowns(userId);
		List<TownResponse> townResponses = new ArrayList<>(travelingTowns);
		List<Long> townIds = townResponses.stream().map(TownResponse::getId).collect(Collectors.toList());

		// 여행 예정인 도시
		if (isFullSize(townResponses)) {
			List<TownResponse> scheduledTowns = getScheduledTowns(userId, travelingTowns, townResponses, townIds);
			townResponses.addAll(scheduledTowns);
			townIds.addAll(scheduledTowns.stream().map(TownResponse::getId).collect(Collectors.toList()));
		}

		// 최근 등록된 도시
		if (isFullSize(townResponses)) {
			List<TownResponse> recentInsertTowns = getRecentInsertTowns(travelingTowns, townResponses, townIds);
			townResponses.addAll(recentInsertTowns);
			townIds.addAll(recentInsertTowns.stream().map(TownResponse::getId).collect(Collectors.toList()));
		}

		// 최근 검색된 도시
		if (isFullSize(townResponses)) {
			List<TownResponse> recentSearchTowns = getRecentSearchTowns(userId, travelingTowns, townResponses, townIds);
			townResponses.addAll(recentSearchTowns);
			townIds.addAll(recentSearchTowns.stream().map(TownResponse::getId).collect(Collectors.toList()));
		}

		// 무작위 도시
		if (isFullSize(townResponses)) {
			List<TownResponse> randomTowns = getRandomTowns(travelingTowns, townResponses, townIds);
			townResponses.addAll(randomTowns);
		}

		return townResponses;
	}

	private boolean isFullSize(List<TownResponse> townResponses) {
		return townResponses.size() < BASE_SIZE;
	}

	private List<TownResponse> getRandomTowns(
		List<TownResponse> travelingTowns,
		List<TownResponse> townResponses,
		List<Long> townIds
	) {
		TownFindDto townFindDto = TownFindDto.getTownFindDto(null, townIds, getSize(travelingTowns, townResponses));
		return townRepository.findRandomTowns(townFindDto);
	}

	private List<TownResponse> getRecentSearchTowns(
		Long userId,
		List<TownResponse> travelingTowns,
		List<TownResponse> townResponses,
		List<Long> townIds
	) {
		TownFindDto townFindDto = TownFindDto.getTownFindDto(userId, townIds, getSize(travelingTowns, townResponses));
		return townRepository.findRecentSearchTowns(townFindDto);
	}

	private List<TownResponse> getRecentInsertTowns(
		List<TownResponse> travelingTowns,
		List<TownResponse> townResponses,
		List<Long> townIds
	) {
		TownFindDto townFindDto = TownFindDto.getTownFindDto(null, townIds, getSize(travelingTowns, townResponses));
		return townRepository.findRecentInsertTowns(townFindDto);
	}

	private List<TownResponse> getScheduledTowns(
		Long userId,
		List<TownResponse> travelingTowns,
		List<TownResponse> townResponses,
		List<Long> townIds
	) {
		TownFindDto townFindDto = TownFindDto.getTownFindDto(userId, townIds, getSize(travelingTowns, townResponses));
		return townRepository.findScheduledTowns(townFindDto);
	}

	private int getSize(
		List<TownResponse> travelingIds,
		List<TownResponse> townIds
	) {
		return BASE_SIZE + travelingIds.size() - townIds.size();
	}

}
