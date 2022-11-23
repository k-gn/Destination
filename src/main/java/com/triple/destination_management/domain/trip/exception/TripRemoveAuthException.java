package com.triple.destination_management.domain.trip.exception;

import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

public class TripRemoveAuthException extends GeneralException {

	public TripRemoveAuthException() {
		super(ResponseCode.ACCESS_DENIED);
	}
}
