package com.cotask.user_server.service;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cotask.user_server.dto.request.Register;
import com.cotask.user_server.dto.request.Verification;
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

	/**
	 * 새 사용자 계정을 생성하고 이메일 인증 토큰을 발급하여 저장한다.
	 *
	 * <p>입력한 회원정보에 대해 중복 검사와 비밀번호 확인을 수행한 후,
	 * 사용자 엔티티를 저장하고 이메일 인증용 Verify 엔티티를 생성하여 저장한다.
	 * 생성된 인증 코드는 UUID 문자열이며 만료 시각은 생성 시점부터 1시간 뒤로 설정된다.
	 *
	 * @param register 회원 가입 요청 정보(이메일, 비밀번호, 비밀번호 확인, 닉네임 등)
	 * @throws com.cotask.common.exception.CoTaskException 중복 이메일 등록 시 또는 비밀번호 확인 불일치 시 발생
	 */
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
			.code(UUID.randomUUID().toString())
			.expiresAt(Instant.now().plusSeconds(3600)) // 1시간 후 만료
			.isUsed(false)
			.build());
		// TODO: kafka 를 사용한 이메일 전송 로직 추가 필요
	}

	/**
	 * 사용자의 이메일 인증 정보를 검증하고, 검증이 성공하면 해당 사용자의 인증 상태(isVerify)를 true로 설정합니다.
	 *
	 * @param verification 이메일과 인증 코드를 포함한 검증 요청 객체. 이 메서드는 해당 이메일로 사용자 조회 후 검증 데이터를 확인합니다.
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = CoTaskException.class)
	public void verification(Verification verification) {
		// verification 의 email을 사용해 사용자가 존재하는지 확인
		User user = userService.findByEmail(verification.email());
		// 인증정보 검증
		verifyService.validate(verification, user);
		// 인증 정보 검증이 통과를 한 경우
		// 사용자의 인증 상태를 true로 변경
		user.setIsVerify(true);
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
