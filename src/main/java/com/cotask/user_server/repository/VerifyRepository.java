package com.cotask.user_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotask.user_server.entity.User;
import com.cotask.user_server.entity.Verify;

public interface VerifyRepository extends JpaRepository<Verify, Long> {
	/**
 * 주어진 사용자에 대해 isUsed 필드가 false인 Verify 엔티티들의 목록을 반환합니다.
 *
 * @param user 조회할 대상 사용자
 * @return 해당 사용자에 대해 아직 사용되지 않은 Verify 엔티티들의 리스트(없으면 빈 리스트)
 */
List<Verify> findByUserAndIsUsedFalse(User user);
}
