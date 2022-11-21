package com.triple.destination_management.domain.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triple.destination_management.domain.trip.entity.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {
}
