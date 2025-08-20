package com.cotask.user_server.service;

import org.springframework.stereotype.Service;

import com.cotask.user_server.entity.Verify;
import com.cotask.user_server.repository.VerifyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerifyServiceImpl implements VerifyService {
	private final VerifyRepository repository;

	@Override
	public Verify save(Verify verify) {
		return repository.save(verify);
	}
}
