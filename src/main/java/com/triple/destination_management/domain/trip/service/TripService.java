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
import com.triple.destination_management.domain.trip.exception.TripRemoveAuthException;
import com.triple.destination_management.domain.trip.repository.TripRepository;
import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.domain.user.exception.UserNotFoundException;
import com.triple.destination_management.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripService {

	private final TownRepository townRepository;

	private final TripRepository tripRepository;

	private final UserRepository userRepository;

	/**
	 * 여행 등록하기
	 */
	@Transactional
	public TripResponse registerTrip(
		TripRequest tripRequest,
		Long userId
	) {
		if (isStartDateAfterEndDate(tripRequest))
			throw new TripDateException();

		User user = getUserById(userId);
		Town town = getTownById(tripRequest.getTownId());

		Trip trip = TripRequest.dtoToEntity(tripRequest);
		trip.setTown(town);
		trip.setUser(user);

		Trip savedTrip = tripRepository.save(trip);
		return TripResponse.entityToDto(savedTrip);
	}

	/**
	 * 여행 수정하기
	 */
	@Transactional
	public TripResponse modifyTrip(
		Long tripId,
		Long userId,
		TripRequest tripRequest
	) {
		if (isStartDateAfterEndDate(tripRequest))
			throw new TripDateException();

		User user = getUserById(userId);
		Trip trip = getTripById(tripId);

		if (isNotSameUser(trip, user))
			throw new TripRemoveAuthException();

		trip.setStartDate(tripRequest.getStartDate());
		trip.setEndDate(tripRequest.getEndDate());

		Town town = getTownById(tripRequest.getTownId());
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
	public Long removeTrip(
		Long tripId,
		Long userId
	) {
		Trip trip = getTripById(tripId);
		User user = getUserById(userId);

		if (isNotSameUser(trip, user))
			throw new TripRemoveAuthException();

		tripRepository.delete(trip);
		return trip.getId();
	}

	private boolean isNotSameUser(
		Trip trip,
		User user
	) {
		return !trip.getUser().getId().equals(user.getId());
	}

	private User getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
	}

	/**
	 * 단일 여행 조회하기
	 */
	public TripResponse findTrip(
		Long tripId,
		Long userId
	) {
		User user = getUserById(userId);
		Trip trip = tripRepository.findTripByUserAndId(user, tripId).orElseThrow(TownNotFoundException::new);
		return TripResponse.entityToDto(trip);
	}

	private boolean isStartDateAfterEndDate(TripRequest tripRequest) {
		return tripRequest.getStartDate().isAfter(tripRequest.getEndDate());
	}

	private Town getTownById(Long townId) {
		return townRepository.findById(townId).orElseThrow(TownNotFoundException::new);
	}
}
