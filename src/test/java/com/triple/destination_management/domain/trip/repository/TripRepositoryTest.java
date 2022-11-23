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
import com.triple.destination_management.domain.user.constants.Auth;
import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.domain.user.repository.UserRepository;
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

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("# [1] 도시로 단일 여행 조회하기")
	void findFirstTripByTown() {
		// given
		Town town = getTown();
		Town savedTown = townRepository.save(town);

		Trip trip = getTrip(savedTown);
		Trip savedTrip = tripRepository.save(trip);

		// when
		Trip dbTrip = tripRepository.findFirstByTown(savedTown).orElse(null);

		// then
		assertThat(dbTrip)
			.isNotNull()
			.hasFieldOrPropertyWithValue("id", savedTrip.getId())
			.hasFieldOrPropertyWithValue("town", savedTown);
	}

	private Town getTown() {
		return Town.builder()
			.name("서울")
			.country("대한민국")
			.build();
	}

	@Test
	@DisplayName("# [2] 유저와 여행 아이디로 조회하기")
	void findTripByUserAndId() {
		// given
		Town town = getTown();
		Town savedTown = townRepository.save(town);

		User user = getUser();
		User savedUser = userRepository.save(user);

		Trip trip = getTrip(savedTown, savedUser);
		Trip savedTrip = tripRepository.save(trip);

		// when
		Trip dbTrip = tripRepository.findTripByUserAndId(savedUser, savedTrip.getId()).orElse(null);

		// then
		assertThat(dbTrip)
			.isNotNull()
			.hasFieldOrPropertyWithValue("id", savedTrip.getId())
			.hasFieldOrPropertyWithValue("town", savedTown);
	}

	private Trip getTrip(Town town) {
		return Trip.builder()
			.town(town)
			.build();
	}

	private Trip getTrip(Town town, User user) {
		return Trip.builder()
			.town(town)
			.user(user)
			.build();
	}

	private User getUser() {
		return User.builder().username("gyul").password("1234").name("김규남").role(Auth.ROLE_USER).build();
	}
}