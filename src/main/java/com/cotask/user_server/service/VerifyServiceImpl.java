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

	@Override
	public Verify save(Verify verify) {
		return repository.save(verify);
	}

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
