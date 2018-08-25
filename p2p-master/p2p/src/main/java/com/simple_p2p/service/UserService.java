package com.simple_p2p.service;

import com.simple_p2p.entity.User;

public interface UserService {
	User findUserByEmail(String email);
	void saveUser(User user);
}
