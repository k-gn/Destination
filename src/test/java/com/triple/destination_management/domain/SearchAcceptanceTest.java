package com.triple.destination_management.domain;

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

import com.triple.destination_management.domain.user.constants.Auth;
import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.global.config.security.jwt.JwtProperties;
import com.triple.destination_management.global.config.security.jwt.JwtProvider;
import com.triple.destination_management.global.constants.ResponseCode;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SearchAcceptanceTest {

	private final MockMvc mvc;

	private final JwtProvider jwtProvider;

	private final DataSource dataSource;

	private String token;

	public SearchAcceptanceTest(
		@Autowired MockMvc mvc,
		@Autowired JwtProvider jwtProvider,
		@Autowired DataSource dataSource
	) {
		this.mvc = mvc;
		this.jwtProvider = jwtProvider;
		this.dataSource = dataSource;
	}

	@BeforeAll
	public void init() {
		User user = User.builder()
			.id(1L)
			.username("gyunam")
			.name("김규남")
			.role(Auth.ROLE_USER)
			.build();

		token = jwtProvider.createAccessToken(user);

		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/h2/data.sql"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("# [1-1]-[POST] 최근 검색어 등록하기")
	void registerSearch() throws Exception {
		// given
		Long townId = 1L;

		// when & then
		mvc.perform(post("/api/v1/search/" + townId)
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data").isNumber())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;
	}

	@Test
	@DisplayName("# [1-2]-[POST] 토큰 없이 최근 검색어 등록하기")
	void registerSearchWithoutToken() throws Exception {
		// given
		Long townId = 1L;

		// when & then
		mvc.perform(post("/api/v1/search/" + townId)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.ACCESS_DENIED.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.ACCESS_DENIED.getMessage()))
		;
	}

	@Test
	@DisplayName("# [1-3]-[POST] 잘못된 토큰으로 최근 검색어 등록하기")
	void registerSearchWithWrongToken() throws Exception {
		// given
		Long townId = 1L;

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
	}

	@Test
	@DisplayName("# [2-1]-[GET] 최근 검색어 조회하기")
	void findSearch() throws Exception {
		// given

		// when & then
		mvc.perform(get("/api/v1/search")
			.contentType(MediaType.APPLICATION_JSON)
			.header(JwtProperties.JWT_ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + token))
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(ResponseCode.OK.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.OK.getMessage()))
		;
	}

	@Test
	@DisplayName("# [2-2]-[GET] 토큰 없이 최근 검색어 조회하기")
	void findSearchWithoutToken() throws Exception {
		// given

		// when & then
		mvc.perform(get("/api/v1/search")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is4xxClientError())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(ResponseCode.ACCESS_DENIED.getCode()))
			.andExpect(jsonPath("$.message").value(ResponseCode.ACCESS_DENIED.getMessage()))
		;
	}

	@Test
	@DisplayName("# [2-3]-[GET] 잘못된 토큰으로 최근 검색어 조회하기")
	void findSearchWithWrongToken() throws Exception {
		// given

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
	}
}
