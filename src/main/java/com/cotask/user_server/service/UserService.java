package com.cotask.user_server.service;

import com.cotask.user_server.entity.User;

public interface UserService {
	/**
	 * 이메일의 사용 여부를 확인합니다.
	 * @param email 이메일 주소
	 * @return 이메일이 이미 사용 중이면 true, 그렇지 않으면 false
	 */
	boolean existsByEmail(String email);

	/**
	 * 사용자의 정보를 저장합니다.
	 * @param user 사용자 정보를 담고 있는 객체
	 * @return 저장된 사용자 객체
	 */
	User save(User user);
}
