package com.triple.destination_management.domain.town.repository;

import static com.triple.destination_management.domain.town.entity.QTown.*;
import static com.triple.destination_management.domain.trip.entity.QTrip.*;
import static com.triple.destination_management.domain.user.entity.QUserSearch.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.destination_management.domain.town.dto.TownFindDto;
import com.triple.destination_management.domain.town.dto.TownResponse;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslTownRepositoryImpl implements QueryDslTownRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<TownResponse> findTownsByLimit() {
		return queryFactory.select(getFields())
			.from(town)
			.limit(10)
			.orderBy(
				NumberExpression.random().asc()
			)
			.fetch();
	}

	@Override
	public List<TownResponse> findRandomTowns(Integer size) {
		return queryFactory.select(getFields())
			.from(town)
			.orderBy(
				NumberExpression.random().asc()
			)
			.limit(size)
			.fetch();
	}

	private QBean<TownResponse> getFields() {
		return Projections.fields(TownResponse.class, town.id, town.country, town.area, town.name);
	}

	@Override
	public List<TownResponse> findTravelingTowns(Long userId) {
		return queryFactory
			.select(
				Projections.fields(TownResponse.class, trip.town.id, trip.town.country, trip.town.area, trip.town.name))
			.from(trip)
			.where(
				eqUserId(userId),
				trip.startDate.before(LocalDateTime.now()),
				trip.endDate.after(LocalDateTime.now())
			)
			.orderBy(trip.id.asc())
			.fetch();
	}

	@Override
	public List<TownResponse> findScheduledTowns(TownFindDto townFindDto) {
		return queryFactory
			.select(
				Projections.fields(TownResponse.class, trip.town.id, trip.town.country, trip.town.area, trip.town.name))
			.from(trip)
			.leftJoin(trip.town)
			.where(
				eqUserId(townFindDto.getUserId()),
				trip.town.id.notIn(townFindDto.getIds()),
				trip.startDate.after(LocalDateTime.now())
			)
			.orderBy(trip.id.asc())
			.limit(townFindDto.getSize())
			.fetch();
	}

	private BooleanExpression eqUserId(Long userId) {
		return trip.user.id.eq(userId);
	}

	@Override
	public List<TownResponse> findRecentSearchTowns(TownFindDto townFindDto) {
		return queryFactory
			.select(
				Projections.fields(TownResponse.class, userSearch.town.id, userSearch.town.country,
					userSearch.town.area, userSearch.town.name))
			.from(userSearch)
			.leftJoin(userSearch.town)
			.where(
				userSearch.town.id.notIn(townFindDto.getIds()),
				userSearch.user.id.eq(townFindDto.getUserId()),
				userSearch.createDate.between(LocalDateTime.now().minusDays(7), LocalDateTime.now())
			)
			.orderBy(userSearch.id.desc())
			.limit(townFindDto.getSize())
			.fetch();
	}

	@Override
	public List<TownResponse> findRecentInsertTowns(TownFindDto townFindDto) {
		return queryFactory
			.select(getFields())
			.from(town)
			.where(
				town.id.notIn(townFindDto.getIds()),
				town.createDate.between(LocalDateTime.now().minusDays(1), LocalDateTime.now())
			)
			.orderBy(town.id.desc())
			.limit(townFindDto.getSize())
			.fetch();
	}
}
