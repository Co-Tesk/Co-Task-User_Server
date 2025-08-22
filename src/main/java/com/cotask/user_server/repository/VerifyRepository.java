package com.cotask.user_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotask.user_server.entity.User;
import com.cotask.user_server.entity.Verify;

public interface VerifyRepository extends JpaRepository<Verify, Long> {
	List<Verify> findByUserAndIsUsedFalse(User user);
}
