package com.nvt.kts.team3.controllerTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.dto.UserDTO;
import com.nvt.kts.team3.model.Authority;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.model.UserRoleName;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.impl.CustomUserDetailsService;






@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@MockBean
	private CustomUserDetailsService customUserDetailsServiceMock;
	
	@MockBean
	SecurityContext securityContext;

	@MockBean
	Authentication authentication;
	
	@MockBean
	AuthenticationManager authenticationManager;
	
	private static final String USER_NAME = "Marina";
	private static final String USER_SURNAME = "Vojnovic";
	private static final String USER_USERNAME = "marinaMaki";
	private static final String USER_EMAIL = "marina.vojnovic1997@gmail.com";
	private static final String USER_PASSWORD = "marinamaki";
	private static final Timestamp USER_LAST_PASSWORD_RESET_DATE = null;
	private static final boolean USER_ENABLED = true;
	private static final List<Authority> authorities = new ArrayList<Authority>();
	
	private UserTokenState token;
	
	@Test
	public void registerUserBadRequest() {
		UserDTO userDto = new UserDTO(1L, USER_NAME, USER_SURNAME, USER_USERNAME, USER_EMAIL, USER_PASSWORD,  USER_LAST_PASSWORD_RESET_DATE, USER_ENABLED, authorities);
		when(customUserDetailsServiceMock.registerUser(userDto, UserRoleName.ROLE_USER)).thenReturn(false);
		ResponseEntity<Boolean> responseEntity = testRestTemplate.postForEntity("/auth/registerUser", userDto, Boolean.class);
		Boolean responseBody = responseEntity.getBody();
		assertFalse(responseBody);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	public void registerAdminBadRequest() {
		UserDTO userDto = new UserDTO(1L, USER_NAME, USER_SURNAME, USER_USERNAME, USER_EMAIL, USER_PASSWORD,  USER_LAST_PASSWORD_RESET_DATE, USER_ENABLED, authorities);
		when(customUserDetailsServiceMock.registerUser(userDto, UserRoleName.ROLE_ADMIN)).thenReturn(false);
		ResponseEntity<Boolean> responseEntity = testRestTemplate.postForEntity("/auth/registerAdmin", userDto, Boolean.class);
		Boolean responseBody = responseEntity.getBody();
		assertFalse(responseBody);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	
	
}
