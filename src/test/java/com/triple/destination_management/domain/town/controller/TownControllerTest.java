package com.triple.destination_management.domain.town.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triple.destination_management.domain.town.dto.TownRequest;
import com.triple.destination_management.domain.town.dto.TownResponse;
import com.triple.destination_management.domain.town.exception.TownDependencyException;
import com.triple.destination_management.domain.town.exception.TownDuplicatedException;
import com.triple.destination_management.domain.town.exception.TownNotFoundException;
import com.triple.destination_management.domain.town.service.TownService;
import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

@ActiveProfiles("local")
@DisplayName("** [ TownControllerTest ] **")
@WebMvcTest(TownController.class)
class TownControllerTest {

	private final MockMvc mvc;

	private final ObjectMapper objectMapper;

	@MockBean
	private TownService townService;

	public TownControllerTest(
		@Autowired MockMvc mvc,
		@Autowired ObjectMapper objectMapper
	) {
		this.mvc = mvc;
		this.objectMapper = objectMapper;
	}

	@Test
	@DisplayName("# [1-1]-[POST] 도시 등록하기")
	void registerTown() throws Exception {
		// given
		TownRequest townRequest = getTownRequest("서울", "대한민국");
		TownResponse townResponse = getTownResponse("서울", "대한민국");
		given(townService.registerTown(townRequest)).willReturn(townResponse);

		// when & then
		mvc.perform(post("/town")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data").value(townResponse))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;

		then(townService).should().registerTown(townRequest);
	}

	@Test
	@DisplayName("# [1-2]-[POST] 동일한 도시 중복 등록")
	void registerDuplicateTown() throws Exception {
		// given
		TownRequest townRequest = getTownRequest("서울", "대한민국");
		given(townService.registerTown(townRequest)).willThrow(new TownDuplicatedException());

		// when & then
		mvc.perform(post("/town")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.DUPLICATED_REQUEST.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.DUPLICATED_REQUEST.getMessage()))
		;

		then(townService).should().registerTown(townRequest);
	}

	@Test
	@DisplayName("# [1-3]-[POST] 국가 미입력 후 도시 등록하기")
	void registerTownWithWrongCountry() throws Exception {
		// given
		TownRequest townRequest = TownRequest.builder().name("서울").build();
		given(townService.registerTown(townRequest)).willThrow(new GeneralException(ResponseCode.VALIDATION_ERROR));

		// when & then
		mvc.perform(post("/town")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
			// TODO: 예외 메시지 가져올 수 있는 방법 찾아보기
			.andExpect(jsonPath("$.message").value("국가를 입력해주세요!"))
		;

		then(townService).should(never()).registerTown(townRequest);
	}

	@Test
	@DisplayName("# [1-4]-[POST] 도시명 미입력 후 도시 등록하기")
	void registerTownWithWrongName() throws Exception {
		// given
		TownRequest townRequest = TownRequest.builder().country("대한민국").build();
		given(townService.registerTown(townRequest)).willThrow(new GeneralException(ResponseCode.VALIDATION_ERROR));

		// when & then
		mvc.perform(post("/town")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
			// TODO: 예외 메시지 가져올 수 있는 방법 찾아보기
			.andExpect(jsonPath("$.message").value("도시명을 입력해주세요!"))
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
		mvc.perform(put("/town/" + townId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data").value(townResponse))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;

		then(townService).should().modifyTown(townId, townRequest);
	}

	@Test
	@DisplayName("# [2-2]-[PUT] 등록되지 않은 도시 수정하기")
	void modifyUnRegisteredTown() throws Exception {
		// given
		Long townId = 1L;
		TownRequest townRequest = getTownRequest("대구", "대한민국");
		given(townService.modifyTown(townId, townRequest)).willThrow(new TownNotFoundException());

		// when & then
		mvc.perform(put("/town/" + townId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.NOT_FOUND.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.NOT_FOUND.getMessage()))
		;

		then(townService).should().modifyTown(townId, townRequest);
	}

	@Test
	@DisplayName("# [2-3]-[PUT] 국가 미입력 후 도시 수정하기")
	void modifyTownWithWrongCountry() throws Exception {
		// given
		Long townId = 1L;
		TownRequest townRequest = TownRequest.builder().name("서울").build();
		given(townService.modifyTown(townId, townRequest)).willThrow(
			new GeneralException(ResponseCode.VALIDATION_ERROR));

		// when & then
		mvc.perform(put("/town/" + townId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
			// TODO: 예외 메시지 가져올 수 있는 방법 찾아보기
			.andExpect(jsonPath("$.message").value("국가를 입력해주세요!"))
		;

		then(townService).should(never()).modifyTown(townId, townRequest);
	}

	@Test
	@DisplayName("# [2-4]-[PUT] 도시명 미입력 후 도시 수정하기")
	void modifyTownWithWrongName() throws Exception {
		// given
		Long townId = 1L;
		TownRequest townRequest = TownRequest.builder().country("대한민국").build();
		given(townService.modifyTown(townId, townRequest)).willThrow(
			new GeneralException(ResponseCode.VALIDATION_ERROR));

		// when & then
		mvc.perform(put("/town/" + townId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(townRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
			// TODO: 예외 메시지 가져올 수 있는 방법 찾아보기
			.andExpect(jsonPath("$.message").value("도시명을 입력해주세요!"))
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
		mvc.perform(delete("/town/" + townId)
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
	@DisplayName("# [3-2]-[DELETE] 도시가 지정된 여행이 있을 경우 삭제")
	void removeDesignatedTownAsTrip() throws Exception {
		// given
		Long townId = 1L;
		given(townService.removeTown(townId)).willThrow(new TownDependencyException());

		// when & then
		mvc.perform(delete("/town/" + townId)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.REMOVE_DEPENDENCY.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.REMOVE_DEPENDENCY.getMessage()))
		;

		then(townService).should().removeTown(townId);
	}

	@Test
	@DisplayName("# [4-1]-[GET] 도시 단일 조회하기")
	void findTown() throws Exception {
		// given
		Long townId = 1L;
		TownResponse townResponse = getTownResponse("서울", "대한민국");
		given(townService.findTown(townId)).willReturn(townResponse);

		// when & then
		mvc.perform(get("/town/" + townId)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data").value(townResponse))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;

		then(townService).should().findTown(townId);
	}

	@Test
	@DisplayName("# [4-2]-[GET] 존재하지 않는 도시 조회하기")
	void findNotExistTown() throws Exception {
		// given
		Long townId = 1L;
		given(townService.findTown(townId)).willThrow(new TownNotFoundException());

		// when & then
		mvc.perform(get("/town/" + townId)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.NOT_FOUND.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.NOT_FOUND.getMessage()))
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