package com.cotask.user_server.service;

import org.springframework.stereotype.Service;

import com.cotask.user_server.entity.Verify;
import com.cotask.user_server.repository.VerifyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerifyServiceImpl implements VerifyService {
	private final VerifyRepository repository;

	/**
	 * 주어진 Verify 엔티티를 저장하고 영속화된 엔티티를 반환합니다.
	 *
	 * @param verify 저장할 Verify 엔티티
	 * @return 저장 후 영속화된 Verify 엔티티 (식별자 등 영속화 과정에서 변경된 필드가 반영됨)
	 */
	@Override
	public Verify save(Verify verify) {
		return repository.save(verify);
	}
}
