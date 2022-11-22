package com.triple.destination_management.domain.trip.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.town.repository.TownRepository;
import com.triple.destination_management.domain.trip.entity.Trip;
import com.triple.destination_management.global.config.JpaConfig;

@ActiveProfiles("dev")
@DataJpaTest
@DisplayName("** [ TripRepositoryTest ] **")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class})
class TripRepositoryTest {

	@Autowired
	private TripRepository tripRepository;

	@Autowired
	private TownRepository townRepository;

	@Test
	@DisplayName("# [1] 도시로 단일 여행 조회하기")
	void findFirstTripByTown() {
		// given
		Town town = Town.builder()
			.name("서울")
			.country("대한민국")
			.build();
		Town savedTown = townRepository.save(town);

		Trip trip = Trip.builder()
			.destination(savedTown)
			.build();
		Trip savedTrip = tripRepository.save(trip);

		// when
		Trip dbTrip = tripRepository.findFirstByDestination(savedTown).orElse(null);

		// then
		assertThat(dbTrip)
			.isNotNull()
			.hasFieldOrPropertyWithValue("id", savedTrip.getId())
			.hasFieldOrPropertyWithValue("town", savedTown);
	}
}