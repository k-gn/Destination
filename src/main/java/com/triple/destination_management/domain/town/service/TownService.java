package com.triple.destination_management.domain.town.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
