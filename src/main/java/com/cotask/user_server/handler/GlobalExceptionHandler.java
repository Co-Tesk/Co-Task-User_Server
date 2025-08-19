package com.cotask.user_server.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cotask.user_server.dto.response.CommonResponse;
import com.cotask.user_server.dto.response.ExceptionResponse;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	// Validation 예외 헨들러 (@Valid 실패)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> tomatoExceptionHandler(MethodArgumentNotValidException e) {
		log.warn("Validation 실패: {}", e.getMessage());

		Map<String, String> errors = new HashMap<>();

		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}

		return ResponseEntity
			.badRequest()
			.body(CommonResponse.fail(
				ExceptionResponse.of(
					HttpStatus.BAD_REQUEST,
					"VALIDATION_ERROR",
					"입력값이 유효하지 않습니다.",
					errors // 필드별 메시지 포함
				)
			));
	}

	// JWT 만료 예외 헨들러
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException e) {
		log.warn("JWT 토큰 만료: {}", e.getMessage());

		return ResponseEntity
			.status(HttpStatus.UNAUTHORIZED)
			.body(CommonResponse.fail(
				ExceptionResponse.of(
					HttpStatus.UNAUTHORIZED,
					"JWT_EXPIRED",
					"인증 토큰이 만료되었습니다. 다시 로그인해주세요."
				)
			));
	}
}
