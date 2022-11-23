package com.triple.destination_management.domain.trip.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.trip.entity.Trip;
import com.triple.destination_management.domain.user.entity.User;

public interface TripRepository extends JpaRepository<Trip, Long> {

	Optional<Trip> findFirstByTown(Town town);

	Optional<Trip> findTripByUserAndId(
		User user,
		Long tripId
	);
}
