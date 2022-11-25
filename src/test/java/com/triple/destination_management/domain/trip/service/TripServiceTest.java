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
import com.triple.destination_management.domain.user.constants.Auth;
import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.domain.user.repository.UserRepository;
import com.triple.destination_management.global.config.JpaConfig;
import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

@ActiveProfiles("test")
@DataJpaTest
@DisplayName("** [ TripServiceTest ] **")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class, TripService.class})
class TripServiceTest {


	private final TripService tripService;

	private final TownRepository townRepository;

	private final UserRepository userRepository;

	TripServiceTest(
		@Autowired TripService tripService,
		@Autowired TownRepository townRepository,
		@Autowired UserRepository userRepository
	) {
		this.tripService = tripService;
		this.townRepository = townRepository;
		this.userRepository = userRepository;
	}

	@Test
	@DisplayName("# [1-1] 여행 등록하기")
	void registerTrip() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		Town town = getTownEntity("서울", "대한민국");
		Town savedTown = townRepository.save(town);

		LocalDateTime startDate = LocalDateTime.now().plusDays(5);
		LocalDateTime endDate = LocalDateTime.now().plusDays(10);
		TripRequest tripRequest = getTripRequest(savedTown.getId(), startDate, endDate);

		// when
		TripResponse tripResponse = tripService.registerTrip(tripRequest, savedUser.getId());

		// then
		assertThat(tripResponse)
			.isNotNull()
			.hasFieldOrPropertyWithValue("name", "서울")
			.hasFieldOrPropertyWithValue("startDate", startDate)
			.hasFieldOrPropertyWithValue("endDate", endDate)
			.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("# [1-2] 여행 등록 시 날짜 예외 (종료일 < 시작일)")
	void registerTripWithWrongDate() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		Town town = getTownEntity("서울", "대한민국");
		Town savedTown = townRepository.save(town);

		LocalDateTime startDate = LocalDateTime.now().plusDays(10);
		LocalDateTime endDate = LocalDateTime.now().plusDays(5);
		TripRequest tripRequest = getTripRequest(savedTown.getId(), startDate, endDate);

		// when
		Throwable thrown = catchThrowable(() -> tripService.registerTrip(tripRequest, savedUser.getId()));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.BAD_REQUEST.getMessage());
	}

	@Test
	@DisplayName("# [1-3] 없는 도시로 등록하기")
	void registerTripWithNotExistTownId() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		Long townId = -99999L;

		LocalDateTime startDate = LocalDateTime.now().plusDays(5);
		LocalDateTime endDate = LocalDateTime.now().plusDays(10);
		TripRequest tripRequest = getTripRequest(townId, startDate, endDate);

		// when
		Throwable thrown = catchThrowable(() -> tripService.registerTrip(tripRequest, savedUser.getId()));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("# [1-4] 없는 유저로 등록하기")
	void registerTripWithNotExistUserId() {
		// given
		Long userId = -99999L;

		Town town = getTownEntity("서울", "대한민국");
		Town savedTown = townRepository.save(town);

		LocalDateTime startDate = LocalDateTime.now().plusDays(5);
		LocalDateTime endDate = LocalDateTime.now().plusDays(10);
		TripRequest tripRequest = getTripRequest(savedTown.getId(), startDate, endDate);

		// when
		Throwable thrown = catchThrowable(() -> tripService.registerTrip(tripRequest, userId));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("# [2-1] 여행 수정하기")
	void modifyTrip() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		Town town = getTownEntity("부산", "대한민국");
		Town savedTown = townRepository.save(town);

		LocalDateTime startDate = LocalDateTime.now().plusDays(5);
		LocalDateTime endDate = LocalDateTime.now().plusDays(10);
		TripRequest tripRequest = getTripRequest(savedTown.getId(), startDate, endDate);
		TripResponse savedTripResponse = tripService.registerTrip(tripRequest, savedUser.getId());

		LocalDateTime modifyStartDate = LocalDateTime.now().plusDays(8);
		LocalDateTime modifyEndDate = LocalDateTime.now().plusDays(15);
		TripRequest modifyTripRequest = getTripRequest(savedTown.getId(), modifyStartDate, modifyEndDate);

		// when
		TripResponse tripResponse
			= tripService.modifyTrip(savedTripResponse.getId(), savedUser.getId(), modifyTripRequest);

		// then
		assertThat(tripResponse)
			.isNotNull()
			.hasFieldOrPropertyWithValue("id", savedTripResponse.getId())
			.hasFieldOrPropertyWithValue("name", "부산")
			.hasFieldOrPropertyWithValue("startDate", modifyStartDate)
			.hasFieldOrPropertyWithValue("endDate", modifyEndDate)
			.hasFieldOrPropertyWithValue("country", "대한민국");
	}

	@Test
	@DisplayName("# [2-2] 존재하지 않는 여행 수정하기")
	void modifyNotExistTrip() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		Town town = getTownEntity("부산", "대한민국");
		Town savedTown = townRepository.save(town);

		Long tripId = -99999L;
		LocalDateTime modifyStartDate = LocalDateTime.now().plusDays(8);
		LocalDateTime modifyEndDate = LocalDateTime.now().plusDays(15);
		TripRequest modifyTripRequest = getTripRequest(savedTown.getId(), modifyStartDate, modifyEndDate);

		// when
		Throwable thrown = catchThrowable(() -> tripService.modifyTrip(tripId, savedUser.getId(), modifyTripRequest));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("# [2-3] 잘못된 날짜로 여행 수정하기")
	void modifyTripWithWrongDate() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		Town town = getTownEntity("부산", "대한민국");
		Town savedTown = townRepository.save(town);

		LocalDateTime startDate = LocalDateTime.now().plusDays(5);
		LocalDateTime endDate = LocalDateTime.now().plusDays(10);
		TripRequest tripRequest = getTripRequest(savedTown.getId(), startDate, endDate);
		TripResponse savedTripResponse = tripService.registerTrip(tripRequest, savedUser.getId());

		LocalDateTime modifyStartDate = LocalDateTime.now().plusDays(20);
		LocalDateTime modifyEndDate = LocalDateTime.now().plusDays(15);
		TripRequest modifyTripRequest = getTripRequest(savedTown.getId(), modifyStartDate, modifyEndDate);

		// when
		Throwable thrown = catchThrowable(
			() -> tripService.modifyTrip(savedTripResponse.getId(), savedUser.getId(), modifyTripRequest));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.BAD_REQUEST.getMessage());
	}

	@Test
	@DisplayName("# [2-4] 존재하지 않는 도시의 여행 수정하기")
	void modifyTripWithNotExistTownId() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		Town town = getTownEntity("부산", "대한민국");
		Town savedTown = townRepository.save(town);

		LocalDateTime startDate = LocalDateTime.now().plusDays(5);
		LocalDateTime endDate = LocalDateTime.now().plusDays(10);
		TripRequest tripRequest = getTripRequest(savedTown.getId(), startDate, endDate);
		TripResponse savedTripResponse = tripService.registerTrip(tripRequest, savedUser.getId());

		Long townId = -99999L;
		LocalDateTime modifyStartDate = LocalDateTime.now().plusDays(8);
		LocalDateTime modifyEndDate = LocalDateTime.now().plusDays(15);
		TripRequest modifyTripRequest = getTripRequest(townId, modifyStartDate, modifyEndDate);

		// when
		Throwable thrown = catchThrowable(
			() -> tripService.modifyTrip(savedTripResponse.getId(), savedUser.getId(), modifyTripRequest));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("# [2-5] 존재하지 않는 사용자의 여행 수정하기")
	void modifyTripWithNotExistUserId() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		Town town = getTownEntity("부산", "대한민국");
		Town savedTown = townRepository.save(town);

		LocalDateTime startDate = LocalDateTime.now().plusDays(5);
		LocalDateTime endDate = LocalDateTime.now().plusDays(10);
		TripRequest tripRequest = getTripRequest(savedTown.getId(), startDate, endDate);
		TripResponse savedTripResponse = tripService.registerTrip(tripRequest, savedUser.getId());

		Long userId = -99999L;
		LocalDateTime modifyStartDate = LocalDateTime.now().plusDays(8);
		LocalDateTime modifyEndDate = LocalDateTime.now().plusDays(15);
		TripRequest modifyTripRequest = getTripRequest(savedTown.getId(), modifyStartDate, modifyEndDate);

		// when
		Throwable thrown = catchThrowable(
			() -> tripService.modifyTrip(savedTripResponse.getId(), userId, modifyTripRequest));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("# [2-6] 다른 사용자의 여행 수정하기")
	void modifyTripWithOtherUserId() {
		// given
		User user1 = getUser("user1", "1234", "유저1");
		User savedUser1 = userRepository.save(user1);

		User user2 = getUser("user2", "1234", "유저2");
		User savedUser2 = userRepository.save(user2);

		Town town = getTownEntity("부산", "대한민국");
		Town savedTown = townRepository.save(town);

		LocalDateTime startDate = LocalDateTime.now().plusDays(5);
		LocalDateTime endDate = LocalDateTime.now().plusDays(10);
		TripRequest tripRequest = getTripRequest(savedTown.getId(), startDate, endDate);
		TripResponse savedTripResponse = tripService.registerTrip(tripRequest, savedUser1.getId());

		LocalDateTime modifyStartDate = LocalDateTime.now().plusDays(8);
		LocalDateTime modifyEndDate = LocalDateTime.now().plusDays(15);
		TripRequest modifyTripRequest = getTripRequest(savedTown.getId(), modifyStartDate, modifyEndDate);

		// when
		Throwable thrown = catchThrowable(
			() -> tripService.modifyTrip(savedTripResponse.getId(), savedUser2.getId(), modifyTripRequest));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.ACCESS_DENIED.getMessage());
	}

	@Test
	@DisplayName("# [3-1] 여행 삭제하기")
	void removeTrip() {
		// given
		Town town = getTownEntity("서울", "대한민국");
		Town savedTown = townRepository.save(town);

		User user = getUser();
		User savedUser = userRepository.save(user);

		LocalDateTime start = getDateTime(5);
		LocalDateTime end = getDateTime(10);
		TripRequest tripRequest = getTripRequest(savedTown.getId(), start, end);
		TripResponse tripResponse = tripService.registerTrip(tripRequest, savedUser.getId());

		// when
		Long removedTripId = tripService.removeTrip(tripResponse.getId(), savedUser.getId());

		// then
		assertThat(removedTripId)
			.isNotNull()
			.isEqualTo(tripResponse.getId());
	}

	@Test
	@DisplayName("# [3-2] 존재하지 않는 여행 삭제하기")
	void removeNotExistTrip() {
		// given
		User user1 = getUser("user1", "1234", "유저1");
		User savedUser1 = userRepository.save(user1);

		User user2 = getUser("user2", "1234", "유저2");
		User savedUser2 = userRepository.save(user2);

		Town town = getTownEntity("서울", "대한민국");
		Town savedTown = townRepository.save(town);

		LocalDateTime start = getDateTime(5);
		LocalDateTime end = getDateTime(10);
		TripRequest tripRequest = getTripRequest(savedTown.getId(), start, end);
		TripResponse tripResponse = tripService.registerTrip(tripRequest, savedUser1.getId());

		// when
		Throwable thrown = catchThrowable(() -> tripService.removeTrip(tripResponse.getId(), savedUser2.getId()));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.ACCESS_DENIED.getMessage());
	}

	@Test
	@DisplayName("# [3-2] 존재하지 않는 여행 삭제하기")
	void removeTripWithOtherUserId() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		Long tripId = -99999L;

		// when
		Throwable thrown = catchThrowable(() -> tripService.removeTrip(tripId, savedUser.getId()));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	private User getUser() {
		return User.builder().username("gyul").password("1234").name("김규남").role(Auth.ROLE_USER).build();
	}

	private User getUser(
		String username,
		String password,
		String name
	) {
		return User.builder().username(username).password(password).name(name).role(Auth.ROLE_USER).build();
	}

	@Test
	@DisplayName("# [4-1] 단일 여행 조회하기")
	void findTrip() {
		// given
		Town town = getTownEntity("부산", "대한민국");
		Town savedTown = townRepository.save(town);

		User user = getUser();
		User savedUser = userRepository.save(user);

		LocalDateTime start = getDateTime(5);
		LocalDateTime end = getDateTime(10);
		TripRequest saveRequest = getTripRequest(savedTown.getId(), start, end);
		TripResponse saveResponse = tripService.registerTrip(saveRequest, savedUser.getId());

		// when
		TripResponse tripResponse = tripService.findTrip(saveResponse.getId(), savedUser.getId());

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
	@DisplayName("# [4-2] 존재하지 않는 단일 여행 조회하기")
	void findNotExistTown() {
		// given
		Long notFoundId = -99999L;

		User user = getUser();
		User savedUser = userRepository.save(user);

		// when
		Throwable thrown = catchThrowable(() -> tripService.findTrip(notFoundId, savedUser.getId()));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("# [4-3] 잘못된 유저 아이디로 단일 여행 조회하기")
	void findTownWithoutWrongUserId() {
		// given
		Town town = getTownEntity("부산", "대한민국");
		Town savedTown = townRepository.save(town);

		User user = getUser();
		User savedUser = userRepository.save(user);

		LocalDateTime start = getDateTime(5);
		LocalDateTime end = getDateTime(10);
		TripRequest saveRequest = getTripRequest(savedTown.getId(), start, end);
		TripResponse saveResponse = tripService.registerTrip(saveRequest, savedUser.getId());

		Long userId = -99999L;

		// when
		Throwable thrown = catchThrowable(() -> tripService.findTrip(saveResponse.getId(), userId));

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