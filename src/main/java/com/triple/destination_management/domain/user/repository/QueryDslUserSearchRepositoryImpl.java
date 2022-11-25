package com.triple.destination_management.domain.user.repository;

import static com.triple.destination_management.domain.user.entity.QUserSearch.*;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.destination_management.domain.user.dto.UserSearchResponse;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslUserSearchRepositoryImpl implements QueryDslUserSearchRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<UserSearchResponse> findSearchByUserId(Long userId) {
		return queryFactory.select(
			Projections.fields(UserSearchResponse.class, userSearch.id, userSearch.town.name, userSearch.town.country))
			.from(userSearch)
			.leftJoin(userSearch.town)
			.where(eqUserId(userId))
			.orderBy(userSearch.createDate.desc())
			.fetch();
	}

	private BooleanExpression eqUserId(Long userId) {
		return userId != null ? userSearch.user.id.eq(userId) : userSearch.user.id.eq(-99999L);
	}
}
