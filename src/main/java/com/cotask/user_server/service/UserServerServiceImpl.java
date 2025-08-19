package com.cotask.user_server.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServerServiceImpl implements UserServerService {
	private final SocialAccountService socialAccountService;
	private final VerifyService verifyService;
	private final UserService userService;
	private final TokenService tokenService;

}
