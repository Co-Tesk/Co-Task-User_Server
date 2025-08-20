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

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody @Valid Register register) {
		service.register(register);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
