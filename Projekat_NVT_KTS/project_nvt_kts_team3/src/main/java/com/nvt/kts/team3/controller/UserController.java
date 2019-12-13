package com.nvt.kts.team3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.nvt.kts.team3.dto.UserDTO;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.security.TokenHelper;
import com.nvt.kts.team3.service.UserService;
import com.nvt.kts.team3.service.impl.CustomUserDetailsService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="http://localhost:4200", allowedHeaders = "*")
public class UserController {

	@Autowired
	TokenHelper tokenUtils;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	@GetMapping(value = "/confirmRegistration/{username}")
	public RedirectView confirmRegistration(@PathVariable String username) {
		System.out.println("Uslo u confirm registration");
	
		User user = (User) userDetailsService.loadUserByUsername(username);
		System.out.println(user.isEnabled());
		if (user != null) {
			user.setEnabled(true);
			userDetailsService.saveUser(user);
			return new RedirectView("http://localhost:4200");
		}
		return null;
	}

	@GetMapping(value = "/getLogged", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public ResponseEntity<User> getLogged() {
		User user = (User) this.userDetailsService
				.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PutMapping(value = "/editUser", consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public ResponseEntity<Boolean> editUser(@RequestBody UserDTO userEdit) {
		return new ResponseEntity<>(userDetailsService.editUser(userEdit), HttpStatus.OK);
	}

	@GetMapping(value = "/user/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public User loadById(@PathVariable Long userId) {
		return this.userService.findById(userId);
	}

	@GetMapping(value = "/user/all")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<User> loadAll() {
		return this.userService.findAll();
	}

}
