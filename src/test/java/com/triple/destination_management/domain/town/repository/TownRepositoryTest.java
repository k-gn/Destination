package com.triple.destination_management.domain.town.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.triple.destination_management.domain.town.dto.TownFindDto;
import com.triple.destination_management.domain.town.dto.TownResponse;
import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.trip.entity.Trip;
import com.triple.destination_management.domain.trip.repository.TripRepository;
import com.triple.destination_management.domain.user.constants.Auth;
import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.domain.user.entity.UserSearch;
import com.triple.destination_management.domain.user.repository.UserRepository;
import com.triple.destination_management.domain.user.repository.UserSearchRepository;
import com.triple.destination_management.global.config.JpaConfig;

@ActiveProfiles("test")
@DataJpaTest
@DisplayName("** [ TownRepositoryTest ] **")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class})
class TownRepositoryTest {

	private final TownRepository townRepository;

	private final UserRepository userRepository;

	private final TripRepository tripRepository;

	private final UserSearchRepository userSearchRepository;

	TownRepositoryTest(
		@Autowired TownRepository townRepository,
		@Autowired UserRepository userRepository,
		@Autowired TripRepository tripRepository,
		@Autowired UserSearchRepository userSearchRepository
	) {
		this.townRepository = townRepository;
		this.userRepository = userRepository;
		this.tripRepository = tripRepository;
		this.userSearchRepository = userSearchRepository;
	}

	private final List<Town> towns = new ArrayList<>();

	private List<Town> savedTowns;

	@BeforeEach
	public void init() {
		List<String> names = List.of("대구", "대전", "부산", "제주", "충북", "충남", "전주", "포항", "경기", "전남");
		for (String name : names) {
			towns.add(getTown(name));
		}
		savedTowns = townRepository.saveAll(towns);
	}

	@Test
	@DisplayName("# [1] 코드로 도시 조회하기")
	void findTownByCode() {
		// given
		Town reqTown = getTown("서울");
		reqTown.setCode(1234);
		Town savedTown = townRepository.save(reqTown);

		// when
		Town town = townRepository.findTownByCode(savedTown.getCode()).orElse(null);

		// then
		assertThat(town)
			.isNotNull()
			.hasFieldOrPropertyWithValue("id", town.getId())
			.hasFieldOrPropertyWithValue("name", "서울")
			.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("# [2] 상위 도시 10개 조회하기")
	void findTownsByLimit() {
		// given

		// when
		List<TownResponse> townResponses = townRepository.findTownsByLimit();

		// then
		assertThat(townResponses)
			.isNotNull()
			.hasSize(10);
	}

	@Test
	@DisplayName("# [3] 무작위 도시 10개 조회하기")
	void findRandomTowns() {
		// given
		Integer size = 10;

		// when
		List<TownResponse> townResponses = townRepository.findRandomTowns(size);

		// then
		assertThat(townResponses)
			.isNotNull()
			.hasSize(10);
	}

	@Test
	@DisplayName("# [4] 여행중인 도시 조회하기")
	void findTravelingTowns() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		// 여행중인 도시
		Trip trip = getTrip(savedTowns.get(0), user, LocalDateTime.now().minusDays(1),
			LocalDateTime.now().plusDays(5));
		tripRepository.save(trip);

		// when
		List<TownResponse> townResponses = townRepository.findTravelingTowns(savedUser.getId());

		// then
		assertThat(townResponses)
			.isNotNull()
			.element(0)
			.hasFieldOrPropertyWithValue("name", savedTowns.get(0).getName())
			.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("# [5] 여행 예정인 도시 조회하기")
	void findScheduledTowns() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		Trip trip1 = getTrip(savedTowns.get(1), user, LocalDateTime.now().plusDays(5),
			LocalDateTime.now().plusDays(10));
		Trip trip2 = getTrip(savedTowns.get(2), user, LocalDateTime.now().plusDays(10),
			LocalDateTime.now().plusDays(15));
		tripRepository.save(trip1);
		tripRepository.save(trip2);

		TownFindDto townFindDto = TownFindDto.getTownFindDto(savedUser.getId(), List.of(savedTowns.get(1).getId()), 10);

		// when
		List<TownResponse> scheduledTowns = townRepository.findScheduledTowns(townFindDto);

		scheduledTowns.forEach(System.out::println);
		System.out.println("scheduledTowns.size() = " + scheduledTowns.size());

		// then
		assertThat(scheduledTowns)
			.isNotNull()
			.hasSize(1)
			.element(0)
			.hasFieldOrPropertyWithValue("name", savedTowns.get(2).getName())
			.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("# [6] 최근에 조회된 도시 조회하기")
	void findRecentSearchTowns() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		// 최근 조회한 도시
		UserSearch userSearch1 = getUserSearch(savedTowns.get(3), user);
		UserSearch userSearch2 = getUserSearch(savedTowns.get(4), user);
		userSearchRepository.save(userSearch1);
		userSearchRepository.save(userSearch2);

		TownFindDto townFindDto = TownFindDto.getTownFindDto(savedUser.getId(), List.of(savedTowns.get(4).getId()), 10);

		// when
		List<TownResponse> recentSearchTowns = townRepository.findRecentSearchTowns(townFindDto);

		// then
		assertThat(recentSearchTowns)
			.isNotNull()
			.hasSize(1)
			.element(0)
			.hasFieldOrPropertyWithValue("name", savedTowns.get(3).getName())
			.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("# [7] 최근에 등록된 도시 조회하기")
	void findRecentInsertTowns() {
		// given
		List<Long> ids = savedTowns.stream()
			.map(Town::getId)
			.filter(id -> id <= savedTowns.get(4).getId())
			.collect(Collectors.toList());

		TownFindDto townFindDto = TownFindDto.getTownFindDto(null, ids, 10);

		// when
		List<TownResponse> recentInsertTowns = townRepository.findRecentInsertTowns(townFindDto);

		// recentInsertTowns.forEach(System.out::println);

		// then
		assertThat(recentInsertTowns)
			.isNotNull()
			.hasSize(5)
			.element(0)
			.hasFieldOrPropertyWithValue("name", savedTowns.get(savedTowns.size() - 1).getName())
			.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	private User getUser() {
		return User.builder()
			.username("gyul")
			.password("1234")
			.name("김규남")
			.role(Auth.ROLE_USER)
			.build();
	}

	private Trip getTrip(
		Town town,
		User user,
		LocalDateTime start,
		LocalDateTime end
	) {
		return Trip.builder()
			.town(town)
			.user(user)
			.startDate(start)
			.endDate(end)
			.build();
	}

	private UserSearch getUserSearch(
		Town town,
		User user
	) {
		return UserSearch.builder()
			.user(user)
			.town(town)
			.build();
	}

	private Town getTown(String name) {
		return Town.builder()
			.name(name)
			.country("대한민국")
			.build();
	}
}