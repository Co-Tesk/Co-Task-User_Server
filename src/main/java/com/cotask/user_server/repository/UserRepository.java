package com.cotask.user_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotask.user_server.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);
}
