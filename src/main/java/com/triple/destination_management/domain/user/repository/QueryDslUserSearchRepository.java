package com.triple.destination_management.domain.user.repository;

import java.util.List;

import com.triple.destination_management.domain.user.dto.UserSearchResponse;

public interface QueryDslUserSearchRepository {

	List<UserSearchResponse> findSearchByUserId(Long userId);
}
