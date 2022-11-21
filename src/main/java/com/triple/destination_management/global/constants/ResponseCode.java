package com.triple.destination_management.global.constants;

import java.util.Optional;
import java.util.function.Predicate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

	// ok case
	OK(200, ResponseCategory.NORMAL, "Ok"),

	// 4xx error case
	BAD_REQUEST(400, ResponseCategory.CLIENT_SIDE, "Bad request"),
	VALIDATION_ERROR(400, ResponseCategory.CLIENT_SIDE, "Validation error"),
	NOT_FOUND(400, ResponseCategory.CLIENT_SIDE, "Requested resource is not found"),
	ACCESS_DENIED(400, ResponseCategory.CLIENT_SIDE, "Access denied"),
	DB_DATA_NOT_FOUND(400, ResponseCategory.CLIENT_SIDE, "Data is not found"),
	TYPE_MISS_MATCHED(400, ResponseCategory.CLIENT_SIDE, "Type miss matched"),

	// 5xx error case
	INTERNAL_ERROR(500, ResponseCategory.SERVER_SIDE, "Internal error"),
	DATA_ACCESS_ERROR(500, ResponseCategory.SERVER_SIDE, "Data access error");

	private final Integer code; // error code
	private final ResponseCategory responseCategory; // error category
	private final String message; // error message

	public String getMessage(Throwable ex) {
		return this.getMessage(ex.getMessage());
	}

	public String getMessage(String message) {
		return Optional.ofNullable(message)
			.filter(Predicate.not(String::isBlank))
			.orElse(this.getMessage());
	}

	// client side error?
	public boolean isClientSideError() {
		return this.getResponseCategory() == ResponseCategory.CLIENT_SIDE;
	}

	// server side error?
	public boolean isServerSideError() {
		return this.getResponseCategory() == ResponseCategory.SERVER_SIDE;
	}

	@Override
	public String toString() {
		return String.format("%s (%d)", this.name(), this.getCode());
	}

	// error category enum
	public enum ResponseCategory {
		NORMAL, CLIENT_SIDE, SERVER_SIDE
	}
}
