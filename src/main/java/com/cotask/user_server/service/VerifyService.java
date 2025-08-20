package com.cotask.user_server.service;

import com.cotask.user_server.entity.Verify;

public interface VerifyService {
	/**
	 * 인증 정보를 저장합니다.
	 * @param verify 인증 정보를 담고 있는 객체
	 * @return 저장된 인증 정보 객체
	 */
	Verify save(Verify verify);
}
