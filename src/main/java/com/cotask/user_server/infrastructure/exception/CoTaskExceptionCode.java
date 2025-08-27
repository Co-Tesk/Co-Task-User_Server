package com.cotask.user_server.infrastructure.exception;

import java.lang.reflect.Field;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.cotask.user_server.annotation.swagger.ExplainError;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoTaskExceptionCode {
	@ExplainError("서버 내부 오류가 발생했을 때 반환됩니다.")
	INTERNAL_SERVER_ERROR(
		HttpStatus.INTERNAL_SERVER_ERROR,
		"INTERNAL_SERVER_ERROR",
		"An internal server error occurred."
	),
	@ExplainError("이미 사용중인 이메일일 때 반환됩니다.")
	DUPLICATE_EMAIL(
		HttpStatus.CONFLICT,
		"CO_TASK_001" ,
		"이미 사용중인 이메일입니다."
	),
	@ExplainError("비밀번호와 비밀번호 확인이 일치하지 않을 때 반환됩니다.")
	PASSWORD_CONFIRM_NOT_MATCH(
		HttpStatus.BAD_REQUEST,
		"CO_TASK_002",
		"비밀번호가 일치하지 않습니다."
	),
	@ExplainError("사용자를 찾을 수 없을 때 반환됩니다.")
	NOT_FOUND_USER(
		HttpStatus.NOT_FOUND,
		"CO_TASK_003",
		"사용자를 찾을 수 없습니다."
	),
	@ExplainError("사용자 인증 정보를 찾을 수 없을 때 반환됩니다.")
	NOT_FOUND_VERIFICATION(
		HttpStatus.NOT_FOUND,
		"CO_TASK_004",
		"사용자 인증 정보를 찾을 수 없습니다."
	),
	@ExplainError("인증 정보가 일치하지 않을 때 반환됩니다.")
	NOT_MATCH_VERIFICATION(
		HttpStatus.BAD_REQUEST,
		"CO_TASK_005",
		"인증 정보가 일치하지 않습니다."
	),
	@ExplainError("인증 정보가 만료되었을 때 반환됩니다.")
	EXPIRED_VERIFICATION(
		HttpStatus.BAD_REQUEST,
		"CO_TASK_006",
		"인증 정보가 만료되었습니다."
	),
	@ExplainError("이미 사용된 인증 정보일 때 반환됩니다.")
	ALREADY_USED_VERIFICATION(
		HttpStatus.BAD_REQUEST,
		"CO_TASK_007",
		"이미 사용된 인증 정보입니다."
	);


	private final HttpStatus status;
	private final String code;
	private final String message;

	public String getExplainError() throws NoSuchFieldException {
		// 1. CoTaskExceptionCode 클래스에서 현재 enum 상수의 필드를 찾음
		Field field = CoTaskExceptionCode.class.getField(this.name());
		ExplainError annotation = field.getAnnotation(ExplainError.class);
		return Objects.nonNull(annotation) ? annotation.value() : this.message;
	}
}
