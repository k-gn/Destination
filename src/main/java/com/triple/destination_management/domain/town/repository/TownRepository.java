package com.triple.destination_management.domain.town.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triple.destination_management.domain.town.entity.Town;

public interface TownRepository extends JpaRepository<Town, Long>, QueryDslTownRepository {

	Optional<Town> findTownByCode(Integer code);
}
