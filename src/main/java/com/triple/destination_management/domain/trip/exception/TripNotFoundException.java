package com.triple.destination_management.domain.trip.exception;

import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

public class TripNotFoundException extends GeneralException {

	public TripNotFoundException() {
		super(ResponseCode.NOT_FOUND);
	}
}
