package com.triple.destination_management.domain.town.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.triple.destination_management.domain.town.dto.TownRequest;
import com.triple.destination_management.domain.town.dto.TownResponse;
import com.triple.destination_management.domain.town.service.TownService;
import com.triple.destination_management.domain.user.constants.Auth;
import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.global.config.security.jwt.JwtProperties;
import com.triple.destination_management.global.config.security.jwt.JwtProvider;
import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

@ActiveProfiles("test")
@DisplayName("** [ TownControllerTest ] **")
@WebMvcTest(TownController.class)
@Import({JwtProvider.class})
class TownControllerTest {

	private final MockMvc mvc;

	private final ObjectMapper objectMapper;

	private final JwtProvider jwtProvider;

	@MockBean
	private TownService townService;

	private String token;

	public TownControllerTest(
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
	@DisplayName("# [1-1]-[POST] 도시 등록하기")
	void registerTown() throws Exception {
		// given
		TownRequest townRequest = getTownRequest("서울", "대한민국");
		TownResponse townResponse = getTownResponse("서울", "대한민국");
		given(townService.registerTown(townRequest)).willReturn(townResponse);

		// when & then
		mvc.perform(post("/api/v1/towns")
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data.name").value(townResponse.getName()))
			.andExpect(jsonPath("$.data.country").value(townResponse.getCountry()))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;

		then(townService).should().registerTown(townRequest);
	}

	@Test
	@DisplayName("# [1-2]-[POST] 국가 미입력 후 도시 등록하기")
	void registerTownWithWrongCountry() throws Exception {
		// given
		TownRequest townRequest = TownRequest.builder().name("서울").build();
		given(townService.registerTown(townRequest)).willThrow(new GeneralException(ResponseCode.VALIDATION_ERROR));

		// when & then
		mvc.perform(post("/api/v1/towns")
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
			.andExpect(jsonPath("$.message").value("국가를 입력해주세요!"))
		;

		then(townService).should(never()).registerTown(townRequest);
	}

	@Test
	@DisplayName("# [1-3]-[POST] 도시명 미입력 후 도시 등록하기")
	void registerTownWithWrongName() throws Exception {
		// given
		TownRequest townRequest = TownRequest.builder().country("대한민국").build();
		given(townService.registerTown(townRequest)).willThrow(new GeneralException(ResponseCode.VALIDATION_ERROR));

		// when & then
		mvc.perform(post("/api/v1/towns")
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
			.andExpect(jsonPath("$.message").value("도시명을 입력해주세요!"))
		;

		then(townService).should(never()).registerTown(townRequest);
	}

	@Test
	@DisplayName("# [1-4]-[POST] 토큰 없이 도시 등록하기")
	void registerTownWithoutToken() throws Exception {
		// given
		TownRequest townRequest = getTownRequest("서울", "대한민국");

		// when & then
		mvc.perform(post("/api/v1/towns")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.ACCESS_DENIED.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.ACCESS_DENIED.getMessage()))
		;

		then(townService).should(never()).registerTown(townRequest);
	}

	@Test
	@DisplayName("# [1-5]-[POST] 잘못된 토큰으로 도시 등록하기")
	void registerTownWithWrongToken() throws Exception {
		// given
		TownRequest townRequest = getTownRequest("서울", "대한민국");

		// when & then
		mvc.perform(post("/api/v1/towns")
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + "wrong token")
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.ACCESS_DENIED.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.ACCESS_DENIED.getMessage()))
		;

		then(townService).should(never()).registerTown(townRequest);
	}

	@Test
	@DisplayName("# [2-1]-[PUT] 도시 수정하기")
	void modifyTown() throws Exception {
		// given
		Long townId = 1L;
		TownRequest townRequest = getTownRequest("대구", "대한민국");
		TownResponse townResponse = getTownResponse("대구", "대한민국");
		given(townService.modifyTown(townId, townRequest)).willReturn(townResponse);

		// when & then
		mvc.perform(put("/api/v1/towns/" + townId)
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data.name").value(townResponse.getName()))
			.andExpect(jsonPath("$.data.country").value(townResponse.getCountry()))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;

		then(townService).should().modifyTown(townId, townRequest);
	}

	@Test
	@DisplayName("# [2-2]-[PUT] 국가 미입력 후 도시 수정하기")
	void modifyTownWithWrongCountry() throws Exception {
		// given
		Long townId = 1L;
		TownRequest townRequest = TownRequest.builder().name("서울").build();
		given(townService.modifyTown(townId, townRequest)).willThrow(
			new GeneralException(ResponseCode.VALIDATION_ERROR));

		// when & then
		mvc.perform(put("/api/v1/towns/" + townId)
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
			.andExpect(jsonPath("$.message").value("국가를 입력해주세요!"))
		;

		then(townService).should(never()).modifyTown(townId, townRequest);
	}

	@Test
	@DisplayName("# [2-3]-[PUT] 도시명 미입력 후 도시 수정하기")
	void modifyTownWithWrongName() throws Exception {
		// given
		Long townId = 1L;
		TownRequest townRequest = TownRequest.builder().country("대한민국").build();
		given(townService.modifyTown(townId, townRequest)).willThrow(
			new GeneralException(ResponseCode.VALIDATION_ERROR));

		// when & then
		mvc.perform(put("/api/v1/towns/" + townId)
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
			.andExpect(jsonPath("$.message").value("도시명을 입력해주세요!"))
		;

		then(townService).should(never()).modifyTown(townId, townRequest);
	}

	@Test
	@DisplayName("# [2-4]-[PUT] 토큰 없이 도시 수정하기")
	void modifyTownWithoutToken() throws Exception {
		// given
		Long townId = 1L;
		TownRequest townRequest = getTownRequest("서울", "대한민국");

		// when & then
		mvc.perform(put("/api/v1/towns/" + townId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.ACCESS_DENIED.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.ACCESS_DENIED.getMessage()))
		;

		then(townService).should(never()).modifyTown(townId, townRequest);
	}

	@Test
	@DisplayName("# [2-5]-[PUT] 잘못된 토큰으로 도시 수정하기")
	void modifyTownWithWrongToken() throws Exception {
		// given
		Long townId = 1L;
		TownRequest townRequest = getTownRequest("서울", "대한민국");

		// when & then
		mvc.perform(put("/api/v1/towns/" + townId)
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + "wrong token")
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.ACCESS_DENIED.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.ACCESS_DENIED.getMessage()))
		;

		then(townService).should(never()).modifyTown(townId, townRequest);
	}

	@Test
	@DisplayName("# [3-1]-[DELETE] 도시 삭제하기")
	void removeTown() throws Exception {
		// given
		Long townId = 1L;
		given(townService.removeTown(townId)).willReturn(townId);

		// when & then
		mvc.perform(delete("/api/v1/towns/" + townId)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data").value(townId))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;

		then(townService).should().removeTown(townId);
	}

	@Test
	@DisplayName("# [3-2]-[DELETE] 토큰 없이 도시 삭제하기")
	void removeTownWithoutToken() throws Exception {
		// given
		Long townId = 1L;

		// when & then
		mvc.perform(delete("/api/v1/towns/" + townId)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.ACCESS_DENIED.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.ACCESS_DENIED.getMessage()))
		;

		then(townService).should(never()).removeTown(townId);
	}

	@Test
	@DisplayName("# [3-3]-[DELETE] 잘못된 토큰으로 도시 삭제하기")
	void removeTownWithWrongToken() throws Exception {
		// given
		Long townId = 1L;

		// when & then
		mvc.perform(delete("/api/v1/towns/" + townId)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + "wrong token")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.ACCESS_DENIED.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.ACCESS_DENIED.getMessage()))
		;

		then(townService).should(never()).removeTown(townId);
	}

	@Test
	@DisplayName("# [4-1]-[GET] 도시 단일 조회하기")
	void findTown() throws Exception {
		// given
		Long townId = 1L;
		TownResponse townResponse = getTownResponse("서울", "대한민국");
		given(townService.findTown(townId)).willReturn(townResponse);

		// when & then
		mvc.perform(get("/api/v1/towns/" + townId)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data.name").value(townResponse.getName()))
			.andExpect(jsonPath("$.data.country").value(townResponse.getCountry()))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;

		then(townService).should().findTown(townId);
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

	private TownResponse getTownResponse(
		String name,
		String country
	) {
		return TownResponse.builder()
			.name(name)
			.country(country)
			.build();
	}
}