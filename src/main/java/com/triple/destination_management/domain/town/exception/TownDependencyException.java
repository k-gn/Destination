package com.triple.destination_management.domain.town.exception;

import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.exception.GeneralException;

public class TownDependencyException extends GeneralException {

	public TownDependencyException() {
		super(ResponseCode.REMOVE_DEPENDENCY.getMessage());
	}
}
