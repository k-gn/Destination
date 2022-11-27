package com.triple.destination_management.domain;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triple.destination_management.domain.user.dto.UserLoginRequest;
import com.triple.destination_management.domain.user.dto.UserRequest;
import com.triple.destination_management.domain.user.repository.UserRepository;
import com.triple.destination_management.global.config.security.jwt.JwtProvider;
import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserAcceptanceTest {

	private final MockMvc mvc;

	private final ObjectMapper objectMapper;

	private final DataSource dataSource;

	public UserAcceptanceTest(
		@Autowired MockMvc mvc,
		@Autowired ObjectMapper objectMapper,
		@Autowired DataSource dataSource
	) {
		this.mvc = mvc;
		this.objectMapper = objectMapper;
		this.dataSource = dataSource;
	}

	@BeforeAll
	public void init() {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/h2/data.sql"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	@DisplayName("# [1-1]-[POST] 유저 회원가입하기")
	void registerUser() throws Exception {
		// given
		UserRequest userRequest = getUserRequest();

		// when & then
		mvc.perform(post("/api/v1/users")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(userRequest)))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data").isNumber())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;
	}

	@Test
	@DisplayName("# [1-2]-[POST] 아이디 미입력 후 회원가입하기")
	void registerUserWithoutUsername() throws Exception {
		// given
		UserRequest userRequest = UserRequest.builder().name("김규남").password("1234").build();

		// when & then
		mvc.perform(post("/api/v1/users")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(userRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
			.andExpect(jsonPath("$.message").value("아이디를 입력해주세요."))
		;
	}

	@Test
	@DisplayName("# [1-3]-[POST] 비밀번호 미입력 후 회원가입하기")
	void registerUserWithoutPassword() throws Exception {
		// given
		UserRequest userRequest = UserRequest.builder().name("김규남").username("gyul").build();

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
	}

	@Test
	@DisplayName("# [1-4]-[POST] 이름 미입력 후 회원가입하기")
	void registerUserWithoutName() throws Exception {
		// given
		UserRequest userRequest = UserRequest.builder().username("gyul").password("1234").build();

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
	}

	@Test
	@DisplayName("# [2-1]-[POST] 로그인하기")
	void login() throws Exception {
		// given
		UserLoginRequest userLoginRequest = UserLoginRequest.builder()
			.username("gyunam")
			.password("1234")
			.build();

		// when & then
		mvc.perform(post("/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(userLoginRequest)))
			.andExpect(status().is2xxSuccessful())
			.andExpect(header().exists("Authorization"))
		;
	}

	@Test
	@DisplayName("# [2-2]-[POST] 로그인 실패")
	void loginFail() throws Exception {
		// given
		UserLoginRequest userLoginRequest = UserLoginRequest.builder()
			.username("gyunam")
			.password("1313")
			.build();

		// when & then
		mvc.perform(post("/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(userLoginRequest)))
			.andExpect(status().is4xxClientError())
			.andExpect(header().doesNotExist("Authorization"))
		;
	}

	private UserRequest getUserRequest() {
		return UserRequest.builder()
			.name("김규남")
			.username("gyul")
			.password("1234")
			.build();
	}
}
