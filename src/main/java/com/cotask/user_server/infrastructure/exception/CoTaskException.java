package com.cotask.user_server.infrastructure.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CoTaskException extends Exception {
	private final HttpStatus status;
	private final String code;
	private final String message;

	public CoTaskException(CoTaskExceptionCode code) {
		this.status = code.getStatus();
		this.code = code.getCode();
		this.message = code.getMessage();
	}
}
