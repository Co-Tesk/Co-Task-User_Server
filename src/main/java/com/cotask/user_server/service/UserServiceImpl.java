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

	/**
	 * 주어진 이메일을 가진 사용자가 저장소에 존재하는지 확인합니다.
	 *
	 * @param email 확인할 사용자 이메일
	 * @return 해당 이메일을 가진 사용자가 존재하면 {@code true}, 없으면 {@code false}
	 */
	@Override
	public boolean existsByEmail(String email) {
		return repository.existsByEmail(email);
	}

	/**
	 * 사용자 엔터티를 저장하고 영속화된 엔터티를 반환합니다.
	 *
	 * 전달된 User 객체를 생성 또는 갱신하여 저장하며, 저장 후 DB에서 채워진 필드(예: id, 생성/수정 시각 등)가 반영된 User 인스턴스를 반환합니다.
	 *
	 * @param user 저장할 사용자 엔터티(새 객체 또는 변경된 엔터티)
	 * @return DB에 영속화되어 필드가 반영된 User 객체
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
