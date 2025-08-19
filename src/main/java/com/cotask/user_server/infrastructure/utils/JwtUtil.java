package com.cotask.user_server.infrastructure.utils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
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
		@Value("${spring.security.jwt.access-key:1f12433956f8c3ad7ecbdf79f15dae0172886e05b32d7f7e016099c58dc01815e968c0d40ae0dc15a20b85b15466fcdfc6532f63bb52ba851b8cde1ceb37fe30}") String accessKey,
		@Value("${spring.security.jwt.refresh-key:7eaff885fbc892ed852ddee99ff75e5f706557ad69c26bef1802ee1a158aab2241310aedff79533fc086771a21e8f4095fc05b4409f0c8c7406d5f04a6082d0c}") String refreshKey,
		@Value("${spring.security.jwt.issuer-uri:http://localhost:8081/realms/co-task}") String issuerUri
	) {
		accessSecretKey = new SecretKeySpec(accessKey.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm());
		refreshSecretKey = new SecretKeySpec(refreshKey.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm());
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
