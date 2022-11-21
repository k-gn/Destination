package com.triple.destination_management.domain.trip.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.trip.entity.Trip;

public interface TripRepository extends JpaRepository<Trip, Long>, QueryDslTripRepository {

	Optional<Trip> findFirstByTown(Town town);
}
