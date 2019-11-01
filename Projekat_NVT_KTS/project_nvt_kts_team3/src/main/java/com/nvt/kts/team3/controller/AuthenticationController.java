package com.nvt.kts.team3.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
		// DOPUNITI :D
		/*
		 * if (this.userDetailsService.usernameTaken(user.getUsername())) { return new
		 * ResponseEntity<>(new MessageDTO("Username is already taken.", "Error"),
		 * HttpStatus.OK); }
		 * 
		 * RegularUser newUser = new RegularUser();
		 * newUser.setUsername(user.getUsername()); newUser.setId(null);
		 * newUser.setEmail(user.getEmail());
		 * newUser.setPassword(this.userDetailsService.encodePassword(user.getPassword()
		 * )); List<Authority> authorities = new ArrayList<>(); Authority a = new
		 * Authority(); a.setName(UserRoleName.ROLE_USER); authorities.add(a);
		 * newUser.setAuthorities(authorities); newUser.setEnabled(false);
		 * newUser.setFirstName(user.getFirstName());
		 * newUser.setLastName(user.getLastName()); newUser.setLastPasswordResetDate(new
		 * Timestamp(System.currentTimeMillis()));
		 * newUser.setPhoneNumber(user.getPhoneNumber()); newUser.setFirstTime(true);
		 * 
		 * if (this.userDetailsService.saveUser(newUser)) { return new
		 * ResponseEntity<>(newUser, HttpStatus.OK); }
		 */
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@PostMapping(value = "auth/registerAdmin")
	public ResponseEntity<?> registerAdmin(@RequestBody UserDTO user) {
		// DOPUNITI :D
		/*
		 * if (this.userDetailsService.usernameTaken(user.getUsername())) { return new
		 * ResponseEntity<>(new MessageDTO("Username is already taken.", "Error"),
		 * HttpStatus.OK); }
		 * 
		 * User sa = new User(); sa.setUsername(user.getUsername()); sa.setId(null);
		 * sa.setEmail(user.getEmail());
		 * sa.setPassword(this.userDetailsService.encodePassword(user.getPassword()));
		 * List<Authority> authorities = new ArrayList<>(); Authority a = new
		 * Authority(); a.setName(UserRoleName.ROLE_SYSTEM_ADMIN); authorities.add(a);
		 * sa.setAuthorities(authorities); sa.setEnabled(true);
		 * sa.setFirstName(user.getFirstName()); sa.setLastName(user.getLastName());
		 * sa.setLastPasswordResetDate(new Timestamp(System.currentTimeMillis()));
		 * sa.setPhoneNumber(user.getPhoneNumber()); sa.setFirstTime(true);
		 * 
		 * if (this.userDetailsService.saveUser(sa)) { return new ResponseEntity<>(true,
		 * HttpStatus.OK); }
		 */
		return new ResponseEntity<>(false, HttpStatus.OK);
	}

	@PostMapping(value = "auth/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
			HttpServletResponse response) throws AuthenticationException, IOException {

		final Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			return new ResponseEntity<>(new MessageDTO("Wrong username or password.", "Error"), HttpStatus.OK);
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
