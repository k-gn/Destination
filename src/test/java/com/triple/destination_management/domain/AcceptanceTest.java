package com.triple.destination_management.domain;

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
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.domain.user.repository.UserRepository;
import com.triple.destination_management.global.config.security.jwt.JwtProvider;

@ActiveProfiles("dev")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AcceptanceTest {

	private final MockMvc mvc;

	private final ObjectMapper objectMapper;

	private final JwtProvider jwtProvider;

	private final DataSource dataSource;

	@Autowired
	private UserRepository userRepository;

	public AcceptanceTest(
		@Autowired MockMvc mvc,
		@Autowired ObjectMapper objectMapper,
		@Autowired JwtProvider jwtProvider,
		@Autowired DataSource dataSource
	) {
		this.mvc = mvc;
		this.objectMapper = objectMapper;
		this.jwtProvider = jwtProvider;
		this.dataSource = dataSource;
	}

	@BeforeAll
	public void init() {
		if (userRepository.findAll().size() == 0) {
			try (Connection conn = dataSource.getConnection()) {
				ScriptUtils.executeSqlScript(conn, new ClassPathResource("/h2/data.sql"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	@DisplayName("1. 회원가입")
	void a() {
		userRepository.findAll().forEach(System.out::println);
	}

	@Test
	@DisplayName("2. 로그인")
	void b() {
		userRepository.findAll().forEach(System.out::println);
	}

	@Test
	@DisplayName("3. 도시 등록")
	void c() {
		userRepository.findAll().forEach(System.out::println);
	}

	@Test
	@DisplayName("3. 도시 조회")
	void d() {
		userRepository.findAll().forEach(System.out::println);
	}

	@Test
	@DisplayName("4. 도시 수정")
	void e() {
		userRepository.findAll().forEach(System.out::println);
	}

	@Test
	@DisplayName("5. 도시 조회")
	void f() {
		userRepository.findAll().forEach(System.out::println);
	}

	@Test
	@DisplayName("6. 도시 삭제")
	void g() {
		userRepository.findAll().forEach(System.out::println);
	}

	@Test
	@DisplayName("7. 도시 조회")
	void h() {
		userRepository.findAll().forEach(System.out::println);
	}
}
