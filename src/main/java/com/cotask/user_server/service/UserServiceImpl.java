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
	 * 사용자 엔터티를 저장하고 영속화된 엔터티를 반환합니다.
	 *
	 * 전달된 User 객체를 저장(생성 또는 갱신)하여 데이터베이스에 영속화하고,
	 * 저장 결과로 식별자(id) 등 데이터베이스에서 채워진 필드를 반영한 User 인스턴스를 반환합니다.
	 *
	 * @param user 저장할 사용자 엔터티 (새 객체이거나 변경된 엔터티)
	 * @return 영속화되어 DB 필드가 반영된 User 객체
	 */
	@Override
	public User save(User user) {
		return repository.save(user);
	}

	/**
	 * 이메일로 사용자를 조회하여 반환한다.
	 *
	 * 지정한 이메일과 일치하는 사용자가 존재하면 해당 User를 반환한다.
	 *
	 * @param email 조회할 사용자의 이메일
	 * @return 조회된 User 객체
	 * @throws CoTaskException 해당 이메일의 사용자가 존재하지 않을 경우 (CoTaskExceptionCode.NOT_FOUND_USER)
	 */
	@Override
	public User findByEmail(String email) {
		return repository.findByEmail(email)
			.orElseThrow(() ->
				new CoTaskException(CoTaskExceptionCode.NOT_FOUND_USER)
			);
	}
}
