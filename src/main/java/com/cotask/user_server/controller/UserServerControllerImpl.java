package com.cotask.user_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotask.user_server.dto.request.Register;
import com.cotask.user_server.service.UserServerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserServerControllerImpl implements UserServerController {
	private final UserServerService service;

	/**
	 * 새로운 사용자를 등록하고 생성된 리소스 없이 HTTP 201(Created) 응답을 반환합니다.
	 *
	 * 요청 본문의 {@code Register} DTO는 유효성 검사를 거쳐 서비스로 전달되며,
	 * 실제 등록 처리는 {@code UserServerService.register(register)}에서 수행됩니다.
	 *
	 * @param register 요청 본문으로 전달되는 회원가입 정보 — 유효성 검사가 적용됩니다.
	 * @return HTTP 201 (Created) 상태의 빈 응답
	 */
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody @Valid Register register) {
		service.register(register);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
