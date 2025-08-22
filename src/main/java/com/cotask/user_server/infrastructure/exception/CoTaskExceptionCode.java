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
	NOT_FOUND_USER(
		HttpStatus.NOT_FOUND,
		"CO_TASK_003",
		"사용자를 찾을 수 없습니다."
	),
	NOT_FOUND_VERIFICATION(
		HttpStatus.NOT_FOUND,
		"CO_TASK_004",
		"사용자 인증 정보를 찾을 수 없습니다."
	),
	NOT_MATCH_VERIFICATION(
		HttpStatus.BAD_REQUEST,
		"CO_TASK_005",
		"인증 정보가 일치하지 않습니다."), EXPIRED_VERIFICATION(
		HttpStatus.BAD_REQUEST,
		"CO_TASK_006",
		"인증 정보가 만료되었습니다."
	),
	ALREADY_USED_VERIFICATION(
		HttpStatus.BAD_REQUEST,
		"CO_TASK_007",
		"이미 사용된 인증 정보입니다."
	);


	private final HttpStatus status;
	private final String code;
	private final String message;
}
