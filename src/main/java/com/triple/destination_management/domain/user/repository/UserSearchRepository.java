package com.triple.destination_management.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.user.entity.User;
import com.triple.destination_management.domain.user.entity.UserSearch;

public interface UserSearchRepository extends JpaRepository<UserSearch, Long>, QueryDslUserSearchRepository {

	Optional<UserSearch> findUserSearchByUserAndTown(
		User user,
		Town town
	);
}
