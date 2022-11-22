package com.triple.destination_management.domain.user.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.triple.destination_management.domain.user.dto.UserRequest;
import com.triple.destination_management.global.config.JpaConfig;
import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

@ActiveProfiles("dev")
@DataJpaTest
@DisplayName("** [ UserServiceTest ] **")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class, BCryptPasswordEncoder.class, UserService.class})
class UserServiceTest {

	@Autowired
	private UserService userService;

	@Test
	@DisplayName("# [1-1] 유저 회원가입하기")
	void registerUser() {
		// given
		UserRequest userRequest = getUserRequest();

		// when
		Long userId = userService.registerUser(userRequest);

		// then
		assertThat(userId).isNotNull();
	}

	@Test
	@DisplayName("# [1-2] 유저 중복 회원가입하기")
	void registerDuplicatedUser() {
		// given
		UserRequest userRequest = getUserRequest();
		userService.registerUser(userRequest);

		// when
		Throwable thrown = catchThrowable(() -> userService.registerUser(userRequest));

		// then
		assertThat(thrown)
			.isInstanceOf(GeneralException.class)
			.hasMessageContaining(ResponseCode.DUPLICATED_REQUEST.getMessage());
	}

	private UserRequest getUserRequest() {
		return UserRequest.builder()
			.name("김규남")
			.username("gyul")
			.password("1234")
			.build();
	}
}