package com.cotask.user_server.infrastructure.utils;

public class Constants {
	public static final String[] PUBLIC_PATH = {
		"/actuator/**",
		"/users/auth/**",
	};

	public static final String[] SWAGGER_PATH = {
		"/swagger-ui/**",
		"/v3/api-docs/**",
		"/swagger-ui.html",
	};
	// token constants
	public static final String ACCESS_TOKEN_HEADER = "X-ACCESS-TOKEN";
	public static final String REFRESH_TOKEN_COOKIE = "X-REFRESH-TOKEN";
	private Constants() {
	}

}
