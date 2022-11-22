package com.triple.destination_management.domain.town.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.triple.destination_management.domain.town.dto.TownRequest;
import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.global.config.JpaConfig;

@ActiveProfiles("dev")
@DataJpaTest
@DisplayName("** [ TownRepositoryTest ] **")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class})
class TownRepositoryTest {

	@Autowired
	private TownRepository townRepository;

	@Test
	@DisplayName("# [1] 코드로 도시 조회하기")
	void findTownByCode() {
		// given
		TownRequest townRequest = TownRequest.builder()
			.name("서울")
			.country("대한민국")
			.build();
		Town savedTown = townRepository.save(TownRequest.dtoToEntity(townRequest));

		// when
		Town town = townRepository.findTownByCode(savedTown.getCode()).orElse(null);

		// then
		assertThat(town)
			.isNotNull()
			.hasFieldOrPropertyWithValue("id", town.getId())
			.hasFieldOrPropertyWithValue("name", "서울")
			.hasFieldOrPropertyWithValue("country", "대한민국");
	}
}