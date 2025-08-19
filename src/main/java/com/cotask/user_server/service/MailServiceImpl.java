package com.cotask.user_server.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
	@Override
	public void sendEmailVerification(String email, UUID code) {
		// TODO: Implement the logic to send an email verification
	}
}
