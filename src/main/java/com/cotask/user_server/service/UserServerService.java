package com.cotask.user_server.service;

import com.cotask.user_server.dto.request.Register;
import com.cotask.user_server.dto.request.Verification;
import com.cotask.user_server.infrastructure.exception.CoTaskException;

public interface UserServerService {
	/**
 * 사용자의 회원가입을 처리한다.
 *
 * <p>이메일 중복 여부와 비밀번호 일치 여부를 검증한 후 사용자 정보를 저장하고 이메일 인증 토큰을 생성하여 저장한다.
 *
 * @param register 회원가입에 필요한 이메일·비밀번호 등 입력 정보를 담은 DTO
 * @throws CoTaskException 이메일 중복 또는 비밀번호 불일치 시 발생
 */
	void register(Register register);

	void verification(Verification verification);
}
