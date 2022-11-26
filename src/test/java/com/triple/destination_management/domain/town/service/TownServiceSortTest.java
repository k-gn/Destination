package com.triple.destination_management.domain.town.service;

import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;

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

	private final TripRepository tripRepository;

	public TownServiceSortTest(
		@Autowired DataSource dataSource,
		@Autowired TownService townService,
		@Autowired TripRepository tripRepository
	) {
		this.dataSource = dataSource;
		this.townService = townService;
		this.tripRepository = tripRepository;
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
	@DisplayName("1. 제목")
	void given_when_then() {

		townService.findTownsByUser(1L);
	}
}