package com.nvt.kts.team3.service;

import java.util.List;

import com.nvt.kts.team3.dto.UserDTO;
import com.nvt.kts.team3.model.User;

public interface UserService {
	User findById(Long id);

	User findByUsername(String username);

	List<User> findAll();

	String editProfile(User user);

	User findUserByToken(String token);

	void save(User user);
	
}