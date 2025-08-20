package com.cotask.user_server.dto.request;

import com.cotask.user_server.annotation.Password;
import com.cotask.user_server.annotation.PasswordMatches;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches
public record Register(
	@Email(message = "유효한 이메일 형식이어야 합니다.")
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Size(max = 100, message = "이메일은 100자 이하이어야 합니다.")
	@Schema(description = "사용자의 이메일 주소", example = "example@example.com")
	String email,
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Password
	@Schema(
		description = "사용자의 비밀번호",
		example = "Password123!",
		accessMode = Schema.AccessMode.WRITE_ONLY
	)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	String password,
	@NotBlank(message = "비밀번호 확인은 필수 입력 항목입니다.")
	@Password
	@Schema(
		description = "비밀번호 확인",
		example = "Password123!",
		accessMode = Schema.AccessMode.WRITE_ONLY
	)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	String passwordConfirm,
	@NotBlank(message = "닉네임은 필수 입력 항목입니다.")
	@Size(min = 2, max = 30, message = "닉네임은 2자 이상 30자 이하이어야 합니다.")
	@Schema(description = "사용자의 닉네임", example = "exampleNickname")
	String nickname
) implements PasswordMatchable {

	/**
	 * PasswordMatchable 인터페이스를 구현하기 위해 사용자의 비밀번호를 반환합니다.
	 *
	 * @return 등록 요청에 제공된 비밀번호 문자열
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * 비밀번호 확인 값을 반환합니다.
	 *
	 * 비밀번호와 일치하는지 검증하기 위해 사용되며 {@link PasswordMatchable}의 구현 메서드입니다.
	 *
	 * @return 사용자가 입력한 비밀번호 확인 문자열
	 */
	@Override
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
}
