package com.cotask.user_server.annotation.dto;

import com.cotask.user_server.dto.request.PasswordMatchable;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordMatchable> {
	/**
	 * 요청 객체의 비밀번호(password)와 비밀번호 확인(passwordConfirm)이 존재하고 일치하는지 검증한다.
	 *
	 * <p>두 값 중 하나라도 null이면 false를 반환한다. 값이 서로 다르면 기본 제약 위반 메시지를 비활성화하고
	 * "비밀번호가 일치하지 않습니다."라는 메시지를 passwordConfirm 필드에 연결한 제약 위반을 등록한다.</p>
	 *
	 * @param request 검증 대상 객체(PasswordMatchable). getPassword()와 getPasswordConfirm()의 값을 비교한다.
	 * @return 두 필드가 null이 아니고 동일하면 true, 그렇지 않으면 false
	 */
	@Override
	public boolean isValid(PasswordMatchable request, ConstraintValidatorContext context) {
		if (request.getPassword() == null || request.getPasswordConfirm() == null) {
			return false;
		}

		boolean match = request.getPassword().equals(request.getPasswordConfirm());

		if (!match) {
			// 필드에 메시지를 지정하고 싶을 경우
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("비밀번호가 일치하지 않습니다.")
				.addPropertyNode("passwordConfirm")
				.addConstraintViolation();
		}

		return match;
	}
}
