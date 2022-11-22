package com.triple.destination_management.domain.town.exception;

import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

public class TownNotFoundException extends GeneralException {

	public TownNotFoundException() {
		super(ResponseCode.NOT_FOUND);
	}
}
