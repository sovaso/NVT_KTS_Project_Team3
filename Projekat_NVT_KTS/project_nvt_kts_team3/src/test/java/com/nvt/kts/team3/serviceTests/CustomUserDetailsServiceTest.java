package com.nvt.kts.team3.serviceTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.UserDTO;
import com.nvt.kts.team3.model.RegularUser;
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
public class CustomUserDetailsServiceTest {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@MockBean
	private UserRepository userRepositoryMock;
	
	private static final String USERNAME = "marinamaki";
	@Test
	public void usernameTakenFalse() {
		when(userRepositoryMock.findOneByUsername(USERNAME)).thenReturn(null);
		Boolean result = customUserDetailsService.usernameTaken(USERNAME);
		verify(userRepositoryMock).findOneByUsername(USERNAME);
		assertFalse(result);
	}
	
	@Test
	public void usernameTakenTrue() {
		when(userRepositoryMock.findOneByUsername(USERNAME)).thenReturn(new RegularUser());
		Boolean result = customUserDetailsService.usernameTaken(USERNAME);
		verify(userRepositoryMock).findOneByUsername(USERNAME);
		assertTrue(result);
	}
	
	@Test
	public void loadUserByUsernameUserSuccessfull() {
		User user = new RegularUser();
		user.setUsername(USERNAME);
		when(userRepositoryMock.findOneByUsername(USERNAME)).thenReturn(user);
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(USERNAME);
		verify(userRepositoryMock).findOneByUsername(USERNAME);
		assertEquals(user.getUsername(), userDetails.getUsername());
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void loadUserByUsernameUserNull() {
		when(userRepositoryMock.findOneByUsername(USERNAME)).thenReturn(null);
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(USERNAME);
		verify(userRepositoryMock).findOneByUsername(USERNAME);
		
	}
	
	@Test
	public void loadUserByIdSuccessfull() {
		User user = new RegularUser();
		user.setUsername(USERNAME);
		user.setId(1L);
		when(userRepositoryMock.findById(1L)).thenReturn(user);
		UserDetails userDetails = customUserDetailsService.loadUserById(1L);
		verify(userRepositoryMock).findById(1L);
		assertEquals(user.getUsername(), userDetails.getUsername());
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void loadUserByIdUserNull() {
		when(userRepositoryMock.findById(1L)).thenReturn(null);
		UserDetails userDetails = customUserDetailsService.loadUserById(1L);
		verify(userRepositoryMock).findById(1L);
	}
	
	//Register user za uspesno treba integraciono
	@Test
	public void registerUserRegularUsernameExist() {
		UserDTO userDto = new UserDTO();
		userDto.setUsername(USERNAME);
		when(userRepositoryMock.findOneByUsername(USERNAME)).thenReturn(new RegularUser());
		boolean result = customUserDetailsService.registerUser(userDto, UserRoleName.ROLE_USER);
		assertFalse(result);
	}
	
	//edit user isto treba integraciono
}
