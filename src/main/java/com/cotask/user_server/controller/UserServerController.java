package com.cotask.user_server.controller;

import org.springframework.http.ResponseEntity;

import com.cotask.user_server.dto.request.Register;
import com.cotask.user_server.dto.response.ExceptionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
			"회원가입 시 필요한 정보를 담고 있는 DTO를 받아 처리합니다.",
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "회원가입 성공",
				content = @Content(
					schema = @Schema(
						implementation = void.class,
						description = "회원가입이 성공적으로 처리되면 빈 응답을 반환합니다。"
					)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "Validation 실패",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						implementation = ExceptionResponse.class,
						description = "유효성 검사 실패 시 반환되는 에러 메시지",
						example = """
								{
									"status": 400,
									"code": "VALIDATION_ERROR",
									"message": "입력값이 유효하지 않습니다.",
									"errors": {
										"email": "이메일은 100자 이하이어야 합니다.",
										"nickname": "닉네임은 필수 입력 항목입니다."
									}
								}
							"""
					)
				)
			),
			@ApiResponse(
				responseCode = "409",
				description = "이메일 중복",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						implementation = ExceptionResponse.class,
						description = "유효성 검사 실패 시 반환되는 에러 메시지",
						example = """
								{
									"status": 409,
									"code": "CO_TASK_001",
									"message": "이미 사용중인 이메일입니다."
								}
							"""
					)
				)
			),
			@ApiResponse(
				responseCode = "500",
				description = "서버 오류",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						implementation = ExceptionResponse.class,
						description = "서버 오류 발생 시 반환되는 에러 메시지",
						example = """
								{
									"status": 500,
									"code": "INTERNAL_SERVER_ERROR",
									"message": "An internal server error occurred."
								}
							"""
					)
				)
			)
		}
	)
	ResponseEntity<?> register(Register register);
}
