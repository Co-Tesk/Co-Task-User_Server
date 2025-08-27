package com.cotask.user_server.controller;

import static com.cotask.user_server.infrastructure.exception.CoTaskExceptionCode.*;

import org.springframework.http.ResponseEntity;

import com.cotask.user_server.annotation.swagger.ApiErrorCodeExamples;
import com.cotask.user_server.dto.request.Register;
import com.cotask.user_server.dto.request.Verification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface UserServerController {
	/**
	 * 사용자가 회원가입을 요청할 때 호출되는 메서드
	 * @param register 회원가입 시 필요한 정보를 담고 있는 DTO
	 * @return ResponseEntity 객체로, 성공 시 201 Created 상태 코드를 반환하게 되며,
	 * 실패 시 적절한 에러 메시지와 상태 코드를 포함한다.
	 */
	@Operation(
		summary = "사용자 회원가입",
		description = "사용자가 회원가입을 요청할 때 호출되는 메서드입니다. <br>" +
			"회원가입 시 필요한 정보를 담고 있는 DTO를 받아 처리합니다."
	)
	@ApiErrorCodeExamples({
		DUPLICATE_EMAIL,
		PASSWORD_CONFIRM_NOT_MATCH,
		INTERNAL_SERVER_ERROR
	})
	@ApiResponse(responseCode = "201", description = "Created - 회원가입 성공")
	ResponseEntity<?> register(Register register);

	/**
	 * 사용자 검증 요청을 처리하는 엔드포인트 계약을 정의한다.
	 *
	 * <p>Verification DTO를 받아 검증을 수행하고 처리 결과를 담은 {@code ResponseEntity<?>}를 반환한다.
	 * 응답의 HTTP 상태 코드와 본문 형식(성공/오류 페이로드)은 구현체에 따라 결정된다.</p>
	 *
	 * @param verification 검증에 필요한 데이터를 담은 요청 DTO
	 * @return 처리 결과를 포함한 {@code ResponseEntity<?>}
	 */
	@Operation(
		summary = "사용자 인증",
		description = "사용자 검증 요청을 처리하는 API입니다.<br>" +
			"Verification DTO를 받아 검증을 수행합니다."
	)
	@ApiErrorCodeExamples({
		NOT_FOUND_USER,
		NOT_FOUND_VERIFICATION,
		ALREADY_USED_VERIFICATION,
		EXPIRED_VERIFICATION,
		INTERNAL_SERVER_ERROR
	})
	@ApiResponse(responseCode = "204", description = "No Content - 인증 성공")
	ResponseEntity<?> verification(Verification verification);
}
