package com.nvt.kts.team3.serviceTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.UserDTO;
import com.nvt.kts.team3.model.Administrator;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.model.UserRoleName;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.TicketService;
import com.nvt.kts.team3.service.impl.CustomUserDetailsService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomUserDetailsServiceIntegrationTest {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Test
	public void usernameTaken_false() {
		Boolean result = customUserDetailsService.usernameTaken("someUsername");
		assertFalse(result);
	}
	

	@Test
	public void usernameTaken_true() {
		Boolean result = customUserDetailsService.usernameTaken("user1");
		assertTrue(result);
	}
	
	@Test
	public void loadUserByUsername_userSuccessfull() {
		UserDetails userDetails = customUserDetailsService.loadUserByUsername("user1");
		User user = (User) userDetails;
		assertEquals("user1", user.getUsername());
		assertEquals("user1", user.getName());
		assertEquals("user1", user.getSurname());
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void loadUserByUsername_userNull() {
		UserDetails userDetails = customUserDetailsService.loadUserByUsername("someUsername");
	}
	
	@Test
	public void loadUserById_successfull() {
		UserDetails userDetails = customUserDetailsService.loadUserById(1L);
		User user = (User) userDetails;
		assertEquals("user1", user.getUsername());
		assertEquals("user1", user.getName());
		assertEquals("user1", user.getSurname());
	}
	

	@Test(expected = UsernameNotFoundException.class)
	public void loadUserById_userNull() {
		UserDetails userDetails = customUserDetailsService.loadUserById(100L);
	}
	
	@Test
	public void registerUser_regular_usernameExist() {
		UserDTO userDto = new UserDTO();
		userDto.setUsername("user1");
		boolean result = customUserDetailsService.registerUser(userDto, UserRoleName.ROLE_USER);
		assertFalse(result);
	}
	
	@Test
	public void registerUser_admin_usernameExist() {
		UserDTO admin = new UserDTO();
		admin.setUsername("user1");
		boolean result = customUserDetailsService.registerUser(admin, UserRoleName.ROLE_ADMIN);
		assertFalse(result);
	}
	/*
	Obezbediti rollback nad bazom
	@Test
	public void registerUser_regular_successfull() {
		UserDTO userDto = new UserDTO();
		userDto.setUsername("newUser");
		userDto.setName("Marina");
		userDto.setId(10L);
		userDto.setEmail("marina.vojnovic1997@gmail.com");
		userDto.setPassword("123");
		userDto.setSurname("Vojnovic");
		boolean result = customUserDetailsService.registerUser(userDto, UserRoleName.ROLE_USER);
		assertTrue(result);
	}
	
	@Test
	public void registerUser_admin_successfull() {
		UserDTO userDto = new UserDTO();
		userDto.setUsername("newUser");
		userDto.setName("Marina");
		userDto.setId(10L);
		userDto.setEmail("marina.vojnovic1997@gmail.com");
		userDto.setPassword("123");
		userDto.setSurname("Vojnovic");
		boolean result = customUserDetailsService.registerUser(userDto, UserRoleName.ROLE_ADMIN);
		assertTrue(result);
	}
	*/
	/*
	Obezbediti rollback nad bazom
	@Test
	public void editUser_regular_successfull() {
		UserDTO userDto = new UserDTO();
		userDto.setUsername("user1");
		userDto.setPassword("123");
		userDto.setName("editedName");
		userDto.setSurname("editedSurname");
		userDto.setEmail("marina.vojnovic1997@gmail.com");
		boolean result = customUserDetailsService.editUser(userDto);
		assertTrue(result);
	}
	
	
	@Test
	public void editUser_admin_successfull() {
		UserDTO userDto = new UserDTO();
		userDto.setUsername("user2");
		userDto.setPassword("123");
		userDto.setName("Marina");
		userDto.setSurname("Vojnovic");
		userDto.setEmail("marina.vojnovic1997@gmail.com");
		boolean result = customUserDetailsService.editUser(userDto);
		assertTrue(result);
	}
	*/
	

}
