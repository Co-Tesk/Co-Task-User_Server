package com.cotask.user_server.infrastructure.config;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;

import com.cotask.user_server.annotation.swagger.ApiErrorCodeExamples;
import com.cotask.user_server.dto.response.CommonResponse;
import com.cotask.user_server.dto.response.ExceptionResponse;
import com.cotask.user_server.infrastructure.exception.CoTaskExceptionCode;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import lombok.Builder;
import lombok.Getter;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("사용자 서버 API 문서")
				.version("1.0.0")
				.description("Co-Task 사용자 서버의 API 문서입니다. <br>" +
					"이 문서는 사용자 서버의 엔드포인트와 요청/응답 형식에 대한 정보를 제공합니다."
				)
			).servers(
				List.of(
					new Server().url("http://localhost:8081").description("로컬 개발 서버")
				)
			);
	}

	@Bean
	public OperationCustomizer operationCustomizer() {
		return (Operation operation, HandlerMethod handlerMethod) -> {
			ApiErrorCodeExamples anntotation = findAnnotation(handlerMethod);

			if (anntotation != null) {
				generateErrorCodeResponseExample(operation, anntotation.value());
			}
			return operation;
		};
	}

	private ApiErrorCodeExamples findAnnotation(HandlerMethod handlerMethod) {
		// 메서드 어노테이션을 먼저 찾고, 없으면 클래스 어노테이션을 찾음
		Optional<ApiErrorCodeExamples> annotation = Optional.ofNullable(
			// 1. 메서드에서 어노테이션 찾기
			AnnotatedElementUtils.findMergedAnnotation(
				handlerMethod.getMethod(), ApiErrorCodeExamples.class
			)
		).or(() -> Optional.ofNullable(
			// 2. 클래스에서 어노테이션 찾기
			AnnotatedElementUtils.findMergedAnnotation(
				handlerMethod.getBeanType(), ApiErrorCodeExamples.class
			)
		));
		// 3. 인터페이스에서 어노테이션 찾기
		return annotation.orElseGet(() -> findAnnotationInInterfaces(handlerMethod));
	}

	private ApiErrorCodeExamples findAnnotationInInterfaces(HandlerMethod handlerMethod) {
		Class<?> beanType = handlerMethod.getBeanType();
		return Arrays.stream(beanType.getInterfaces())
			.map(anInterface -> {
				// 1. 인터페이스 자체에 어노테이션이 있는지 확인
				ApiErrorCodeExamples typeAnnotation = AnnotatedElementUtils.findMergedAnnotation(
					anInterface, ApiErrorCodeExamples.class
				);
				if (typeAnnotation != null) {
					return Optional.of(typeAnnotation);
				}

				// 2. 인터페이스의 메서드에 어노테이션이 있는지 확인
				try {
					Method method = anInterface.getMethod(
						handlerMethod.getMethod().getName(),
						handlerMethod.getMethod().getParameterTypes()
					);
					return Optional.ofNullable(
						AnnotatedElementUtils.findMergedAnnotation(method, ApiErrorCodeExamples.class)
					);
				} catch (NoSuchMethodException e) {
					// 인터페이스에 해당 메서드가 없는 경우는 어노테이션이 없는 것으로 간주하고 처리 (에러로 보지 않음)
					return Optional.<ApiErrorCodeExamples>empty();
				}
			})
			.filter(Optional::isPresent) // Optional.empty()인 경우는 필터링
			.map(Optional::get) // 값 추출
			.findFirst() // 첫 번째로 찾은 어노테이션 반환
			.orElse(null); // 모든 인터페이스에서 찾지 못하면 null 반환
	}

	private ExampleHolder getValidationErrorHolder() {
		;
		Example example = new Example();
		example.setValue(
			CommonResponse.fail(
				ExceptionResponse.of(
					HttpStatus.BAD_REQUEST,
					"VALIDATION_ERROR",
					"입력값이 유효하지 않습니다.",
					Map.of(
						"field1", "field1은(는) 필수 입력 항목입니다.",
						"field2", "field2은(는) 0 이상이어야 합니다."
					) // 필드별 메시지 포함
				)
			)
		);
		example.setDescription("입력값이 validation 기준을 통과하지 못했을 때 발생하는 에러 예시입니다.");
		return ExampleHolder.builder()
			.holder(example)
			.name("VALIDATION_ERROR")
			.code(400)
			.build();
	}

	private Map<Integer, List<ExampleHolder>> getGroupedExamples(CoTaskExceptionCode[] codes) {
		return Arrays.stream(codes)
			.map(code -> ExampleHolder.builder()
				.holder(getSwaggerExample(code))
				.name(code.getCode())
				.code(code.getStatus().value())
				.build()
			)
			.collect(Collectors.groupingBy(ExampleHolder::getCode));
	}

	private Example getSwaggerExample(CoTaskExceptionCode code) {
		String explain = "";
		try {
			explain = code.getExplainError();
		} catch (NoSuchFieldException e) {
			explain = code.getMessage();
		}

		CommonResponse<Object> fail = CommonResponse.fail(ExceptionResponse.of(
			code.getStatus(),
			code.getCode(),
			code.getMessage()
		));

		Example example = new Example();
		example.setValue(fail);
		example.setDescription(explain);
		return example;
	}

	private void generateErrorCodeResponseExample(
		Operation operation,
		CoTaskExceptionCode[] codes
	) {
		ApiResponses responses = operation.getResponses();

		ExampleHolder validationErrorHolder = getValidationErrorHolder();

		// `CoTaskExceptionCode` 배열을 기반으로 `ExampleHolder`를 생성 및 그룹화
		Map<Integer, List<ExampleHolder>> groupedExamples = getGroupedExamples(codes);

		// Validation Error를 400 상태 코드 그룹에 추가
		groupedExamples.computeIfAbsent(400, k -> new java.util.ArrayList<>()).add(validationErrorHolder);

		// 모든 예외를 API 응답에 추가
		groupedExamples.forEach((status, examples) -> {
			Content content = new Content();
			MediaType mediaType = new MediaType();

			examples.forEach(example -> {
				mediaType.addExamples(example.getName(), example.getHolder());
			});

			content.addMediaType("application/json", mediaType);

			ApiResponse apiResponse = new ApiResponse();
			apiResponse.setContent(content);
			responses.addApiResponse(String.valueOf(status), apiResponse);
		});
	}
}

@Getter
@Builder
class ExampleHolder {
	private Example holder;
	private String name;
	private int code;
}
