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

	/**
 * 주어진 Verification DTO로 대상 사용자의 검증 정보를 확인하고 필요시 검증 상태를 갱신한다.
 *
 * 구현체는 전달된 Verification(예: 코드·토큰·만료정보 등)을 기반으로 검증 규칙을 적용하여
 * 검증 성공/실패를 판정하고, 검증 성공 시 사용자 엔티티의 상태 변경이나 후속 작업(토큰 발급, 로그 기록 등)을 수행할 수 있다.
 *
 * @param verification 검증에 사용되는 DTO(예: 인증 코드·토큰·만료 정보 등)
 * @param user         검증 대상인 사용자 엔티티
 */
void validate(Verification verification, User user);
}
