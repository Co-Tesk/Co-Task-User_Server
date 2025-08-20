package com.cotask.user_server.infrastructure.utils;

public class Constants {
	public static final String[] PUBLIC_PATH = {
		"/actuator/**",
		"/users/register"
	};
	// token constants
	public static final String ACCESS_TOKEN_HEADER = "X-ACCESS-TOKEN";
	public static final String REFRESH_TOKEN_COOKIE = "X-REFRESH-TOKEN";
	private Constants() {
	}

}
