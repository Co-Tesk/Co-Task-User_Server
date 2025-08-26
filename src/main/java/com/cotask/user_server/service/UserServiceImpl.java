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

	/**
	 * 사용자 엔티티를 저장하고 저장된 엔티티를 반환합니다.
	 *
	 * @param user 저장할 사용자 엔티티 (새 엔티티이면 식별자 등 영속화된 값이 채워집니다)
	 * @return 저장되어 영속화된 사용자 엔티티
	 */
	@Override
	public User save(User user) {
		return repository.save(user);
	}

	/**
	 * 이메일로 사용자를 조회하여 반환한다.
	 *
	 * 지정한 이메일과 일치하는 User를 조회한다. 사용자가 존재하지 않으면 CoTaskException(CoTaskExceptionCode.NOT_FOUND_USER)을 던진다.
	 *
	 * @param email 조회할 사용자의 이메일
	 * @return 조회된 User 객체
	 * @throws CoTaskException 사용자가 존재하지 않을 경우 (CoTaskExceptionCode.NOT_FOUND_USER)
	 */
	@Override
	public User findByEmail(String email) {
		return repository.findByEmail(email)
			.orElseThrow(() ->
				new CoTaskException(CoTaskExceptionCode.NOT_FOUND_USER)
			);
	}
}
