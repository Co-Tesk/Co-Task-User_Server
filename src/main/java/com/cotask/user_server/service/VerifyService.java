package com.cotask.user_server.service;

import com.cotask.user_server.dto.request.Verification;
import com.cotask.user_server.entity.User;
import com.cotask.user_server.entity.Verify;

public interface VerifyService {
	/**
 * 인증 정보를 저장하고 저장된 엔티티(예: DB에서 생성된 식별자 등 영속화된 상태가 반영된 객체)를 반환합니다.
 *
 * @param verify 저장할 인증 정보 엔티티
 * @return 저장된 인증 정보 엔티티
 */
	Verify save(Verify verify);

	void validate(Verification verification, User user);
}
