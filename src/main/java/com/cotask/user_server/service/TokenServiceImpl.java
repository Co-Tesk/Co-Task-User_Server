package com.cotask.user_server.service;

import static com.cotask.user_server.infrastructure.utils.Constants.*;

import java.time.Instant;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotask.user_server.dto.response.LoginToken;
import com.cotask.user_server.entity.Token;
import com.cotask.user_server.entity.User;
import com.cotask.user_server.infrastructure.utils.JwtUtil;
import com.cotask.user_server.repository.TokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
	private final JwtUtil jwtUtil;
	private final TokenRepository repository;
	private final PasswordEncoder encoder;

	@Override
	@Transactional
	public LoginToken createLoginToken(User userByEmail) {
		String accessToken = jwtUtil.getAccessToken(userByEmail.getEmail());
		String refreshToken = jwtUtil.getRefreshToken(userByEmail.getEmail());

		repository.save(
			Token.builder()
				.refreshToken(encoder.encode(refreshToken))
				.user(userByEmail)
				.expiredDate(Instant.now().plusMillis(REFRESH_EXPIRE_MS))
				.build()
		);
		return LoginToken.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
