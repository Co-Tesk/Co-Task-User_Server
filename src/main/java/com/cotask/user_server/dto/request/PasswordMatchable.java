package com.cotask.user_server.dto.request;

public interface PasswordMatchable {
	/**
 * 사용자가 입력한 비밀번호를 반환한다.
 *
 * 구현체는 회원 가입이나 비밀번호 변경 요청 등에서 제공된 원본 비밀번호 값을 반환해야 한다. 반환값이 평문인지 해시인지, null을 허용하는지는 각 구현체의 규약에 따른다.
 *
 * @return 비밀번호 문자열 (구현체에 따라 null일 수 있음)
 */
String getPassword();

	/**
 * 사용자가 입력한 비밀번호 확인 값을 반환합니다.
 *
 * 비밀번호 일치 검사에서 원본 비밀번호와 비교하는 확인용 문자열을 제공해야 합니다.
 *
 * @return 비밀번호 확인 문자열(구현에 따라 null일 수 있음)
 */
String getPasswordConfirm();
}
