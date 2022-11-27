package com.triple.destination_management.domain.trip.exception;

import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

public class TripDateException extends GeneralException {

	public TripDateException() {
		super(ResponseCode.BAD_REQUEST);
	}
}
