package com.cotask.user_server.service;

import java.util.UUID;

public interface MailService {
	void sendEmailVerification(String email, UUID code);
}
