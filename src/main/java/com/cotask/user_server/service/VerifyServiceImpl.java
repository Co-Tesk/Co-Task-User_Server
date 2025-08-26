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
	 * Verify 엔티티를 저장소에 영속화하고 저장된 엔티티를 반환합니다.
	 *
	 * @param verify 저장할 Verify 엔티티
	 * @return 저장이 완료된 Verify 엔티티(식별자나 자동으로 설정되는 필드가 반영된 객체)
	 */
	@Override
	public Verify save(Verify verify) {
		return repository.save(verify);
	}

	/**
	 * 주어진 사용자와 검증 데이터로 인증 레코드를 검증하고 성공 시 해당 레코드를 사용됨으로 표시한다.
	 *
	 * <p>트랜잭션 내에서 실행되며, 사용자·타입·코드에 해당하는 최신 검증 레코드를 잠금 조회(for update)하여 다음 검증을 수행한다.</p>
	 *
	 * @param verification 검증 타입과 코드(예: 이메일/휴대폰 인증 코드)를 포함한 도메인 값 객체
	 * @param user         검증 대상 사용자
	 * @throws CoTaskException NOT_FOUND_VERIFICATION   조회되는 검증 레코드가 없을 때
	 * @throws CoTaskException ALREADY_USED_VERIFICATION 이미 해당 검증 레코드가 사용된 상태일 때
	 * @throws CoTaskException EXPIRED_VERIFICATION    검증 코드가 만료되었을 때
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
