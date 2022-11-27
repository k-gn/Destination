package com.triple.destination_management.domain.user.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.town.repository.TownRepository;
import com.triple.destination_management.domain.user.constants.Auth;
import com.triple.destination_management.domain.user.dto.UserSearchResponse;
import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.domain.user.entity.UserSearch;
import com.triple.destination_management.domain.user.repository.UserRepository;
import com.triple.destination_management.domain.user.repository.UserSearchRepository;
import com.triple.destination_management.global.config.JpaConfig;
import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

@ActiveProfiles("test")
@DataJpaTest
@DisplayName("** [ UserSearchServiceTest ] **")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class, UserSearchService.class})
class UserSearchServiceTest {

	private final UserSearchService userSearchService;

	private final TownRepository townRepository;

	private final UserRepository userRepository;

	private final UserSearchRepository userSearchRepository;

	private final List<Town> towns = new ArrayList<>();

	private final List<String> townNames = List.of("서울", "부산", "대구", "포항", "제주");

	UserSearchServiceTest(
		@Autowired UserSearchService userSearchService,
		@Autowired TownRepository townRepository,
		@Autowired UserRepository userRepository,
		@Autowired UserSearchRepository userSearchRepository
	) {
		this.userSearchService = userSearchService;
		this.townRepository = townRepository;
		this.userRepository = userRepository;
		this.userSearchRepository = userSearchRepository;
	}

	@BeforeEach
	public void init() {
		for (String name : townNames) {
			Town town = getTown(name, "대한민국");
			Town savedTown = townRepository.save(town);
			towns.add(savedTown);
		}
	}

	@Test
	@DisplayName("# [1-1] 최근 검색어 등록하기")
	void registerSearch() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		Town town = getTown("서울", "대한민국");
		Town savedTown = townRepository.save(town);

		// when
		Long searchId = userSearchService.registerSearch(savedTown.getId(), savedUser.getId());

		// then
		assertThat(searchId).isNotNull();
		UserSearch userSearch = userSearchRepository.findById(searchId).orElse(null);

		assertThat(userSearch)
			.isNotNull()
			.hasFieldOrPropertyWithValue("user", savedUser)
			.hasFieldOrPropertyWithValue("town", savedTown);
	}

	@Test
	@DisplayName("# [1-2] 없는 도시로 최근 검색어 등록하기")
	void registerSearchWithNotExistTownId() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		Long townId = -99999L;

		// when
		Throwable thrown = catchThrowable(() -> userSearchService.registerSearch(townId, savedUser.getId()));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("# [1-3] 없는 유저로 최근 검색어 등록하기")
	void registerSearchWithNotExistUserId() {
		// given
		Long userId = -99999L;

		Town town = getTown("부산", "대한민국");
		Town savedTown = townRepository.save(town);

		// when
		Throwable thrown = catchThrowable(() -> userSearchService.registerSearch(savedTown.getId(), userId));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("# [2-1] 최근 검색어 목록 조회하기")
	void findSearchByUserId() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		for (Town town : towns) {
			UserSearch userSearch = getUserSearch(town, savedUser);
			userSearchRepository.save(userSearch);
		}

		// when
		List<UserSearchResponse> userSearchResponses = userSearchService.findSearch(savedUser.getId());

		// then
		assertThat(userSearchResponses)
			.isNotNull()
			.isNotEmpty()
			.hasSize(towns.size())
			.element(0)
			.hasFieldOrPropertyWithValue("name", towns.get(0).getName())
			.hasFieldOrPropertyWithValue("country", towns.get(0).getCountry());
	}

	@Test
	@DisplayName("# [2-2] 없는 유저로 최근 검색어 목록 조회하기")
	void findSearchByWrongUserId() {
		// given
		Long userId = -99999L;

		// when
		Throwable thrown = catchThrowable(() -> userSearchService.findSearch(userId));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	private Town getTown(
		String name,
		String country
	) {
		return Town.builder()
			.name(name)
			.country(country)
			.build();
	}

	private User getUser() {
		return User.builder()
			.username("gyul")
			.password("1234")
			.name("김규남")
			.role(Auth.ROLE_USER)
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
}