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
import com.cotask.user_server.infrastructure.exception.CoTaskException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	// CoTaskException 헨들러
	@ExceptionHandler(CoTaskException.class)
	public ResponseEntity<?> handleCoTaskException(CoTaskException e) {
		log.error("CoTaskException occurred: {}", e.getMessage(), e);
		return ResponseEntity.status(e.getStatus())
			.body(CommonResponse.fail(
				ExceptionResponse.of(
					e.getStatus(), e.getCode(), e.getMessage()
				)
			));
	}

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
}
