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
	 * 새 사용자 계정을 등록하고 이메일 인증 토큰을 생성해 저장합니다.
	 *
	 * <p>요약:
	 * - 입력한 정보를 검증(registerVerify 호출).
	 * - 비밀번호를 인코딩하여 User 엔티티를 저장.
	 * - 이메일 인증용 Verify 엔티티(만료 시간 포함)를 생성하여 저장.
	 * - 이메일 전송은 비동기 처리(예: Kafka)를 통해 별도로 수행되어야 합니다(현재 TODO).
	 *
	 * @param register 등록에 필요한 이메일, 비밀번호, 닉네임 등을 담은 요청 객체
	 * @throws com.cotask.common.exception.CoTaskException 이메일 중복 또는 비밀번호 확인 불일치 등 유효성 실패 시 발생합니다.
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
	 * 사용자 인증 요청(예: 이메일 인증)을 처리한다.
	 *
	 * 전달된 Verification의 이메일로 사용자 조회 후 검증 정보를 verifyService로 확인한다.
	 * 검증이 통과하면, 인증 타입이 EMAIL인 경우 해당 사용자의 isVerify 플래그를 true로 설정하여 사용자 계정을 인증 상태로 변경한다.
	 *
	 * @param verification 이메일(정규화된 형식), 인증 코드 및 인증 타입(예: EMAIL)을 포함한 검증 요청
	 */
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

	/**
	 * 회원가입 전제 조건을 검증한다.
	 *
	 * <p>다음을 검사한다:
	 * <ul>
	 *   <li>이메일 중복 여부 — 중복이면 {@link CoTaskExceptionCode#DUPLICATE_EMAIL} 코드의 {@link CoTaskException}를 던진다.</li>
	 *   <li>비밀번호와 비밀번호 확인 일치 여부 — 불일치면 {@link CoTaskExceptionCode#PASSWORD_CONFIRM_NOT_MATCH} 코드의 {@link CoTaskException}를 던진다.</li>
	 * </ul>
	 *
	 * @param register 회원가입 요청 DTO (이메일은 내부에서 trim 및 소문자화하여 검사함)
	 * @throws CoTaskException 발생 가능한 코드:
	 *         <ul>
	 *           <li>{@link CoTaskExceptionCode#DUPLICATE_EMAIL} — 이메일이 이미 존재할 때</li>
	 *           <li>{@link CoTaskExceptionCode#PASSWORD_CONFIRM_NOT_MATCH} — 비밀번호 확인이 일치하지 않을 때</li>
	 *         </ul>
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
	 * 비밀번호와 확인용 비밀번호가 동일한지 검증한다.
	 *
	 * @param password 원본 비밀번호
	 * @param passwordConfirm 확인용 비밀번호
	 * @throws CoTaskException 비밀번호가 일치하지 않을 경우 CoTaskExceptionCode.PASSWORD_CONFIRM_NOT_MATCH로 예외를 던진다.
	 */
	private void passwordConfirmMatch(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new CoTaskException(CoTaskExceptionCode.PASSWORD_CONFIRM_NOT_MATCH);
		}
	}
}
