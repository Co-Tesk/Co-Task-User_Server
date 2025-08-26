package com.cotask.user_server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.cotask.user_server.entity.User;
import com.cotask.user_server.entity.Verify;
import com.cotask.user_server.entity.VerifyType;

import jakarta.persistence.LockModeType;

public interface VerifyRepository extends JpaRepository<Verify, Long> {
	/**
 * 주어진 사용자에 대해 isUsed 필드가 false인 Verify 엔티티들의 목록을 반환합니다.
 *
 * @param user 조회할 대상 사용자
 * @return 해당 사용자에 대해 아직 사용되지 않은 Verify 엔티티들의 리스트(없으면 빈 리스트)
 */
List<Verify> findByUserAndIsUsedFalse(User user);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select v from Verify v where v.user = :user and v.type = :type and v.code = :code")
	Optional<Verify> findTopByUserAndTypeAndCodeForUpdate(User user, VerifyType type, String code);
}
