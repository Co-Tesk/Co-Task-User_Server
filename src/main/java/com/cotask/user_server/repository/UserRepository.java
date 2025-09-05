package com.cotask.user_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotask.user_server.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	/**
 * 주어진 이메일을 가진 사용자가 존재하는지 확인합니다.
 *
 * 이 메서드는 Spring Data JPA의 파생 쿼리로 런타임에 구현됩니다.
 *
 * @param email 확인할 사용자 이메일
 * @return 해당 이메일을 가진 사용자가 존재하면 {@code true}, 그렇지 않으면 {@code false}
 */
boolean existsByEmail(String email);

	/**
 * 주어진 이메일로 User 엔티티를 조회한다.
 *
 * 주어진 이메일에 대응하는 사용자 엔티티가 존재하면 해당 User를 Optional에 담아 반환하고,
 * 존재하지 않으면 Optional.empty()를 반환한다.
 *
 * @param email 조회할 사용자의 이메일 주소
 * @return 조회된 User를 담은 Optional, 없으면 Optional.empty()
 */
Optional<User> findByEmail(String email);
}
