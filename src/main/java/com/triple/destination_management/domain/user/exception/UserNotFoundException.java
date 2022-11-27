package com.triple.destination_management.domain.user.exception;

import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

public class UserNotFoundException extends GeneralException {

	public UserNotFoundException() {
		super(ResponseCode.NOT_FOUND);
	}
}
