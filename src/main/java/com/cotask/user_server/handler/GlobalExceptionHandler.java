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
	/**
	 * CoTaskException을 처리하여 표준화된 에러 응답을 반환한다.
	 *
	 * 상세: 발생한 CoTaskException을 로깅한 뒤, 예외에 포함된 HTTP 상태(e.getStatus())를 상태 코드로 사용하고
	 *       ExceptionResponse.of(status, code, message)를 담은 CommonResponse.fail를 본문으로 하는 ResponseEntity를 생성해 반환한다.
	 *
	 * @param e 처리할 CoTaskException
	 * @return 예외에 명시된 HTTP 상태와 표준화된 예외 응답을 포함한 ResponseEntity
	 */
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

	/**
	 * 유효성 검사 실패(MethodArgumentNotValidException)를 처리하여 필드별 오류 메시지를 포함한
	 * 표준화된 에러 응답을 HTTP 400으로 반환한다.
	 *
	 * 반환되는 응답은 CommonResponse.fail(...) 형태이며 ExceptionResponse에는
	 * status=HttpStatus.BAD_REQUEST, code="VALIDATION_ERROR", message="입력값이 유효하지 않습니다.",
	 * 그리고 각 필드의 검증 오류 메시지를 담은 맵이 포함된다.
	 *
	 * @return HTTP 400 (Bad Request)와 필드별 검증 오류 정보를 담은 CommonResponse.fail 응답
	 */
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
