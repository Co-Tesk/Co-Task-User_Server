package com.cotask.user_server.dto.request;

import jakarta.validation.constraints.Email;

public record SendEmailVerification(
	@Email
	String email
) {
}
