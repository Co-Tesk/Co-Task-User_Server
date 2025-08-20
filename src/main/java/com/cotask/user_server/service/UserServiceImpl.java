package com.cotask.user_server.service;

import org.springframework.stereotype.Service;

import com.cotask.user_server.entity.User;
import com.cotask.user_server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository repository;

	/**
	 * 주어진 이메일을 가진 사용자가 존재하는지 확인합니다.
	 *
	 * @param email 조회할 사용자의 이메일(예: 등록된 계정의 이메일)
	 * @return 해당 이메일을 가진 사용자가 존재하면 {@code true}, 없으면 {@code false}
	 */
	@Override
	public boolean existsByEmail(String email) {
		return repository.existsByEmail(email);
	}

	/**
	 * 사용자를 저장하고 저장된 User 객체를 반환합니다.
	 *
	 * 전달된 User 엔티티를 저장(삽입 또는 갱신)하고, 영속화 과정에서 채워진 필드(예: ID, 생성/수정 시간 등)가 반영된 저장된 엔티티를 반환합니다.
	 *
	 * @param user 저장할 User 엔티티
	 * @return 저장된 User 엔티티
	 */
	@Override
	public User save(User user) {
		return repository.save(user);
	}
}
