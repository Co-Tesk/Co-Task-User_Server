package com.cotask.user_server.controller;

import org.springframework.http.ResponseEntity;

import com.cotask.user_server.dto.request.Register;

public interface UserServerController {
	/**
	 * 사용자가 회원가입을 요청할 때 호출되는 메서드
	 * @param register 회원가입 시 필요한 정보를 담고 있는 DTO
	 * @return ResponseEntity 객체로, 성공 시 201 Created 상태 코드를 반환하게 되며,
	 * 실패 시 적절한 에러 메시지와 상태 코드를 포함한다.
	 */
	ResponseEntity<?> register(Register register);
}
