package com.cotask.user_server.infrastructure.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoTaskExceptionCode {
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR",
		"An internal server error occurred."),
	;

	private final HttpStatus status;
	private final String code;
	private final String message;
}
