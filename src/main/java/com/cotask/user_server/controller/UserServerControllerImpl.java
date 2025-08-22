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
	 * 새 사용자 등록 요청을 처리하고 성공하면 HTTP 201 Created 응답을 반환합니다.
	 *
	 * 요청 본문의 Register DTO(입력 검증 적용)를 서비스에 전달해 사용자 등록을 수행합니다.
	 *
	 * @param register 요청 본문의 등록 정보 (유효성 검사가 적용된 DTO)
	 * @return HTTP 201 Created 상태의 빈 응답(ResponseEntity)
	 */
	@PostMapping("/auth/register")
	public ResponseEntity<?> register(@RequestBody @Valid Register register) {
		service.register(register);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 사용자 검증 요청을 처리합니다.
	 *
	 * 요청 본문의 Verification DTO를 받아 서비스에서 검증을 수행하고 성공 시 HTTP 204 No Content 응답을 반환합니다.
	 *
	 * @param verification 검증에 필요한 정보(예: 사용자 식별자 및 검증 코드)를 담은 요청 본문
	 * @return 처리 성공 시 HTTP 204 No Content를 가진 ResponseEntity
	 */
	@PatchMapping("/auth/verification")
	public ResponseEntity<?> verification(@RequestBody Verification verification) {
		service.verification(verification);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
