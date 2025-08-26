package com.cotask.user_server.service;

import com.cotask.user_server.dto.request.Register;
import com.cotask.user_server.dto.request.Verification;
import com.cotask.user_server.infrastructure.exception.CoTaskException;

public interface UserServerService {
	/**
 * 사용자의 회원가입을 처리한다.
 *
 * <p>이메일 중복과 비밀번호 일치 여부를 검증한 뒤 사용자 정보를 영구 저장하고,
 * 이메일 인증을 위한 토큰을 생성하여 저장한다.
 *
 * @param register 회원가입에 필요한 이메일·비밀번호 등 입력 정보를 담은 DTO
 * @throws CoTaskException 이메일이 중복되었거나 비밀번호가 일치하지 않을 경우 발생
 */
	void register(Register register);

	/**
 * 사용자 이메일 또는 계정 검증을 수행합니다.
 *
 * Verification DTO에 담긴 검증 정보(예: 토큰)를 검사하여 계정의 이메일/인증 상태를 완료하거나 갱신합니다.
 *
 * @param verification 검증에 필요한 데이터(예: 이메일 주소, 검증 토큰). DTO 필드의 의미는 해당 타입 문서를 참조하세요.
 */
void verification(Verification verification);
}
