package com.triple.destination_management.domain.town.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.triple.destination_management.domain.town.dto.TownRequest;
import com.triple.destination_management.domain.town.dto.TownResponse;
import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.town.repository.TownRepository;
import com.triple.destination_management.domain.trip.entity.Trip;
import com.triple.destination_management.domain.trip.repository.TripRepository;
import com.triple.destination_management.global.config.JpaConfig;
import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

@ActiveProfiles("test")
@DataJpaTest
@DisplayName("** [ TownServiceTest ] **")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class, TownService.class})
class TownServiceTest {

	private final TownService townService;

	private final TripRepository tripRepository;

	public TownServiceTest(
		@Autowired TownService townService,
		@Autowired TripRepository tripRepository
	) {

		this.townService = townService;
		this.tripRepository = tripRepository;
	}

	@Test
	@DisplayName("# [1-1] 도시 등록하기")
	void registerTown() {
		// given
		TownRequest saveRequest = getTownRequest("서울", "대한민국");

		// when
		TownResponse townResponse = townService.registerTown(saveRequest);

		// then
		assertThat(townResponse)
			.isNotNull()
			.hasFieldOrPropertyWithValue("name", "서울")
			.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("# [1-2] 동일한 도시 중복 등록")
	void registerDuplicateTown() {
		// given
		TownRequest saveRequest = getTownRequest("서울", "대한민국");
		townService.registerTown(saveRequest);

		TownRequest duplicatedRequest = getTownRequest("서울", "대한민국");

		// when
		Throwable thrown = catchThrowable(() -> townService.registerTown(duplicatedRequest));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.DUPLICATED_REQUEST.getMessage());
	}

	@Test
	@DisplayName("# [2-1] 도시 수정하기")
	void modifyTown() {
		// given
		TownRequest saveRequest = getTownRequest("대구", "대한민국");
		TownResponse saveTownResponse = townService.registerTown(saveRequest);

		TownRequest modifyRequest = getTownRequest("대전", "대한민국");

		// when
		TownResponse townResponse = townService.modifyTown(saveTownResponse.getId(), modifyRequest);

		// then
		assertThat(townResponse)
			.isNotNull()
			.hasFieldOrPropertyWithValue("name", "대전")
			.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("# [2-2] 등록되지 않은 도시 수정하기")
	void modifyUnRegisteredTown() {
		// given
		TownRequest modifyRequest = getTownRequest("대전", "대한민국");

		// when
		Throwable thrown = catchThrowable(() -> townService.modifyTown(-99999L, modifyRequest));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("# [3-1] 도시 삭제하기")
	void removeTown() {
		// given
		TownRequest saveRequest = getTownRequest("대구", "대한민국");
		TownResponse saveTownResponse = townService.registerTown(saveRequest);

		// when
		Long removeTownId = townService.removeTown(saveTownResponse.getId());

		// then
		assertThat(removeTownId)
			.isNotNull()
			.isEqualTo(saveTownResponse.getId());

		Throwable thrown = catchThrowable(() -> townService.findTown(removeTownId));
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("# [3-2] 도시가 지정된 여행이 있을 경우 삭제")
	void removeDesignatedTownAsTrip() {
		// given
		TownRequest saveRequest = getTownRequest("서울", "대한민국");
		TownResponse saveTownResponse = townService.registerTown(saveRequest);

		Trip trip = getTrip(saveTownResponse.getId());
		tripRepository.save(trip);

		// when
		Throwable thrown = catchThrowable(() -> townService.removeTown(saveTownResponse.getId()));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.REMOVE_DEPENDENCY.getMessage());
	}

	@Test
	@DisplayName("# [3-3] 존재하지 않는 도시 삭제하기")
	void removeNotExistTown() {
		// given
		Long townId = -99999L;

		// when
		Throwable thrown = catchThrowable(() -> townService.removeTown(townId));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("# [4-1] 단일 도시 조회하기")
	void findTown() {
		// given
		TownRequest saveRequest = getTownRequest("서울", "대한민국");
		TownResponse saveTownResponse = townService.registerTown(saveRequest);

		// when
		TownResponse townResponse = townService.findTown(saveTownResponse.getId());

		// then
		assertThat(townResponse)
			.isNotNull()
			.hasFieldOrPropertyWithValue("id", saveTownResponse.getId())
			.hasFieldOrPropertyWithValue("name", "서울")
			.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("# [4-2] 존재하지 않는 도시 조회하기")
	void findNotExistTown() {
		// given
		Long notFoundId = -99999L;

		// when
		Throwable thrown = catchThrowable(() -> townService.findTown(notFoundId));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	private Trip getTrip(Long townId) {
		return Trip.builder()
			.town(Town.builder().id(townId).build())
			.build();
	}

	private TownRequest getTownRequest(
		String name,
		String country
	) {
		return TownRequest.builder()
			.name(name)
			.country(country)
			.build();
	}

	private Town getTown(String name) {
		return Town.builder()
			.name(name)
			.country("대한민국")
			.build();
	}
}