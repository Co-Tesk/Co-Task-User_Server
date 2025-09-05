package com.cotask.user_server.service;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cotask.user_server.dto.request.Login;
import com.cotask.user_server.dto.request.Register;
import com.cotask.user_server.dto.request.Verification;
import com.cotask.user_server.dto.response.LoginToken;
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
	private final TokenService tokenService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void register(Register register) {
		registerVerify(register);
		User savedUser = userService.save(User.builder()
			.email(register.email().trim().toLowerCase(Locale.ROOT))
			.password(passwordEncoder.encode(register.getPassword()))
			.nickname(register.nickname())
			.isVerify(false)
			.isDeleted(false)
			.build());
		// 이메일 전송의 경우 kafka 를 사용해 비동기로 처리하도록 해야함.
		// 이메일 인증 토큰 생성 및 저장
		Verify savedVerify = verifyService.save(Verify.builder()
			.user(savedUser)
			.type(VerifyType.EMAIL)
			.code(UUID.randomUUID().toString())
			.expiresAt(Instant.now().plusSeconds(3600)) // 1시간 후 만료
			.isUsed(false)
			.build());
		// TODO: kafka 를 사용한 이메일 전송 로직 추가 필요
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = CoTaskException.class)
	public void verification(Verification verification) {
		// verification 의 email을 사용해 사용자가 존재하는지 확인
		User user = userService.findByEmail(verification.email().trim().toLowerCase(Locale.ROOT));
		// 인증정보 검증
		verifyService.validate(verification, user);
		// 인증 정보 검증이 통과를 한 경우
		// 사용자의 인증 상태를 true로 변경
		if (verification.type() == VerifyType.EMAIL) {
			if (Boolean.FALSE.equals(user.getIsVerify())) {
				user.setIsVerify(true);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = CoTaskException.class)
	public LoginToken login(Login login) {
		// 1. 사용자 조회
		User userByEmail = userService.findByEmail(login.email().trim().toLowerCase(Locale.ROOT));
		// 2. 비밀번호 검증
		if (!passwordEncoder.matches(login.password(), userByEmail.getPassword())) {
			// 비밀번호 불일치
			// 같은 에러코드를 반환하여 무차별 대입 공격 방어
			throw new CoTaskException(CoTaskExceptionCode.NOT_FOUND_USER);
		}
		// 3. 인증 확인
		if (Boolean.FALSE.equals(userByEmail.getIsVerify())) {
			// 인증이 되지 않은 사용자
			throw new CoTaskException(CoTaskExceptionCode.NOT_VERIFY_USER);
		}

		// 4. 삭제된 사용자 확인
		if (userByEmail.getIsDeleted()) {
			throw new CoTaskException(CoTaskExceptionCode.WITHDRAWN_USER);
		}

		// 5. 토큰 생성 (Access LoginToken, Refresh LoginToken)
		return tokenService.createLoginToken(userByEmail);
	}

	private void registerVerify(Register register) {
		// 이메일 중복 확인
		if (userService.existsByEmail(register.email().trim().toLowerCase(Locale.ROOT))) {
			throw new CoTaskException(CoTaskExceptionCode.DUPLICATE_EMAIL);
		}
		// 비밀번호 동일 3차 확인(1차: front, 2차: dto, 3차: registerVerify)
		passwordConfirmMatch(register.password(), register.passwordConfirm());
	}

	private void passwordConfirmMatch(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new CoTaskException(CoTaskExceptionCode.PASSWORD_CONFIRM_NOT_MATCH);
		}
	}
}
