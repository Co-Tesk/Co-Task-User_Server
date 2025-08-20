package com.cotask.user_server.service;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cotask.user_server.dto.request.Register;
import com.cotask.user_server.entity.User;
import com.cotask.user_server.entity.Verify;
import com.cotask.user_server.entity.VerifyType;
import com.cotask.user_server.infrastructure.exception.CoTaskException;
import com.cotask.user_server.infrastructure.exception.CoTaskExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServerServiceImpl implements UserServerService {
	private final SocialAccountService socialAccountService;
	private final VerifyService verifyService;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void register(Register register) {
		registerVerify(register);
		User savedUser = userService.save(User.builder()
			.email(register.email().trim().toLowerCase(Locale.ROOT))
			.password(passwordEncoder.encode(register.getPassword()))
			.nickname(register.nickname())
			.isVerify(false)
			.build());
		// 이메일 전송의 경우 kafka 를 사용해 비동기로 처리하도록 해야함.
		// 이메일 인증 토큰 생성 및 저장
		Verify savedVerify = verifyService.save(Verify.builder()
			.user(savedUser)
			.type(VerifyType.EMAIL)
			.code(UUID.randomUUID())
			.expiresAt(Instant.now().plusSeconds(3600)) // 1시간 후 만료
			.isUsed(false)
			.build());
		// TODO: kafka 를 사용한 이메일 전송 로직 추가 필요
	}

	/**
	 * 회원 가입 입력값의 사전 검증을 수행한다.
	 *
	 * <p>이메일 중복 여부와 비밀번호/비밀번호 확인 일치 여부를 확인하여 조건을 만족하지 않으면 예외를 던진다.</p>
	 *
	 * @param register 가입 요청 정보를 담은 DTO
	 * @throws CoTaskException 이메일이 이미 존재하면 {@code CoTaskExceptionCode.DUPLICATE_EMAIL}을 포함한 예외가 발생한다.
	 * @throws CoTaskException 비밀번호와 비밀번호 확인이 일치하지 않으면 {@code CoTaskExceptionCode.PASSWORD_CONFIRM_NOT_MATCH}을 포함한 예외가 발생한다.
	 */
	private void registerVerify(Register register) {
		// 이메일 중복 확인
		if (userService.existsByEmail(register.email().trim().toLowerCase(Locale.ROOT))) {
			throw new CoTaskException(CoTaskExceptionCode.DUPLICATE_EMAIL);
		}
		// 비밀번호 동일 3차 확인(1차: front, 2차: dto, 3차: registerVerify)
		passwordConfirmMatch(register.password(), register.passwordConfirm());
	}

	/**
	 * 비밀번호와 비밀번호 확인 값이 일치하는지 검증한다.
	 *
	 * @param password        사용자가 입력한 비밀번호
	 * @param passwordConfirm 사용자가 입력한 비밀번호 확인 값
	 * @throws CoTaskException 비밀번호와 확인 값이 일치하지 않을 경우 {@link CoTaskExceptionCode#PASSWORD_CONFIRM_NOT_MATCH} 코드로 발생
	 */
	private void passwordConfirmMatch(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new CoTaskException(CoTaskExceptionCode.PASSWORD_CONFIRM_NOT_MATCH);
		}
	}
}
