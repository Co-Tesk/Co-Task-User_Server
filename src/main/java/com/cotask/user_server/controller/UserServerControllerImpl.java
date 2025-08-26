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
	 * 새 사용자 등록 요청을 처리하고 성공 시 201 Created 상태를 반환합니다.
	 *
	 * 요청 본문으로 전달된 Register DTO를 기반으로 사용자 등록을 수행합니다.
	 * 입력 검증은 메소드에 적용된 {@code @Valid}에 의해 수행되며, 성공 시 응답 본문 없이 HTTP 201 상태가 반환됩니다.
	 *
	 * @param register 등록에 필요한 사용자 정보가 담긴 DTO
	 * @return HTTP 201 Created 상태를 가진 빈 응답 엔티티
	 */
	@PostMapping("/auth/register")
	public ResponseEntity<?> register(@RequestBody @Valid Register register) {
		service.register(register);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 사용자 계정 검증 요청을 처리한다.
	 *
	 * 전달받은 Verification DTO로 계정 검증을 수행하고 성공 시 HTTP 204 No Content 응답을 반환한다.
	 *
	 * @param verification 검증에 필요한 데이터(예: 이메일/토큰 등)를 담은 DTO
	 * @return 항상 HTTP 204 No Content 상태를 가진 ResponseEntity
	 */
	@PatchMapping("/auth/verification")
	public ResponseEntity<?> verification(@RequestBody @Valid Verification verification) {
		service.verification(verification);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
