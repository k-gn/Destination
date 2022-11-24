package com.triple.destination_management.domain.user.exception;

import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

public class UserDuplicatedException extends GeneralException {

	public UserDuplicatedException() {
		super(ResponseCode.DUPLICATED_REQUEST);
	}
}
