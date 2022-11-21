package com.triple.destination_management.domain.town.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslTownRepositoryImpl implements QueryDslTownRepository {

	private final JPAQueryFactory queryFactory;
}
