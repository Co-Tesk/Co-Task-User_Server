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
 * 주어진 사용자 정보를 저장하고 영속화된 User 객체를 반환합니다.
 *
 * @param user 저장할 사용자 엔티티
 * @return 저장되어 반환된 User 객체
 */
	User save(User user);
}
