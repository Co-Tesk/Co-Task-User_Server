package com.cotask.user_server.service;

import org.springframework.stereotype.Service;

import com.cotask.user_server.entity.User;
import com.cotask.user_server.infrastructure.exception.CoTaskException;
import com.cotask.user_server.infrastructure.exception.CoTaskExceptionCode;
import com.cotask.user_server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository repository;

	@Override
	public boolean existsByEmail(String email) {
		return repository.existsByEmail(email);
	}

	@Override
	public User save(User user) {
		return repository.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return repository.findByEmail(email)
			.orElseThrow(() ->
				new CoTaskException(CoTaskExceptionCode.NOT_FOUND_USER)
			);
	}
}
