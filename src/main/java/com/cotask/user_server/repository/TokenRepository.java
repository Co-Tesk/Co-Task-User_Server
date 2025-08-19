package com.cotask.user_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotask.user_server.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
