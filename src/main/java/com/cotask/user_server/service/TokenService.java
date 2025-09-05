package com.cotask.user_server.service;

import com.cotask.user_server.dto.response.LoginToken;
import com.cotask.user_server.entity.User;

public interface TokenService {
	LoginToken createLoginToken(User userByEmail);
}
