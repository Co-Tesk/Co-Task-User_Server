package com.cotask.user_server.service;

import com.cotask.user_server.dto.request.Verification;
import com.cotask.user_server.entity.User;
import com.cotask.user_server.entity.Verify;

public interface VerifyService {
	/**
 * 인증 정보를 저장하고 영속화된 엔티티를 반환합니다.
 *
 * 저장 과정에서 DB가 생성한 식별자 등 영속화된 상태가 반영된 Verify 객체를 반환합니다.
 *
 * @param verify 저장할 Verify 엔티티
 * @return 영속화되어 반환된 Verify 엔티티
 */
	Verify save(Verify verify);

	/**
 * 주어진 사용자에 대해 검증(verification) 정보를 확인한다.
 *
 * 구현체는 전달된 Verification DTO와 User 엔티티를 기반으로 검증 규칙을 적용하여 검증을 수행한다.
 * 검증 결과에 따라 관련 상태를 갱신하거나 후속 처리를 수행할 수 있다.
 *
 * @param verification 검증에 사용될 데이터 전송 객체
 * @param user         검증 대상 사용자 엔티티
 */
void validate(Verification verification, User user);
}
