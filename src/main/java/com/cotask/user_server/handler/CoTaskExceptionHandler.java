package com.cotask.user_server.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cotask.user_server.dto.response.CommonResponse;
import com.cotask.user_server.dto.response.ExceptionResponse;
import com.cotask.user_server.infrastructure.exception.CoTaskException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CoTaskExceptionHandler {
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
}
