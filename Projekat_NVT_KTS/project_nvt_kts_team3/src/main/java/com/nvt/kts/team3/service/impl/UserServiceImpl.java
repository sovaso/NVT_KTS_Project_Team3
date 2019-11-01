package com.nvt.kts.team3.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.repository.UserRepository;
import com.nvt.kts.team3.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	public User findUserByToken(String token) {
		return userRepository.findByToken(token);
	}

	public void save(User user) {
		userRepository.save(user);
	}

	public String editProfile(User user) {
		User userToEdit = userRepository.findOneByUsername(user.getUsername());
		if (userToEdit == null) {
			return "User with given username does not exist.";
		}

		String firstName = user.getName();
		if (firstName != null) {
			userToEdit.setName(firstName);
		}

		String lastName = user.getSurname();
		if (lastName != null) {
			userToEdit.setSurname(lastName);
		}

		try {
			userRepository.save(userToEdit);
		} catch (Exception e) {
			return "Database error.";
		}

		return null;
	}

	@Override
	public User findById(Long id) {
		return userRepository.getOne(id);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

}
