package com.triple.destination_management.domain.trip.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triple.destination_management.domain.trip.dto.TripRequest;
import com.triple.destination_management.domain.trip.dto.TripResponse;
import com.triple.destination_management.domain.trip.service.TripService;
import com.triple.destination_management.domain.user.constants.Auth;
import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.global.config.security.jwt.JwtProperties;
import com.triple.destination_management.global.config.security.jwt.JwtProvider;
import com.triple.destination_management.global.constants.ResponseCode;

@ActiveProfiles("dev")
@DisplayName("** [ TripControllerTest ] **")
@WebMvcTest(TripController.class)
@Import({JwtProvider.class})
class TripControllerTest {

	private final MockMvc mvc;

	private final ObjectMapper objectMapper;

	private final JwtProvider jwtProvider;

	private String token;

	@MockBean
	private TripService tripService;

	public TripControllerTest(
		@Autowired MockMvc mvc,
		@Autowired ObjectMapper objectMapper,
		@Autowired JwtProvider jwtProvider
	) {
		this.mvc = mvc;
		this.objectMapper = objectMapper;
		this.jwtProvider = jwtProvider;
	}

	@BeforeEach
	public void initToken() {
		User user = User.builder()
			.id(1L)
			.username("gyul")
			.password("1234")
			.name("김규남")
			.role(Auth.ROLE_USER)
			.build();

		token = jwtProvider.createAccessToken(user);
	}

	@Test
	@DisplayName("# [1-1] 여행 등록하기")
	void registerTrip() throws Exception {
		// given
		Long userId = 1L;
		LocalDateTime startDate = LocalDateTime.now().plusDays(5);
		LocalDateTime endDate = LocalDateTime.now().plusDays(10);
		TripRequest tripRequest = getTripRequest(startDate, endDate);
		TripResponse tripResponse = getTripResponse(startDate, endDate, "서울", "대한민국");
		given(tripService.registerTrip(tripRequest, userId)).willReturn(tripResponse);

		// when & then
		mvc.perform(post("/api/v1/trips")
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token)
			.content(objectMapper.writeValueAsString(tripRequest)))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data.id").value(tripResponse.getId()))
			.andExpect(jsonPath("$.data.name").value(tripResponse.getName()))
			.andExpect(jsonPath("$.data.country").value(tripResponse.getCountry()))
			.andExpect(jsonPath("$.data.startDate").value(tripResponse.getStartDate().toString()))
			.andExpect(jsonPath("$.data.endDate").value(tripResponse.getEndDate().toString()))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;

		then(tripService).should().registerTrip(tripRequest, userId);
	}

	@Test
	@DisplayName("# [2-1] 여행 수정하기")
	void modifyTrip() throws Exception {
		// given
		Long userId = 1L;
		Long tripId = 1L;
		LocalDateTime startDate = LocalDateTime.now().plusDays(5);
		LocalDateTime endDate = LocalDateTime.now().plusDays(10);
		TripRequest tripRequest = getTripRequest(startDate, endDate);
		TripResponse tripResponse = getTripResponse(startDate, endDate, "서울", "대한민국");
		given(tripService.modifyTrip(tripId, userId, tripRequest)).willReturn(tripResponse);

		// when & then
		mvc.perform(put("/api/v1/trips/" + tripId)
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token)
			.content(objectMapper.writeValueAsString(tripRequest)))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data.id").value(tripResponse.getId()))
			.andExpect(jsonPath("$.data.name").value(tripResponse.getName()))
			.andExpect(jsonPath("$.data.country").value(tripResponse.getCountry()))
			.andExpect(jsonPath("$.data.startDate").value(tripResponse.getStartDate().toString()))
			.andExpect(jsonPath("$.data.endDate").value(tripResponse.getEndDate().toString()))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;

		then(tripService).should().modifyTrip(tripId, userId, tripRequest);
	}

	@Test
	@DisplayName("# [3-1] 여행 삭제하기")
	void removeTrip() throws Exception {
		// given
		Long tripId = 1L;
		Long userId = 1L;
		given(tripService.removeTrip(tripId, userId)).willReturn(tripId);

		// when & then
		mvc.perform(delete("/api/v1/trips/" + tripId)
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data").value(tripId))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;

		then(tripService).should().removeTrip(tripId, userId);
	}

	@Test
	@DisplayName("# [4-1] 단일 여행 조회하기")
	void findTrip() throws Exception {
		// given
		Long tripId = 1L;
		LocalDateTime startDate = LocalDateTime.now().plusDays(5);
		LocalDateTime endDate = LocalDateTime.now().plusDays(10);
		TripResponse tripResponse = getTripResponse(startDate, endDate, "서울", "대한민국");
		given(tripService.findTrip(tripId)).willReturn(tripResponse);

		// when & then
		mvc.perform(get("/api/v1/trips/" + tripId)
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data.id").value(tripResponse.getId()))
			.andExpect(jsonPath("$.data.name").value(tripResponse.getName()))
			.andExpect(jsonPath("$.data.country").value(tripResponse.getCountry()))
			.andExpect(jsonPath("$.data.startDate").value(tripResponse.getStartDate().toString()))
			.andExpect(jsonPath("$.data.endDate").value(tripResponse.getEndDate().toString()))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;

		then(tripService).should().findTrip(tripId);
	}

	private TripResponse getTripResponse(
		LocalDateTime startDate,
		LocalDateTime endDate,
		String name,
		String country
	) {
		return TripResponse.builder()
			.id(1L)
			.startPoint("HOME")
			.startDate(startDate)
			.endDate(endDate)
			.name(name)
			.country(country)
			.build();
	}

	private TripRequest getTripRequest(
		LocalDateTime startDate,
		LocalDateTime endDate
	) {
		return TripRequest.builder()
			.startPoint("HOME")
			.startDate(startDate)
			.endDate(endDate)
			.townId(1L)
			.build();
	}
}