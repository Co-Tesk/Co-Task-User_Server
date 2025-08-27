package com.cotask.user_server.annotation.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
	private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$";

	/**
	 * 주어진 문자열이 비밀번호 규칙을 만족하는지 검사한다.
	 *
	 * <p>비밀번호는 최소 8자 이상이어야 하며, 최소 하나의 대문자, 소문자, 숫자 및 특수문자를 포함해야 한다.
	 * null 입력은 유효하지 않으며 false를 반환한다.</p>
	 *
	 * @param password 검사할 비밀번호 문자열 (null인 경우 유효하지 않음)
	 * @return 주어진 문자열이 정책(PASSWORD_REGEX)을 만족하면 {@code true}, 그렇지 않으면 {@code false}
	 */
	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (password == null) {
			return false; // null is not valid
		}
		return password.matches(PASSWORD_REGEX);
	}
}
