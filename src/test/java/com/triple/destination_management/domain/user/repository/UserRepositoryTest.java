package com.triple.destination_management.domain.user.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.triple.destination_management.domain.user.constants.Auth;
import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.global.config.JpaConfig;

@ActiveProfiles("test")
@DataJpaTest
@DisplayName("** [ UserRepositoryTest ] **")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class})
class UserRepositoryTest {


	private final UserRepository userRepository;

	UserRepositoryTest(@Autowired UserRepository userRepository) {this.userRepository = userRepository;}

	@Test
	@DisplayName("# [1] 유저 아이디(username)로 조회하기")
	void findUserByUsername() {
		// given
		User user = getUser();
		User savedUser = userRepository.save(user);

		// when
		User dbUser = userRepository.findUserByUsername(savedUser.getUsername()).orElse(null);

		// then
		assertThat(dbUser)
			.isNotNull()
			.hasFieldOrPropertyWithValue("username", savedUser.getUsername())
			.hasFieldOrPropertyWithValue("password", savedUser.getPassword())
			.hasFieldOrPropertyWithValue("name", savedUser.getName())
			.hasFieldOrPropertyWithValue("role", savedUser.getRole());
	}

	private User getUser() {
		return User.builder().username("gyul").password("1234").name("김규남").role(Auth.ROLE_USER).build();
	}
}