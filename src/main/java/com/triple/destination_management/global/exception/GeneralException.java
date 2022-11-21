package com.triple.destination_management.global.exception;

import com.triple.destination_management.global.constants.ResponseCode;

import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

	private final ResponseCode responseCode;

	public GeneralException() {
		super(ResponseCode.INTERNAL_ERROR.getMessage());
		this.responseCode = ResponseCode.INTERNAL_ERROR;
	}

	public GeneralException(String message) {
		super(ResponseCode.INTERNAL_ERROR.getMessage(message));
		this.responseCode = ResponseCode.INTERNAL_ERROR;
	}

	public GeneralException(
		String message,
		Throwable cause
	) {
		super(ResponseCode.INTERNAL_ERROR.getMessage(message), cause);
		this.responseCode = ResponseCode.INTERNAL_ERROR;
	}

	public GeneralException(Throwable cause) {
		super(ResponseCode.INTERNAL_ERROR.getMessage(cause));
		this.responseCode = ResponseCode.INTERNAL_ERROR;
	}

	public GeneralException(ResponseCode responseCode) {
		super(responseCode.getMessage());
		this.responseCode = responseCode;
	}

	public GeneralException(
		ResponseCode responseCode,
		Throwable cause
	) {
		super(responseCode.getMessage(cause), cause);
		this.responseCode = responseCode;
	}
}
