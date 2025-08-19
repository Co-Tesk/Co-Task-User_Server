package com.cotask.user_server.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 요청에 대한 응답을 구조화하는 공통 DTO 클래스입니다.
 *
 * <p>이 클래스는 요청 성공 여부와 관련 데이터를 포함한 응답 객체를 제공합니다.
 * 제네릭 타입을 사용하여 다양한 데이터 타입을 처리할 수 있습니다.</p>
 *
 * @param <T> 응답 데이터의 타입
 * @param success 요청이 성공했는지 여부를 나타내는 플래그
 * @param data 요청에 대한 실제 응답 데이터
 * @param error 에러가 발생할 경우 받는 데이터
 *
 * @version 1.0
 * @since 2024-07-30
 * @author namung08
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommonResponse<T>(Boolean success, T data, ExceptionResponse error) {
	/**
	 * 성공적인 응답을 생성하는 정적 메서드입니다.
	 *
	 * <p>이 메서드는 주어진 데이터를 포함한 성공적인 응답 객체를 생성합니다.</p>
	 *
	 * @param <T> 응답 데이터의 타입
	 * @param data 응답에 포함할 데이터
	 * @return 성공 플래그가 {@code true}인 {@code CommonResponse} 객체
	 */
	public static <T> CommonResponse<T> success(T data) {
		return new CommonResponse<>(true, data, null);
	}

	/**
	 * 실패한 응답을 생성하는 정적 메서드입니다.
	 *
	 * <p>이 메서드는 주어진 데이터를 포함한 실패한 응답 객체를 생성합니다.</p>
	 *
	 * @param <T> 응답 데이터의 타입
	 * @param error 실패에 대한 정보를 담은 데이터
	 * @return 성공 플래그가 {@code false}인 {@code CommonResponse} 객체
	 */
	public static <T> CommonResponse<T> fail(ExceptionResponse error) {
		return new CommonResponse<>(false, null, error);
	}
}
