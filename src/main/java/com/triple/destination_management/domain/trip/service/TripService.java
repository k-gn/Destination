package com.triple.destination_management.domain.trip.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.town.exception.TownNotFoundException;
import com.triple.destination_management.domain.town.repository.TownRepository;
import com.triple.destination_management.domain.trip.dto.TripRequest;
import com.triple.destination_management.domain.trip.dto.TripResponse;
import com.triple.destination_management.domain.trip.entity.Trip;
import com.triple.destination_management.domain.trip.exception.TripDateException;
import com.triple.destination_management.domain.trip.exception.TripNotFoundException;
import com.triple.destination_management.domain.trip.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripService {

	private final TownRepository townRepository;

	private final TripRepository tripRepository;

	/**
	 * 여행 등록하기
	 */
	@Transactional
	public TripResponse registerTrip(TripRequest tripRequest) {
		if (isStartDateAfterEndDate(tripRequest))
			throw new TripDateException();

		Town town = getTownById(tripRequest.getTownId());
		Trip trip = TripRequest.dtoToEntity(tripRequest);
		trip.setTown(town);
		Trip savedTrip = tripRepository.save(trip);

		return TripResponse.entityToDto(savedTrip);
	}

	/**
	 * 여행 수정하기
	 */
	@Transactional
	public TripResponse modifyTrip(
		Long tripId,
		TripRequest tripRequest
	) {
		if (isStartDateAfterEndDate(tripRequest))
			throw new TripDateException();

		Town town = getTownById(tripRequest.getTownId());
		Trip trip = getTripById(tripId);
		trip.setStartDate(tripRequest.getStartDate());
		trip.setStartDate(tripRequest.getEndDate());
		trip.setTown(town);

		return TripResponse.entityToDto(trip);
	}

	private Trip getTripById(Long tripId) {
		return tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);
	}

	/**
	 * 여행 삭제하기
	 */
	@Transactional
	public Long removeTrip(Long tripId) {
		Trip trip = getTripById(tripId);
		tripRepository.delete(trip);
		return trip.getId();
	}

	/**
	 * 단일 여행 조회하기
	 */
	public void findTrip() {

	}

	private boolean isStartDateAfterEndDate(TripRequest tripRequest) {
		return tripRequest.getStartDate().isAfter(tripRequest.getEndDate());
	}

	private Town getTownById(Long townId) {
		return townRepository.findById(townId).orElseThrow(TownNotFoundException::new);
	}
}
