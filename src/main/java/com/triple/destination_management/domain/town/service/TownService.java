package com.triple.destination_management.domain.town.service;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.triple.destination_management.domain.town.dto.TownModifyRequest;
import com.triple.destination_management.domain.town.dto.TownRequest;
import com.triple.destination_management.domain.town.dto.TownResponse;
import com.triple.destination_management.domain.town.entity.Town;
import com.triple.destination_management.domain.town.repository.TownRepository;
import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TownService {

	private final TownRepository townRepository;

	/**
	 * 도시 등록하기
	 */
	@Transactional
	public TownResponse register(TownRequest townRequest) {
		Town town = TownRequest.dtoToEntity(townRequest);

		if (isDuplicatedTown(town))
			throw new GeneralException(ResponseCode.DUPLICATED_REQUEST);

		Town savedTown = townRepository.save(town);
		return TownResponse.entityToDto(savedTown);
	}

	private boolean isDuplicatedTown(Town town) {
		return townRepository.findTownByCode(town.getCode()).isPresent();
	}

	/**
	 * 도시 수정하기
	 */
	public void modify(
		Long townId,
		TownModifyRequest townModifyRequest
	) {

	}

	/**
	 * 도시 삭제하기
	 */
	public void remove(Long townId) {

	}

	/**
	 * 단일 도시 조회하기
	 */
	public void findOne(Long townId) {

	}

	private Town getTownById(Long townId) {
		return townRepository.findById(townId).orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));
	}

	/**
	 * 사용자별 도시 목록 조회하기
	 */
}
