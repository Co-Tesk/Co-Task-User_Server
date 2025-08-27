package com.cotask.user_server.controller;

import org.springframework.http.ResponseEntity;

import com.cotask.user_server.dto.request.Register;
import com.cotask.user_server.dto.request.Verification;
import com.cotask.user_server.dto.response.CommonResponse;

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
						implementation = CommonResponse.class,
						description = "유효성 검사 실패 시 반환되는 에러 메시지",
						example = """
								{
									"success": false,
									"error": {
										"status": 400,
										"code": "VALIDATION_ERROR",
										"message": "입력값이 유효하지 않습니다.",
										"errors": {
											"email": "이메일은 100자 이하이어야 합니다.",
											"nickname": "닉네임은 필수 입력 항목입니다."
										}
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
						implementation = CommonResponse.class,
						description = "유효성 검사 실패 시 반환되는 에러 메시지",
						example = """
								{
									"success": false,
									"error": {
										"status": 409,
										"code": "CO_TASK_001",
										"message": "이미 사용중인 이메일입니다."
									}
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
						implementation = CommonResponse.class,
						description = "서버 오류 발생 시 반환되는 에러 메시지",
						example = """
								{
									"success": false,
									"error": {
										"status": 500,
										"code": "INTERNAL_SERVER_ERROR",
										"message": "An internal server error occurred."
									}
								}
							"""
					)
				)
			)
		}
	)
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
			"Verification DTO를 받아 검증을 수행합니다.",
		responses = {
			@ApiResponse(
				responseCode = "204",
				description = "검증 성공",
				content = @Content(
					schema = @Schema(
						implementation = void.class,
						description = "검증이 성공적으로 처리되면 빈 응답을 반환합니다。"
					)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "Validation 실패",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						implementation = CommonResponse.class,
						description = "유효성 검사 실패 시 반환되는 에러 메시지",
						example = """
								{
									"success": false,
									"error": {
										"status": 400,
										"code": "VALIDATION_ERROR",
										"message": "입력값이 유효하지 않습니다.",
										"errors": {
											"email": "이메일은 100자 이하이어야 합니다."
										}
									}
								}
							"""
					)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "인증 정보를 이미 사용한 경우",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						implementation = CommonResponse.class,
						description = "인증 정보를 이미 사용한 경우 반환되는 에러 메시지",
						example = """
								{
									"success": false,
									"error": {
										"status": 400,
										"code": "CO_TASK_007",
										"message": "이미 사용된 인증 정보입니다."
									}
								}
							"""
					)
				)
			),
			@ApiResponse(
				responseCode = "400",
				description = "만료된 인증 정보",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						implementation = CommonResponse.class,
						description = "만료된 인증 정보 반환되는 에러 메시지",
						example = """
								{
									"success": false,
									"error": {
										"status": 400,
										"code": "CO_TASK_006",
										"message": "인증 정보가 만료되었습니다."
									}
								}
							"""
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "사용자 조회 실패",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						implementation = CommonResponse.class,
						description = "사용자 조회 실패 시 반환되는 에러 메시지",
						example = """
								{
									"success": false,
									"error": {
										"status": 404,
										"code": "CO_TASK_003",
										"message": "사용자를 찾을 수 없습니다."
									}
								}
							"""
					)
				)
			),
			@ApiResponse(
				responseCode = "404",
				description = "사용자 인증 정보 조회 실패",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(
						implementation = CommonResponse.class,
						description = "사용자 인증 정보 조회 실패 시 반환되는 에러 메시지",
						example = """
								{
									"success": false,
									"error": {
										"status": 404,
										"code": "CO_TASK_004",
										"message": "사용자 인증 정보를 찾을 수 없습니다."
									}
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
						implementation = CommonResponse.class,
						description = "서버 오류 발생 시 반환되는 에러 메시지",
						example = """
								{
									"success": false,
									"error": {
										"status": 500,
										"code": "INTERNAL_SERVER_ERROR",
										"message": "An internal server error occurred."
									}
								}
							"""
					)
				)
			)
		}
	)
	ResponseEntity<?> verification(Verification verification);
}
