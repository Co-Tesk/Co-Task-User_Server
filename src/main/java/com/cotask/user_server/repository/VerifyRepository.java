package com.cotask.user_server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
	 * 지정한 사용자·타입·코드에 맞는 단일 Verify 엔티티를 조회하고 조회 시 PESSIMISTIC_WRITE 락을 적용합니다.
	 *
	 * 지정된 user, type, code와 일치하는 Verify를 검색하여 Optional로 반환합니다.
	 * 트랜잭션 내에서 호출되며, 조회 대상에 대해 배타적 쓰기 락(PESSIMISTIC_WRITE)이 걸려 동시 수정으로부터 보호됩니다.
	 *
	 * @param user 조회할 Verify의 소유자 사용자
	 * @param type 조회할 Verify의 타입
	 * @param code 조회할 Verify의 코드
	 * @return 일치하는 Verify가 존재하면 해당 엔티티를 담은 Optional, 없으면 빈 Optional
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select v from Verify v where v.user = :user and v.type = :type and v.code = :code")
	Optional<Verify> findTopByUserAndTypeAndCodeForUpdate(
		@Param("user") User user,
		@Param("type") VerifyType type,
		@Param("code") String code
	);
}
