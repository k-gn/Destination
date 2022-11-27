package com.triple.destination_management.global.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import com.triple.destination_management.global.constants.ResponseCode;
import com.triple.destination_management.global.dto.ApiErrorResponse;
import com.triple.destination_management.global.exception.GeneralException;

@DisplayName("** [ApiExceptionHandlerTest - API 에러 처리] **")
class ApiExceptionHandlerTest {

	private ApiExceptionHandler exceptionHandler;

	private WebRequest webRequest;

	@BeforeEach
	void setUp() {
		exceptionHandler = new ApiExceptionHandler();
		webRequest = new DispatcherServletWebRequest(new MockHttpServletRequest());
	}

	@DisplayName("# [1] 프로젝트 일반 오류")
	@Test
	void generalException() {
		// Given
		ResponseCode responseCode = ResponseCode.INTERNAL_ERROR;
		GeneralException ex = new GeneralException(responseCode);

		// When
		ResponseEntity<Object> response = exceptionHandler.general(ex, webRequest);

		// Then
		assertThat(response)
			.hasFieldOrPropertyWithValue("body", ApiErrorResponse.of(false, responseCode, ex))
			.hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
			.hasFieldOrPropertyWithValue("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DisplayName("# [2] 기타(전체) 오류")
	@Test
	void exception() {
		// Given
		Exception ex = new Exception();

		// When
		ResponseEntity<Object> response = exceptionHandler.exception(ex, webRequest);

		// Then
		assertThat(response)
			.hasFieldOrPropertyWithValue("body", ApiErrorResponse.of(false, ResponseCode.INTERNAL_ERROR, ex))
			.hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
			.hasFieldOrPropertyWithValue("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DisplayName("# [3] 권한 오류")
	@Test
	void accessDeniedException() {
		// Given
		AccessDeniedException ex = new AccessDeniedException(ResponseCode.ACCESS_DENIED.getMessage());

		// When
		ResponseEntity<Object> response = exceptionHandler.accessDeniedException(ex, webRequest);

		// Then
		assertThat(response)
			.hasFieldOrPropertyWithValue("body", ApiErrorResponse.of(false, ResponseCode.ACCESS_DENIED, ex))
			.hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
			.hasFieldOrPropertyWithValue("statusCode", HttpStatus.BAD_REQUEST);
	}
}