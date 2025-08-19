package com.cotask.user_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotask.user_server.entity.Verify;

public interface VerifyRepository extends JpaRepository<Verify, Long> {
}
