package com.cotask.user_server.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record Login(
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Schema(description = "사용자의 이메일 주소", example = "example@example.com")
	String email,
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Schema(
		description = "사용자의 비밀번호",
		example = "Password123!",
		accessMode = Schema.AccessMode.WRITE_ONLY
	)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	String password
) {
}
