package com.cotask.user_server.service;

import org.springframework.stereotype.Service;

import com.cotask.user_server.repository.SocialAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocialAccountServiceImpl implements SocialAccountService {
	private final SocialAccountRepository repository;

}
