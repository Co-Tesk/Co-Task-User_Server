package com.cotask.user_server.infrastructure.utils;

import java.time.Duration;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	private final SecretKey accessSecretKey;
	private final SecretKey refreshSecretKey;
	private final Long accessExpireMs;
	private final Long refreshExpireMs;
	private final String issuer;

	public JwtUtil(
		@Value("${spring.security.jwt.access-key}") String accessKey,
		@Value("${spring.security.jwt.refresh-key}") String refreshKey,
		@Value("${spring.security.jwt.issuer-uri:http://localhost:8081/realms/co-task}") String issuerUri
	) {
		if (accessKey == null || accessKey.isBlank()) {
			throw new IllegalArgumentException("spring.security.jwt.access-key is required");
		}
		if (refreshKey == null || refreshKey.isBlank()) {
			throw new IllegalArgumentException("spring.security.jwt.refresh-key is required");
		}
		accessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
		refreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
		issuer = issuerUri;
		accessExpireMs = Duration.ofSeconds(30L).toMillis();
		refreshExpireMs = Duration.ofDays(7L).toMillis();
	}

	public String getAccessToken(UserDetails userDetails) {
		return generateToken(userDetails, accessExpireMs, "access", accessSecretKey);
	}

	public String getRefreshToken(UserDetails userDetails) {
		return generateToken(userDetails, refreshExpireMs, "refresh", refreshSecretKey);
	}

	private String generateToken(UserDetails userDetails, Long expireMs, String category, SecretKey secretKey) {
		Map<String, Object> claims = new LinkedHashMap<>();
		claims.put("roles", userDetails.getAuthorities());
		claims.put("category", category);
		return Jwts.builder()
			.claims(claims)
			.subject(userDetails.getUsername())
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expireMs))
			.issuer(issuer)
			.signWith(secretKey)
			.compact();
	}
}
