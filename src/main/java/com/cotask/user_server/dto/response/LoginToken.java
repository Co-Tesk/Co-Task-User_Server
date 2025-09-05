package com.cotask.user_server.dto.response;

import lombok.Builder;

@Builder
public record LoginToken(
	String accessToken,
	String refreshToken
) {
}
