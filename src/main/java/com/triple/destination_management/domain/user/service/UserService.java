package com.triple.destination_management.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jdi.request.DuplicateRequestException;
import com.triple.destination_management.domain.user.constants.Auth;
import com.triple.destination_management.domain.user.dto.UserRequest;
import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.domain.user.exception.UserDuplicatedException;
import com.triple.destination_management.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	/**
	 * 유저 회원가입하기
	 */
	@Transactional
	public Long registerUser(UserRequest userRequest) {
		if (isDuplicatedUser(userRequest))
			throw new UserDuplicatedException();

		User user = UserRequest.dtoToEntity(userRequest);
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setRole(Auth.ROLE_USER);

		User savedUser = userRepository.save(user);
		return savedUser.getId();
	}

	private boolean isDuplicatedUser(UserRequest userRequest) {
		return userRepository.findUserByUsername(userRequest.getUsername()).isPresent();
	}
}
