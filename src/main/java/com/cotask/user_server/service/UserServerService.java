package com.cotask.user_server.service;

import com.cotask.user_server.dto.request.Register;
import com.cotask.user_server.infrastructure.exception.CoTaskException;

public interface UserServerService {
	/**
	 * 사용자의 회원가입을 처리하는 메서드
	 * 회원가입 시 이메일 중복 확인, 비밀번호 일치 여부 확인 후
	 * 사용자 정보를 저장하고 이메일 인증 토큰을 생성하여 저장한다.
	 * @throws CoTaskException 이메일 중복 또는 비밀번호 불일치 시 예외 발생
	 * @param register 회원가입에 필요한 정보를 담고 있는 DTO
	 */
	void register(Register register);
}
