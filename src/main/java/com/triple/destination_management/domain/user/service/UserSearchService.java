package com.triple.destination_management.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.town.exception.TownNotFoundException;
import com.triple.destination_management.domain.town.repository.TownRepository;
import com.triple.destination_management.domain.user.dto.UserSearchResponse;
import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.domain.user.entity.UserSearch;
import com.triple.destination_management.domain.user.exception.UserNotFoundException;
import com.triple.destination_management.domain.user.repository.UserRepository;
import com.triple.destination_management.domain.user.repository.UserSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSearchService {

	private final UserSearchRepository userSearchRepository;

	private final UserRepository userRepository;

	private final TownRepository townRepository;

	/**
	 * 최근 검색도시 저장하기
	 */
	@Transactional
	public Long registerSearch(
		Long townId,
		Long userId
	) {
		Town town = townRepository.findById(townId).orElseThrow(TownNotFoundException::new);
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		userSearchRepository.findUserSearchByUserAndTown(user, town).ifPresent(userSearchRepository::delete);
		UserSearch userSearch = UserSearch.builder()
			.town(town)
			.user(user)
			.build();

		UserSearch savedSearch = userSearchRepository.save(userSearch);
		return savedSearch.getId();
	}

	/**
	 * 최근 검색도시 조회하기
	 */
	public List<UserSearchResponse> findSearch(Long userId) {
		if (userRepository.findById(userId).isPresent())
			return userSearchRepository.findSearchByUserId(userId);
		else
			throw new UserNotFoundException();
	}
}
