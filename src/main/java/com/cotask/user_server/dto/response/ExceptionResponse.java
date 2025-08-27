package com.cotask.user_server.dto.response;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExceptionResponse(
	@Schema(
		description = "HTTP 상태 코드",
		type = "int",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	int status,
	@Schema(
		description = "애플리케이션별 에러 코드",
		type = "string",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	String code,
	@Schema(
		description = "에러 메시지",
		type = "string",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	String message,
	@Schema(
		description = "필드별 상세 에러 정보",
		type = "object",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		example = """
			{
				"email": "이메일 형식이 올바르지 않습니다.",
				"password": "비밀번호는 최소 8자 이상이어야 합니다."
			}
			"""
	)
	Map<String, String> errors
) {
	/**
	 * 주어진 HTTP 상태, 애플리케이션 에러 코드 및 메시지로 ExceptionResponse 인스턴스를 생성하여 반환합니다.
	 *
	 * <p>생성된 응답의 {@code status}는 전달된 {@link HttpStatus#value()} 값으로 설정되며,
	 * {@code errors} 필드는 설정되지 않아 JSON 직렬화 시 생략됩니다.</p>
	 *
	 * @param status HTTP 응답 상태를 나타내는 {@link HttpStatus}
	 * @param code 애플리케이션별 에러 코드
	 * @param message 사용자에게 표시할 에러 메시지
	 * @return 구성된 {@link ExceptionResponse} 객체
	 */
	public static ExceptionResponse of(HttpStatus status, String code, String message) {
		return ExceptionResponse.builder()
			.status(status.value())
			.code(code)
			.message(message)
			.build();
	}

	public static ExceptionResponse of(HttpStatus status, String code, String message, Map<String, String> errors) {
		return ExceptionResponse.builder()
			.status(status.value())
			.code(code)
			.message(message)
			.errors(errors)
			.build();
	}
}
