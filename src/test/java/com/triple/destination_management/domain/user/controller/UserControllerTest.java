package com.triple.destination_management.domain.user.controller;

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
import com.triple.destination_management.domain.user.dto.UserRequest;
import com.triple.destination_management.domain.user.service.UserService;
import com.triple.destination_management.global.config.security.jwt.JwtProvider;
import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

@ActiveProfiles("test")
@DisplayName("** [ UserControllerTest ] **")
@WebMvcTest(UserController.class)
class UserControllerTest {

	private final MockMvc mvc;

	private final ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@MockBean
	private JwtProvider jwtProvider;

	public UserControllerTest(
		@Autowired MockMvc mvc,
		@Autowired ObjectMapper objectMapper
	) {
		this.mvc = mvc;
		this.objectMapper = objectMapper;
	}

	@Test
	@DisplayName("# [1-1]-[POST] 유저 회원가입하기")
	void registerUser() throws Exception {
		// given
		UserRequest userRequest = getUserRequest();
		Long userId = 1L;
		given(userService.registerUser(userRequest)).willReturn(userId);

		// when & then
		mvc.perform(post("/api/v1/users")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(userRequest)))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data").value(userId))
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;

		then(userService).should().registerUser(userRequest);
	}

	@Test
	@DisplayName("# [1-2]-[POST] 아이디 미입력 후 회원가입하기")
	void registerUserWithoutUsername() throws Exception {
		// given
		UserRequest userRequest = UserRequest.builder().name("김규남").password("1234").build();
		given(userService.registerUser(userRequest)).willThrow(new GeneralException(ResponseCode.VALIDATION_ERROR));

		// when & then
		mvc.perform(post("/api/v1/users")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(userRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
			// TODO: 예외 메시지 가져올 수 있는 방법 찾아보기
			.andExpect(jsonPath("$.message").value("아이디를 입력해주세요."))
		;

		then(userService).should(never()).registerUser(userRequest);
	}

	@Test
	@DisplayName("# [1-3]-[POST] 비밀번호 미입력 후 회원가입하기")
	void registerUserWithoutPassword() throws Exception {
		// given
		UserRequest userRequest = UserRequest.builder().name("김규남").username("gyul").build();
		given(userService.registerUser(userRequest)).willThrow(new GeneralException(ResponseCode.VALIDATION_ERROR));

		// when & then
		mvc.perform(post("/api/v1/users")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(userRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
			.andExpect(jsonPath("$.message").value("비밀번호를 입력해주세요."))
		;

		then(userService).should(never()).registerUser(userRequest);
	}

	@Test
	@DisplayName("# [1-4]-[POST] 이름 미입력 후 회원가입하기")
	void registerUserWithoutName() throws Exception {
		// given
		UserRequest userRequest = UserRequest.builder().username("gyul").password("1234").build();
		given(userService.registerUser(userRequest)).willThrow(new GeneralException(ResponseCode.VALIDATION_ERROR));

		// when & then
		mvc.perform(post("/api/v1/users")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(userRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
			.andExpect(jsonPath("$.message").value("이름을 입력해주세요."))
		;

		then(userService).should(never()).registerUser(userRequest);
	}

	private UserRequest getUserRequest() {
		return UserRequest.builder()
			.name("김규남")
			.username("gyul")
			.password("1234")
			.build();
	}
}