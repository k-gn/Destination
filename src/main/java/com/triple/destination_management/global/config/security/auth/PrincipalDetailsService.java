package com.triple.destination_management.global.config.security.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.domain.user.repository.UserRepository;
import com.triple.destination_management.global.constants.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findUserByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException(ResponseCode.NOT_FOUND.getMessage()));
		return new PrincipalDetails(user);
	}
}
