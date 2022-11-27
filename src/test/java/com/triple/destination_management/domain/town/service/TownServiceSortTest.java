package com.triple.destination_management.domain.town.service;

import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;

import com.triple.destination_management.domain.town.dto.TownRequest;
import com.triple.destination_management.domain.town.dto.TownResponse;
import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.trip.entity.Trip;
import com.triple.destination_management.domain.trip.repository.TripRepository;
import com.triple.destination_management.global.config.JpaConfig;
import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

@ActiveProfiles("test")
@DataJpaTest
@DisplayName("** [ TownServiceSortTest ] **")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class, TownService.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TownServiceSortTest {

	private final DataSource dataSource;

	private final TownService townService;

	public TownServiceSortTest(
		@Autowired DataSource dataSource,
		@Autowired TownService townService
	) {
		this.dataSource = dataSource;
		this.townService = townService;
	}

	@BeforeAll
	public void init() {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/h2/data.sql"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("# [1] 사용자별 도시 목록 조회")
	void given_when_then() {
		// given
		Long userId = 1L;
		
		// when
		List<TownResponse> townResponses = townService.findTownsByUser(userId);
		townResponses.forEach(System.out::println);

		// then
		assertThat(townResponses)
			.isNotNull()
			.hasSize(11)
			.element(0)
				.hasFieldOrPropertyWithValue("name", "대구")
				.hasFieldOrPropertyWithValue("country", "대한민국");

		List<String> names = List.of("대구", "제주", "포항", "전남", "충남", "충북", "전북", "광주", "수원", "대전", "서울");
		List<String> sortNames = townResponses.stream().map(TownResponse::getName).collect(Collectors.toList());

		assertThat(sortNames)
			.containsAll(names);
	}
}