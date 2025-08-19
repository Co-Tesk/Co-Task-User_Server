package com.cotask.user_server.dto.request;

import com.cotask.user_server.annotation.Password;
import com.cotask.user_server.annotation.PasswordMatches;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches
public record Register(
	@Email
	@NotBlank
	@Size(max = 100, message = "이메일은 100자 이하이어야 합니다.")
	String email,
	@NotBlank
	@Password
	String password,
	@NotBlank
	String confirmPassword,
	@NotBlank
	@Size(min = 2, max = 30, message = "닉네임은 2자 이상 30자 이하이어야 합니다.")
	String nickname
) implements PasswordMatchable {

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getPasswordConfirm() {
		return confirmPassword;
	}
}
