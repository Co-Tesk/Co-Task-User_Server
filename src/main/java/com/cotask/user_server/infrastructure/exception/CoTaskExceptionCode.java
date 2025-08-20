package com.cotask.user_server.infrastructure.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoTaskExceptionCode {
	INTERNAL_SERVER_ERROR(
		HttpStatus.INTERNAL_SERVER_ERROR,
		"INTERNAL_SERVER_ERROR",
		"An internal server error occurred."
	),
	DUPLICATE_EMAIL(
		HttpStatus.CONFLICT,
		"CO_TASK_001" ,
		"이미 사용중인 이메일입니다."
	),
	PASSWORD_CONFIRM_NOT_MATCH(
		HttpStatus.BAD_REQUEST,
		"CO_TASK_002",
		"비밀번호가 일치하지 않습니다."
	),
	;

	private final HttpStatus status;
	private final String code;
	private final String message;
}
