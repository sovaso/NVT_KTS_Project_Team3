package com.nvt.kts.team3.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nvt.kts.team3.dto.UserDTO;
import com.nvt.kts.team3.model.Administrator;
import com.nvt.kts.team3.model.Authority;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.model.UserRoleName;
import com.nvt.kts.team3.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	protected final Log LOGGER = LogFactory.getLog(getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	public boolean saveUser(User ru) {
		this.userRepository.save(ru);
		/*try {
			this.userRepository.save(ru);
		} catch (Exception e) {
			return false;
		}
		*/
		return true;
	}

	public boolean usernameTaken(String username) {
		User user = userRepository.findByUsername(username);

		return user != null;
	}

	// Funkcija koja na osnovu username-a iz baze vraca objekat User-a
	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByUsername(username);
	
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return user;
		}
	}

	public UserDetails loadUserById(long id) {
		User user = userRepository.findById(id);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with id '%s'.", id));
		} else {
			return user;
		}
	}

	public String encodePassword(String password) {
		return this.passwordEncoder.encode(password);
	}

	// Funkcija pomocu koje korisnik menja svoju lozinku
	public void changePassword(String oldPassword, String newPassword) {

		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		String username = currentUser.getName();
		if (authenticationManager != null) {
			LOGGER.debug("Re-authenticating user '" + username + "' for password change request.");

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
		} else {
			LOGGER.debug("No authentication manager set. can't change Password!");

			return;
		}

		LOGGER.debug("Changing password for user '" + username + "'");

		User user = (User) loadUserByUsername(username);

		// pre nego sto u bazu upisemo novu lozinku, potrebno ju je hesirati
		// ne zelimo da u bazi cuvamo lozinke u plain text formatu
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);

	}
	
	public boolean registerUser(UserDTO user, UserRoleName userRole) {
		if (this.usernameTaken(user.getUsername())) {
			return false;
		}
		if (userRole.equals(UserRoleName.ROLE_USER)){
			RegularUser newUser = new RegularUser();
			Authority a = new Authority();
			a.setName(UserRoleName.ROLE_USER);
			newUser.setReservations(new HashSet<Reservation>());
			List<Authority> authorities = new ArrayList<>();
			authorities.add(a);
			newUser.setAuthorities(authorities);
			newUser.setId(user.getId());
			newUser.setEmail(user.getEmail());
			newUser.setUsername(user.getUsername());
			newUser.setPassword(this.encodePassword(user.getPassword()));
			newUser.setEnabled(false);
			newUser.setName(user.getName());
			newUser.setSurname(user.getSurname());
			newUser.setLastPasswordResetDate(new Timestamp(System.currentTimeMillis()));
			this.userRepository.save(newUser);
		}else {
			Administrator newUser = new Administrator();
			Authority a = new Authority();
			a.setName(UserRoleName.ROLE_ADMIN);
			List<Authority> authorities = new ArrayList<>();
			authorities.add(a);
			newUser.setAuthorities(authorities);
			newUser.setId(user.getId());
			newUser.setEmail(user.getEmail());
			newUser.setUsername(user.getUsername());
			newUser.setPassword(this.encodePassword(user.getPassword()));
			newUser.setEnabled(false);
			newUser.setName(user.getName());
			newUser.setSurname(user.getSurname());
			newUser.setLastPasswordResetDate(new Timestamp(System.currentTimeMillis()));
			this.userRepository.save(newUser);
		}
		
		
		return true;
	}
	
	public boolean editUser(UserDTO userDto) {
		 User user = (User) this.loadUserByUsername(userDto.getUsername());
		 user.setPassword(this.encodePassword(userDto.getPassword())); 
		 user.setName(userDto.getName());
		 user.setSurname(userDto.getSurname()); 
		 user.setEmail(userDto.getEmail());
		 saveUser(user);
		 return true;
	}

}
