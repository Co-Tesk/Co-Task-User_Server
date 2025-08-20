package com.cotask.user_server.dto.request;

import com.cotask.user_server.annotation.Password;
import com.cotask.user_server.annotation.PasswordMatches;

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
	@Schema(description = "사용자의 비밀번호", example = "Password123!")
	String password,
	@NotBlank(message = "비밀번호 확인은 필수 입력 항목입니다.")
	@Password
	@Schema(description = "비밀번호 확인", example = "Password123!")
	String passwordConfirm,
	@NotBlank(message = "닉네임은 필수 입력 항목입니다.")
	@Size(min = 2, max = 30, message = "닉네임은 2자 이상 30자 이하이어야 합니다.")
	@Schema(description = "사용자의 닉네임", example = "exampleNickname")
	String nickname
) implements PasswordMatchable {

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
}
