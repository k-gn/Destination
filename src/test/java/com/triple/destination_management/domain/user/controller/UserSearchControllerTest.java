package com.triple.destination_management.domain.user.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

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
import com.triple.destination_management.domain.user.constants.Auth;
import com.triple.destination_management.domain.user.dto.UserSearchResponse;
import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.domain.user.service.UserSearchService;
import com.triple.destination_management.global.config.security.jwt.JwtProperties;
import com.triple.destination_management.global.config.security.jwt.JwtProvider;
import com.triple.destination_management.global.constants.ResponseCode;

@ActiveProfiles("test")
@DisplayName("** [ UserSearchControllerTest ] **")
@WebMvcTest(UserSearchController.class)
@Import({JwtProvider.class})
class UserSearchControllerTest {

	private final MockMvc mvc;

	private final ObjectMapper objectMapper;

	private final JwtProvider jwtProvider;

	@MockBean
	private UserSearchService userSearchService;

	private String token;

	public UserSearchControllerTest(
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
	@DisplayName("# [1-1]-[POST] 최근 검색어 등록하기")
	void registerSearch() throws Exception {
		// given
		Long userId = 1L;
		Long townId = 1L;
		Long searchId = 1L;
		given(userSearchService.registerSearch(townId, userId)).willReturn(searchId);

		// when & then
		mvc.perform(post("/api/v1/search/" + townId)
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data").value(searchId))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;

		then(userSearchService).should().registerSearch(townId, userId);
	}

	@Test
	@DisplayName("# [1-2]-[POST] 토큰 없이 최근 검색어 등록하기")
	void registerSearchWithoutToken() throws Exception {
		// given
		Long userId = 1L;
		Long townId = 1L;
		Long searchId = 1L;
		given(userSearchService.registerSearch(townId, userId)).willReturn(searchId);

		// when & then
		mvc.perform(post("/api/v1/search/" + townId)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.ACCESS_DENIED.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.ACCESS_DENIED.getMessage()))
		;

		then(userSearchService).should(never()).registerSearch(townId, userId);
	}

	@Test
	@DisplayName("# [1-3]-[POST] 잘못된 토큰으로 최근 검색어 등록하기")
	void registerSearchWithWrongToken() throws Exception {
		// given
		Long userId = 1L;
		Long townId = 1L;
		Long searchId = 1L;
		given(userSearchService.registerSearch(townId, userId)).willReturn(searchId);

		// when & then
		mvc.perform(post("/api/v1/search/" + townId)
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + "wrong token"))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.ACCESS_DENIED.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.ACCESS_DENIED.getMessage()))
		;

		then(userSearchService).should(never()).registerSearch(townId, userId);
	}

	@Test
	@DisplayName("# [2-1]-[GET] 최근 검색어 조회하기")
	void findSearch() throws Exception {
		// given
		List<UserSearchResponse> userSearchResponses = getUserSearchResponses();
		Long userId = 1L;
		given(userSearchService.findSearch(userId)).willReturn(userSearchResponses);

		// when & then
		mvc.perform(get("/api/v1/search")
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data[0]").value(userSearchResponses.get(0)))
			.andExpect(jsonPath("$.data[1]").value(userSearchResponses.get(1)))
			.andExpect(jsonPath("$.data[2]").value(userSearchResponses.get(2)))
			.andExpect(jsonPath("$.data[3]").value(userSearchResponses.get(3)))
			.andExpect(jsonPath("$.data[4]").value(userSearchResponses.get(4)))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;

		then(userSearchService).should().findSearch(userId);
	}

	@Test
	@DisplayName("# [2-2]-[GET] 토큰 없이 최근 검색어 조회하기")
	void findSearchWithoutToken() throws Exception {
		// given
		List<UserSearchResponse> userSearchResponses = getUserSearchResponses();
		Long userId = 1L;
		given(userSearchService.findSearch(userId)).willReturn(userSearchResponses);

		// when & then
		mvc.perform(get("/api/v1/search")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.ACCESS_DENIED.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.ACCESS_DENIED.getMessage()))
		;

		then(userSearchService).should(never()).findSearch(userId);
	}

	@Test
	@DisplayName("# [2-3]-[GET] 잘못된 토큰으로 최근 검색어 조회하기")
	void findSearchWithWrongToken() throws Exception {
		// given
		List<UserSearchResponse> userSearchResponses = getUserSearchResponses();
		Long userId = 1L;
		given(userSearchService.findSearch(userId)).willReturn(userSearchResponses);

		// when & then
		mvc.perform(get("/api/v1/search")
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + "wrong token"))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.ACCESS_DENIED.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.ACCESS_DENIED.getMessage()))
		;

		then(userSearchService).should(never()).findSearch(userId);
	}

	private UserSearchResponse getUserSearchResponse(
		Long id,
		String name
	) {
		return UserSearchResponse.builder()
			.id(id)
			.name(name)
			.country("대한민국")
			.build();
	}

	private final List<String> townNames = List.of("서울", "부산", "대구", "포항", "제주");

	private List<UserSearchResponse> getUserSearchResponses() {
		List<UserSearchResponse> userSearchResponses = new ArrayList<>();
		LongStream.range(0, 5).forEach(i -> {
				UserSearchResponse userSearchResponse = getUserSearchResponse(i, townNames.get(Math.toIntExact(i)));
				userSearchResponses.add(userSearchResponse);
			}
		);
		return userSearchResponses;
	}
}