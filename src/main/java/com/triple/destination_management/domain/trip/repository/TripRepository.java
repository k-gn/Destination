package com.triple.destination_management.domain.trip.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.trip.entity.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {

	Optional<Trip> findFirstByTown(Town town);

	Optional<Trip> findFirstByStartDateGreaterThanEqualAndUser(
		LocalDateTime startDate,
		Long user
	);

	Optional<Trip> findFirstByEndDateLessThanEqualAndUser(
		LocalDateTime startDate,
		Long user
	);
}
