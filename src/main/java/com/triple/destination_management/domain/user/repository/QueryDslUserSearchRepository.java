package com.triple.destination_management.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triple.destination_management.domain.user.dto.UserSearchResponse;
import com.triple.destination_management.domain.user.entity.UserSearch;

public interface QueryDslUserSearchRepository {

	List<UserSearchResponse> findSearchByUserId(Long userId);
}
