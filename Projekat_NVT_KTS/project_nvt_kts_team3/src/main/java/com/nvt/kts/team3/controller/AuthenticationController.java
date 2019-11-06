package com.nvt.kts.team3.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.dto.UserDTO;
import com.nvt.kts.team3.model.Administrator;
import com.nvt.kts.team3.model.Authority;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.model.UserRoleName;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.security.TokenHelper;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;
import com.nvt.kts.team3.service.impl.CustomUserDetailsService;

@RestController
public class AuthenticationController {
	@Autowired
	TokenHelper tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@PostMapping(value = "auth/registerUser")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO user) {
		
		
		if (this.userDetailsService.usernameTaken(user.getUsername())) {
			return new ResponseEntity<>(new MessageDTO("Username is already take.", "Error"), HttpStatus.OK);
		}
		RegularUser regularUser = new RegularUser();
		regularUser.setId(user.getId());
		regularUser.setEmail(user.getEmail());
		regularUser.setUsername(user.getUsername());
		regularUser.setPassword(this.userDetailsService.encodePassword(user.getPassword()));
		List<Authority> authorities = new ArrayList<>();
		Authority a = new Authority();
		a.setName(UserRoleName.ROLE_USER);
		authorities.add(a);
		regularUser.setAuthorities(authorities);
		regularUser.setEnabled(true);
		regularUser.setName(user.getName());
		regularUser.setSurname(user.getSurname());
		regularUser.setLastPasswordResetDate(new Timestamp(System.currentTimeMillis()));
		regularUser.setReservations(new HashSet<Reservation>());
		if (this.userDetailsService.saveUser(regularUser)) { 
			return new ResponseEntity<>(true, HttpStatus.OK); 
			}
		 
		return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
	
	}

	@PostMapping(value = "auth/registerAdmin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> registerAdmin(@RequestBody UserDTO user) {
		
		if (this.userDetailsService.usernameTaken(user.getUsername())) {
			return new ResponseEntity<>(new MessageDTO("Username is already take.", "Error"), HttpStatus.OK);
		}
		Administrator admin = new Administrator();
		admin.setId(user.getId());
		admin.setEmail(user.getEmail());
		admin.setUsername(user.getUsername());
		admin.setPassword(this.userDetailsService.encodePassword(user.getPassword()));
		List<Authority> authorities = new ArrayList<>();
		Authority a = new Authority();
		a.setName(UserRoleName.ROLE_ADMIN);
		authorities.add(a);
		admin.setAuthorities(authorities);
		admin.setEnabled(true);
		admin.setName(user.getName());
		admin.setSurname(user.getSurname());
		admin.setLastPasswordResetDate(new Timestamp(System.currentTimeMillis()));
		
		if (this.userDetailsService.saveUser(admin)) { 
			return new ResponseEntity<>(true, HttpStatus.OK); 
			}
		 
		return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
	}

	@PostMapping(value = "auth/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
			HttpServletResponse response) throws AuthenticationException, IOException {

		final Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			return new ResponseEntity<>(new MessageDTO("Wrong username or password.", "Error"), HttpStatus.NOT_FOUND);
		} catch (DisabledException e) {
			return new ResponseEntity<>(new MessageDTO("Account is not verified. Check your email.", "Error"),
					HttpStatus.OK);
		}
		User user = (User) authentication.getPrincipal();

		// Ubaci username + password u kontext
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Kreiraj token
		String jwt = tokenUtils.generateToken(user.getUsername());
		int expiresIn = tokenUtils.getExpiredIn();
		UserRoleName userType = null;

		if (user instanceof Administrator) {
			userType = UserRoleName.ROLE_ADMIN;
		} else {
			userType = UserRoleName.ROLE_USER;
		}

		// Vrati token kao odgovor na uspesno autentifikaciju
		return new ResponseEntity<>(new UserTokenState(jwt, expiresIn, userType), HttpStatus.OK);
	}
}
