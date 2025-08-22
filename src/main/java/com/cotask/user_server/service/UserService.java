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
 * 주어진 사용자 엔티티를 저장하고 영속화된(User 상태로 관리되는) 엔티티를 반환합니다.
 *
 * <p>저장 과정에서 식별자 등 영속화된 상태가 변경된 User 객체(예: ID가 할당된 상태)를 반환합니다.</p>
 *
 * @param user 저장할 사용자 엔티티
 * @return 영속화되어 저장된 User 객체
 */
	User save(User user);

	/**
 * 주어진 이메일로 사용자를 조회하여 반환합니다.
 *
 * @param email 조회할 사용자의 이메일 주소
 * @return 이메일과 일치하는 User 객체, 존재하지 않으면 null
 */
User findByEmail(String email);
}
