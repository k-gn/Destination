package com.triple.destination_management.domain.user.repository;

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
import com.triple.destination_management.global.config.JpaConfig;

@ActiveProfiles("test")
@DataJpaTest
@DisplayName("** [ UserSearchRepositoryTest ] **")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class})
class UserSearchRepositoryTest {


	private final UserSearchRepository userSearchRepository;

	private final TownRepository townRepository;

	private final UserRepository userRepository;

	private final List<Town> towns = new ArrayList<>();

	private final List<String> townNames = List.of("서울", "부산", "대구", "포항", "제주");

	UserSearchRepositoryTest(
		@Autowired UserSearchRepository userSearchRepository,
		@Autowired TownRepository townRepository,
		@Autowired UserRepository userRepository
	) {
		this.userSearchRepository = userSearchRepository;
		this.townRepository = townRepository;
		this.userRepository = userRepository;
	}

	@BeforeEach
	public void init() {
		for (String name : townNames) {
			Town town = getTown(name);
			Town savedTown = townRepository.save(town);
			towns.add(savedTown);
		}
	}

	@Test
	@DisplayName("# [1] 최근 검색어 목록 조회하기")
	void findSearchByUserId() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		for (Town town : towns) {
			UserSearch userSearch = getUserSearch(town, savedUser);
			userSearchRepository.save(userSearch);
		}

		// when
		List<UserSearchResponse> userSearchResponses = userSearchRepository.findSearchByUserId(savedUser.getId());

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
	@DisplayName("# [2] 유저와 도시로 최근 검색어 조회하기")
	void findUserSearchByUserAndTown() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		for (Town town : towns) {
			UserSearch userSearch = getUserSearch(town, savedUser);
			userSearchRepository.save(userSearch);
		}

		// when
		UserSearch userSearch = userSearchRepository.findUserSearchByUserAndTown(savedUser, towns.get(0)).orElse(null);

		// then
		assertThat(userSearch)
			.isNotNull()
			.hasFieldOrPropertyWithValue("user", savedUser)
			.hasFieldOrPropertyWithValue("town", towns.get(0));
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

	private User getUser() {
		return User.builder()
			.username("gyul")
			.password("1234")
			.name("김규남")
			.role(Auth.ROLE_USER)
			.build();
	}
}