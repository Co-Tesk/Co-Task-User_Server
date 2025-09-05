package com.cotask.user_server.infrastructure.utils;

import java.time.Duration;

public class Constants {
	public static final String[] PUBLIC_PATH = {
		"/actuator/**",
		"/users/auth/register",
		"/users/auth/verification",
		"/users/auth/login"
	};

	public static final String[] SWAGGER_PATH = {
		"/swagger-ui/**",
		"/v3/api-docs/**",
		"/swagger-ui.html",
	};
	// token constants
	public static final String ACCESS_TOKEN_HEADER = "X-ACCESS-TOKEN";
	public static final String REFRESH_TOKEN_COOKIE = "X-REFRESH-TOKEN";

	public static final Long ACCESS_EXPIRE_MS = Duration.ofMinutes(30L).toMillis();
	public static final Long REFRESH_EXPIRE_MS = Duration.ofDays(7L).toMillis();
	private Constants() {
	}

}
