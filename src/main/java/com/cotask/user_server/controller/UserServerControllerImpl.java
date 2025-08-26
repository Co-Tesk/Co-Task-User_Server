package com.cotask.user_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotask.user_server.dto.request.Register;
import com.cotask.user_server.dto.request.Verification;
import com.cotask.user_server.service.UserServerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserServerControllerImpl implements UserServerController {
	private final UserServerService service;

	/**
	 * 사용자 등록 요청을 처리하고 성공 시 HTTP 201 Created를 반환한다.
	 *
	 * 요청 본문으로 전달된 {@code Register} 객체는 컨트롤러 레벨에서 검증(@Valid)되며,
	 * 검증에 성공하면 등록 로직을 서비스에 위임한다.
	 *
	 * @param register 등록에 필요한 사용자 정보
	 * @return 빈 바디의 HTTP 201 Created 응답
	 */
	@PostMapping("/auth/register")
	public ResponseEntity<?> register(@RequestBody @Valid Register register) {
		service.register(register);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 사용자 검증 요청을 처리하고 성공 시 HTTP 204 No Content를 반환합니다.
	 *
	 * 요청 본문의 {@code Verification} DTO를 검증(@Valid)한 뒤 서비스 계층에 위임합니다.
	 *
	 * @param verification 검증에 필요한 데이터(예: 인증 토큰, 사용자 식별자 등). DTO 필드를 참고하세요.
	 * @return HTTP 204 No Content 상태를 가진 {@code ResponseEntity}
	 */
	@PatchMapping("/auth/verification")
	public ResponseEntity<?> verification(@RequestBody @Valid Verification verification) {
		service.verification(verification);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
