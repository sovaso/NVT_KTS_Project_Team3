package com.nvt.kts.team3.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.dto.UserDTO;
import com.nvt.kts.team3.model.Administrator;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.model.UserRoleName;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.security.TokenHelper;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;
import com.nvt.kts.team3.service.impl.CustomUserDetailsService;

@RestController
@CrossOrigin(origins="http://localhost:4200", allowedHeaders = "*")
public class AuthenticationController {
	@Autowired
	TokenHelper tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@PostMapping(value = "auth/registerUser")
	public ResponseEntity<Boolean> registerUser(@RequestBody UserDTO user) {
		Boolean result = this.userDetailsService.registerUser(user, UserRoleName.ROLE_USER);
		if (result == true) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
	
	}

	@PostMapping(value = "auth/registerAdmin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Boolean> registerAdmin(@RequestBody UserDTO user) {
		Boolean result = this.userDetailsService.registerUser(user, UserRoleName.ROLE_ADMIN);
		if (result == true) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		
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
					HttpStatus.FORBIDDEN);
		}
		
		User user = (User) authentication.getPrincipal();
		
		// Ubaci username + password u kontext
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		//System.out.println("NAME"+SecurityContextHolder.getContext().getAuthentication().getName());
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
