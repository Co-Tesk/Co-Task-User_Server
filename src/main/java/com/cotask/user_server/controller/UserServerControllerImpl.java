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
}
