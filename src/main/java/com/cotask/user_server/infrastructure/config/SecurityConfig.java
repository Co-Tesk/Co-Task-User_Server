package com.cotask.user_server.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cotask.user_server.infrastructure.utils.Constants;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final Environment environment;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(AbstractHttpConfigurer::disable)
			// 세션 관리
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			// 주소 인가 규칙 설정
			.authorizeHttpRequests(auth -> {
					auth
				// 공개 경로 설정
						.requestMatchers(Constants.PUBLIC_PATH).permitAll();
					if (environment.acceptsProfiles("swagger")) {
						auth.requestMatchers(Constants.SWAGGER_PATH).permitAll();
					}

					auth.anyRequest().authenticated();
				}
			);
		// Swagger 경로 공개 설정 (개발 환경에서만)
		// application.properties 에서 active profile 을 dev 로 설정한 경우에만 Swagger 경로를 공개합니다.

		return http.build();
	}
}
