package com.cotask.user_server.dto.request;

import com.cotask.user_server.entity.VerifyType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Verification(
	@Email(message = "유효하지 않은 이메일 형식입니다.")
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	String email,
	@NotBlank(message = "인증 코드는 필수 입력 항목입니다.")
	String verificationCode,
	/**
	 * 검증 유형을 나타내는 필드입니다.
	 * 예를 들어, 회원가입, 비밀번호 재설정 등 다양한 검증 유형을 지원할 수 있습니다.
	 */
	@NotNull(message = "검증 유형은 필수 입력 항목입니다.")
	VerifyType type
) {
}
