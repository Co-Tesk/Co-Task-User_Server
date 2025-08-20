package com.cotask.user_server.infrastructure.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

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
}
