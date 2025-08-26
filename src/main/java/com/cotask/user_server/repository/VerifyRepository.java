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

	/**
	 * 지정한 사용자, 타입, 코드에 일치하는 Verify 엔티티를 조회하고 조회된 행에 대해 비관적 쓰기(PESSIMISTIC_WRITE) 락을 획득합니다.
	 *
	 * <p>데이터베이스 레벨의 쓰기 락을 사용하여 조회된 엔티티에 대한 동시 수정(경합)을 방지합니다.
	 *
	 * @param user 조회 대상 사용자
	 * @param type 조회 대상 인증 타입
	 * @param code 조회 대상 인증 코드
	 * @return 일치하는 Verify 엔티티를 포함한 Optional. 일치하는 엔티티가 없으면 비어 있음.
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select v from Verify v where v.user = :user and v.type = :type and v.code = :code")
	Optional<Verify> findTopByUserAndTypeAndCodeForUpdate(User user, VerifyType type, String code);
}
