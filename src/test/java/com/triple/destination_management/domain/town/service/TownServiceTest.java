package com.triple.destination_management.domain.town.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.triple.destination_management.domain.town.dto.TownRequest;
import com.triple.destination_management.domain.town.dto.TownResponse;
import com.triple.destination_management.global.config.JpaConfig;
import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

@ActiveProfiles("dev")
@DataJpaTest
@DisplayName("[TownServiceTest]")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class, TownService.class})
class TownServiceTest {

	@Autowired
	private TownService townService;

	@BeforeEach
	public void init() {
		TownRequest townRequest = TownRequest.builder()
			.name("서울")
			.country("대한민국")
			.build();
		townService.register(townRequest);
	}

	@Test
	@DisplayName("[1] 도시 등록하기")
	void register() {
		// given
		TownRequest townRequest = TownRequest.builder()
			.name("부산")
			.country("대한민국")
			.build();

		// when
		TownResponse townResponse = townService.register(townRequest);

		// then
		assertThat(townResponse)
			.isNotNull()
			.hasFieldOrPropertyWithValue("name", "부산")
			.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("[1-1] 동일한 도시 중복 등록")
	void registerDuplicate() {
		// given
		TownRequest townRequest = TownRequest.builder()
			.name("서울")
			.country("대한민국")
			.build();

		// when
		Throwable thrown = catchThrowable(() -> townService.register(townRequest));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.DUPLICATED_REQUEST.getMessage());
	}

	@Test
	@DisplayName("[2] 도시 수정하기")
	void modify() {
		// given
		TownRequest saveRequest = TownRequest.builder()
			.name("대구")
			.country("대한민국")
			.build();
		TownResponse saveTownResponse = townService.register(saveRequest);

		TownRequest modifyRequest = TownRequest.builder()
			.name("대전")
			.country("대한민국")
			.build();

		// when
		TownResponse townResponse = townService.modify(saveTownResponse.getId(), modifyRequest);

		// then
		assertThat(townResponse)
			.isNotNull()
			.hasFieldOrPropertyWithValue("name", "대전")
			.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("[2-1] 등록되지 않은 도시 수정하기")
	void modifyUnRegisteredTown() {
		// given
		TownRequest townRequest = TownRequest.builder()
			.name("대전")
			.country("대한민국")
			.build();

		// when
		Throwable thrown = catchThrowable(() -> townService.modify(99999L, townRequest));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}
}