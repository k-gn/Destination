package com.triple.destination_management.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triple.destination_management.domain.user.entity.UserSearch;

public interface UserSearchRepository extends JpaRepository<UserSearch, Long> {

}
