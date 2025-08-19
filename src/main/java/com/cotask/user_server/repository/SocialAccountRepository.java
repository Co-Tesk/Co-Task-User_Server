package com.cotask.user_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotask.user_server.entity.SocialAccount;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
}
