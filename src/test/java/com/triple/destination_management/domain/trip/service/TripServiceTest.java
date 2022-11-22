package com.triple.destination_management.domain.trip.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.town.repository.TownRepository;
import com.triple.destination_management.domain.trip.dto.TripRequest;
import com.triple.destination_management.domain.trip.dto.TripResponse;
import com.triple.destination_management.global.config.JpaConfig;
import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

@ActiveProfiles("dev")
@DataJpaTest
@DisplayName("** [ TownServiceTest ] **")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class, TripService.class})
class TripServiceTest {

	@Autowired
	private TripService tripService;

	@Autowired
	private TownRepository townRepository;

	@Test
	@DisplayName("# [1-1] 여행 등록하기")
	void registerTown() {
		// given

		// when

		// then
		// assertThat(townResponse)
		// 	.isNotNull()
		// 	.hasFieldOrPropertyWithValue("name", "서울")
		// 	.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("# [1-2] 여행 등록 시 날짜 예외 (종료일 < 시작일)")
	void registerTripWithWrongDate() {
		// given

		// when

		// then
		// assertThat(townResponse)
		// 	.isNotNull()
		// 	.hasFieldOrPropertyWithValue("name", "서울")
		// 	.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("# [1-3] 여행 등록 시 날짜 예외 (이미 해당기간에 여행이 있을 경우)")
	void registerDuplicatedTrip() {
		// given

		// when

		// then
		// assertThat(townResponse)
		// 	.isNotNull()
		// 	.hasFieldOrPropertyWithValue("name", "서울")
		// 	.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("# [3-1] 여행 삭제하기")
	void removeTrip() {
		// given
		Town town = getTownEntity("서울", "대한민국");
		Town savedTown = townRepository.save(town);
		LocalDateTime start = getDateTime(5);
		LocalDateTime end = getDateTime(10);
		TripRequest tripRequest = getTripRequest(savedTown.getId(), start, end);
		TripResponse tripResponse = tripService.registerTrip(tripRequest);

		// when
		Long removedTripId = tripService.removeTrip(tripResponse.getId());

		// then
		assertThat(removedTripId)
			.isNotNull()
			.isEqualTo(tripResponse.getId());
	}

	@Test
	@DisplayName("# [3-2] 존재하지 않는 여행 삭제하기")
	void removeNotExistTrip() {
		// given
		Long tripId = -99999L;

		// when
		Throwable thrown = catchThrowable(() -> tripService.removeTrip(tripId));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("# [4-1] 단일 여행 조회하기")
	void findTrip() {
		// given
		Town town = getTownEntity("부산", "대한민국");
		Town savedTown = townRepository.save(town);
		LocalDateTime start = getDateTime(5);
		LocalDateTime end = getDateTime(10);
		TripRequest saveRequest = getTripRequest(savedTown.getId(), start, end);
		TripResponse saveResponse = tripService.registerTrip(saveRequest);

		// when
		TripResponse tripResponse = tripService.findTrip(saveResponse.getId());

		// then
		assertThat(tripResponse)
			.isNotNull()
			.hasFieldOrPropertyWithValue("id", saveResponse.getId())
			.hasFieldOrPropertyWithValue("startDate", start)
			.hasFieldOrPropertyWithValue("endDate", end)
			.hasFieldOrPropertyWithValue("name", "부산")
			.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("# [4-2] 존재하지 않는 여행 조회하기")
	void findNotExistTown() {
		// given
		Long notFoundId = -99999L;

		// when
		Throwable thrown = catchThrowable(() -> tripService.findTrip(notFoundId));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	private LocalDateTime getDateTime(int day) {
		return LocalDateTime.now().plusDays(day);
	}

	private Town getTownEntity(
		String name,
		String country
	) {
		return Town.builder()
			.name(name)
			.country(country)
			.build();
	}

	private TripRequest getTripRequest(
		Long townId,
		LocalDateTime start,
		LocalDateTime end
	) {
		return TripRequest.builder()
			.townId(townId)
			.startDate(start)
			.endDate(end)
			.build();
	}
}