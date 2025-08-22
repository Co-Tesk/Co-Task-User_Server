package com.cotask.user_server.service;

import java.time.Instant;
import java.util.List;

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

	@Override
	public Verify save(Verify verify) {
		return repository.save(verify);
	}

	@Override
	@Transactional
	public void validate(Verification verification, User user) {
		// User와 isUsed가 false인 Verify 엔티티를 조회
		List<Verify> verifyList = repository.findByUserAndIsUsedFalse(user);
		// 사용 전인 인증 정보가 없으면 예외 발생
		if (verifyList.isEmpty()) {
			throw new CoTaskException(CoTaskExceptionCode.NOT_FOUND_VERIFICATION);
		}
		Verify checkedVerify = verifyList.stream()
			// verification.type()이 Verify의 type과 일치하는지 확인
			.filter(verify -> verify.getType().equals(verification.type()))
			// verification.code()가 Verify의 code와 일치하는지 확인
			.filter(verify -> verify.getCode().toString().equals(verification.verificationCode()))
			// 만약 일치 할 경우 1개만 존재해야 하므로 findFirst()로 첫 번째 요소를 가져옴
			.findFirst()
			// 일치하는 인증 정보가 없으면 예외 발생
			.orElseThrow(
				() -> new CoTaskException(CoTaskExceptionCode.NOT_FOUND_VERIFICATION)
			);
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
