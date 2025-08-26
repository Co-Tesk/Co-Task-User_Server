package com.cotask.user_server.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotask.user_server.dto.request.Verification;
import com.cotask.user_server.entity.User;
import com.cotask.user_server.entity.Verify;
import com.cotask.user_server.infrastructure.exception.CoTaskException;
import com.cotask.user_server.infrastructure.exception.CoTaskExceptionCode;
import com.cotask.user_server.repository.VerifyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerifyServiceImpl implements VerifyService {
	private final VerifyRepository repository;

	/**
	 * Verify 엔티티를 저장하고 저장된 엔티티를 반환합니다.
	 *
	 * 전달된 Verify 객체를 영속화(repository.save)를 통해 저장(삽입 또는 갱신)하고,
	 * 저장 결과(영속화된 Verify 인스턴스)를 반환합니다.
	 *
	 * @param verify 저장할 Verify 엔티티
	 * @return 저장되어 영속 상태가 반영된 Verify 객체
	 */
	@Override
	public Verify save(Verify verify) {
		return repository.save(verify);
	}

	/**
	 * 사용자가 제출한 Verification을 검사하고 유효하면 해당 Verify 엔티티를 사용 처리합니다.
	 *
	 * <p>주요 동작:
	 * - 주어진 사용자(user)에 대해 isUsed가 false인 Verify 목록을 조회합니다.
	 * - 목록이 비어 있거나, 전달된 verification의 타입·코드와 일치하는 Verify가 없으면 NOT_FOUND_VERIFICATION 예외를 던집니다.
	 * - 일치하는 Verify가 이미 사용된 경우 ALREADY_USED_VERIFICATION 예외를 던집니다.
	 * - 일치하는 Verify가 만료된 경우 EXPIRED_VERIFICATION 예외를 던집니다.
	 * - 모든 검증을 통과하면 해당 Verify의 isUsed를 true로 설정합니다.
	 *
	 * @param verification 검증할 코드 및 타입 정보를 가진 객체
	 * @param user         검증 대상 사용자
	 * @throws CoTaskException NOT_FOUND_VERIFICATION   일치하는 사용 전(미사용) 인증 정보가 없을 때
	 *                         ALREADY_USED_VERIFICATION 이미 사용된 인증 정보를 검증하려 할 때
	 *                         EXPIRED_VERIFICATION       인증 정보가 만료되었을 때
	 */
	@Override
	@Transactional
	public void validate(Verification verification, User user) {

		Verify checkedVerify = repository
			.findTopByUserAndTypeAndCodeForUpdate(user, verification.type(), verification.verificationCode())
			.orElseThrow(() -> new CoTaskException(CoTaskExceptionCode.NOT_FOUND_VERIFICATION));
		// 인증 정보 사용 여부 확인
		if (checkedVerify.getIsUsed()) {
			throw new CoTaskException(CoTaskExceptionCode.ALREADY_USED_VERIFICATION);
		}

		// 만료 시간 체크
		if (checkedVerify.getExpiresAt().isBefore(Instant.now())) {
			throw new CoTaskException(CoTaskExceptionCode.EXPIRED_VERIFICATION);
		}
		// 인증 정보 사용 처리
		checkedVerify.setIsUsed(true);
	}
}
