package com.cotask.user_server.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cotask.user_server.dto.request.Login;
import com.cotask.user_server.dto.request.Register;
import com.cotask.user_server.dto.request.Verification;
import com.cotask.user_server.dto.response.LoginToken;
import com.cotask.user_server.infrastructure.utils.Constants;
import com.cotask.user_server.service.UserServerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserServerControllerImpl implements UserServerController {
	private final UserServerService service;

	@PostMapping("/auth/register")
	public ResponseEntity<?> register(@RequestBody @Valid Register register) {
		service.register(register);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PatchMapping("/auth/verification")
	public ResponseEntity<?> verification(@RequestBody @Valid Verification verification) {
		service.verification(verification);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody @Valid Login login) {
		LoginToken response = service.login(login);

		// 1. http 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.ACCESS_TOKEN_HEADER, "Bearer " + response.accessToken());

		// 2. http 쿠키 설정
		ResponseCookie cookie = ResponseCookie.from(Constants.REFRESH_TOKEN_COOKIE, response.refreshToken())
			.httpOnly(true) // 자바스크립트에서 접근 불가
			.secure(false) // HTTPS 환경에서만 전송 (true 로 설정하는 것이 안전)
			// .domain("cotask.com") // 도메인 설정 (필요에 따라 설정)
			.domain("localhost") // 로컬 테스트용
			.path("/") // 모든 경로에서 접근 가능
			.maxAge(7 * 24 * 60 * 60) // 7일
			.sameSite("None") // 크로스 사이트 요청 허용
			.build();

		headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
		return ResponseEntity.status(HttpStatus.OK)
			.headers(headers)
			.build();
	}
}
