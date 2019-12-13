package com.nvt.kts.team3.serviceTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.UserDTO;
import com.nvt.kts.team3.model.Administrator;
import com.nvt.kts.team3.model.Authority;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.model.UserRoleName;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.repository.UserRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.TicketService;
import com.nvt.kts.team3.service.impl.CustomUserDetailsService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomUserDetailsServiceUnitTest {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@MockBean
	private UserRepository userRepositoryMock;
	
	private static final String USERNAME = "marinamaki";
	
	@MockBean
	SecurityContext securityContext;

	@MockBean
	Authentication authentication;
	
	@MockBean
	AuthenticationManager authenticationManager;
	
	@Test
	public void usernameTaken_false() {
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(null);
		Boolean result = customUserDetailsService.usernameTaken(USERNAME);
		verify(userRepositoryMock).findByUsername(USERNAME);
		assertFalse(result);
	}
	
	@Test
	public void usernameTaken_true() {
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(new RegularUser());
		Boolean result = customUserDetailsService.usernameTaken(USERNAME);
		verify(userRepositoryMock).findByUsername(USERNAME);
		assertTrue(result);
	}
	
	@Test
	public void loadUserByUsername_userSuccessfull() {
		User user = new RegularUser();
		user.setUsername(USERNAME);
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(user);
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(USERNAME);
		verify(userRepositoryMock).findByUsername(USERNAME);
		assertEquals(user.getUsername(), userDetails.getUsername());
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void loadUserByUsername_userNull() {
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(null);
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(USERNAME);
		verify(userRepositoryMock).findByUsername(USERNAME);
		
	}
	
	@Test
	public void loadUserById_successfull() {
		User user = new RegularUser();
		user.setUsername(USERNAME);
		user.setId(1L);
		when(userRepositoryMock.findById(1L)).thenReturn(user);
		UserDetails userDetails = customUserDetailsService.loadUserById(1L);
		verify(userRepositoryMock).findById(1L);
		assertEquals(user.getUsername(), userDetails.getUsername());
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void loadUserById_userNull() {
		when(userRepositoryMock.findById(1L)).thenReturn(null);
		UserDetails userDetails = customUserDetailsService.loadUserById(1L);
		verify(userRepositoryMock).findById(1L);
	}
	
	@Test
	public void registerUser_regular_usernameExist() {
		UserDTO userDto = new UserDTO();
		userDto.setUsername(USERNAME);
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(new RegularUser());
		boolean result = customUserDetailsService.registerUser(userDto, UserRoleName.ROLE_USER);
		assertFalse(result);
	}
	
	@Test
	public void registerUser_admin_usernameExist() {
		UserDTO admin = new UserDTO();
		admin.setUsername(USERNAME);
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(new Administrator());
		boolean result = customUserDetailsService.registerUser(admin, UserRoleName.ROLE_ADMIN);
		assertFalse(result);
	}
	
	@Test
	public void registerUser_regular_successfull() {
		UserDTO userDto = new UserDTO();
		userDto.setUsername(USERNAME);
		userDto.setName("Marina");
		userDto.setId(10L);
		userDto.setEmail("marina.vojnovic1997@gmail.com");
		userDto.setPassword("123");
		userDto.setSurname("Vojnovic");
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(null);
		boolean result = customUserDetailsService.registerUser(userDto, UserRoleName.ROLE_USER);
		assertTrue(result);
	}
	
	@Test
	public void registerUser_admin_successfull() {
		UserDTO userDto = new UserDTO();
		userDto.setUsername(USERNAME);
		userDto.setName("Marina");
		userDto.setId(10L);
		userDto.setEmail("marina.vojnovic1997@gmail.com");
		userDto.setPassword("123");
		userDto.setSurname("Vojnovic");
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(null);
		boolean result = customUserDetailsService.registerUser(userDto, UserRoleName.ROLE_ADMIN);
		assertTrue(result);
	}
	
	@Test
	public void editUser_regular_successfull() {
		UserDTO userDto = new UserDTO();
		userDto.setUsername(USERNAME);
		userDto.setPassword("123");
		userDto.setName("Marina");
		userDto.setSurname("Vojnovic");
		userDto.setEmail("marina.vojnovic1997@gmail.com");
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(new RegularUser());
		boolean result = customUserDetailsService.editUser(userDto);
		assertTrue(result);
	}
	
	@Test
	public void editUser_admin_successfull() {
		UserDTO userDto = new UserDTO();
		userDto.setUsername(USERNAME);
		userDto.setPassword("123");
		userDto.setName("Marina");
		userDto.setSurname("Vojnovic");
		userDto.setEmail("marina.vojnovic1997@gmail.com");
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(new Administrator());
		boolean result = customUserDetailsService.editUser(userDto);
		assertTrue(result);
	}
	
	@Test
	public void changePassword_successfull() {
		User user = new RegularUser();
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getName()).thenReturn("marinamaki");
		when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("marinamaki", "123"))).thenReturn(authentication);
		when(userRepositoryMock.findByUsername("marinamaki")).thenReturn(user);
		customUserDetailsService.changePassword("123", "456");
	}
	
	@Test
	public void changePassword_unsuccessfull() {
		User user = new RegularUser();
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getName()).thenReturn("marinamaki");
		when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("marinamaki", "123"))).thenReturn(null);
		when(userRepositoryMock.findByUsername("marinamaki")).thenReturn(user);
		customUserDetailsService.changePassword("123", "456");
		
	}
	
}
