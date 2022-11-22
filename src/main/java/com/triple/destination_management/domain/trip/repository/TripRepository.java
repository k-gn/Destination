package com.triple.destination_management.domain.trip.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.trip.entity.Trip;
import com.triple.destination_management.domain.user.entity.User;

public interface TripRepository extends JpaRepository<Trip, Long> {

	Optional<Trip> findFirstByDestination(Town destination);

	Optional<Trip> findFirstByStartDateLessThanEqualAndUser(
		LocalDateTime startDate,
		User user
	);

	Optional<Trip> findFirstByEndDateGreaterThanEqualAndUser(
		LocalDateTime startDate,
		User user
	);
}
