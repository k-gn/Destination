package com.triple.destination_management.domain.town.exception;

import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

public class TownDuplicatedException extends GeneralException {

	public TownDuplicatedException() {
		super(ResponseCode.DUPLICATED_REQUEST);
	}
}
