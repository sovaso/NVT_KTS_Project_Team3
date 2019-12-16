package com.nvt.kts.team3.controllerTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.LocationDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.dto.UserDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.UserRoleName;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerIntegrationTest {
	@Autowired
	private TestRestTemplate testRestTemplate;

	
	private String token;
	
	@Before
	public void logIn() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
	}
	
	@Test
	public void registerUser_badRequest() {
		UserDTO userDto = new UserDTO(1L, "Name", "Surname", "user1", "n@n", "123",  null, false, null);
		ResponseEntity<Boolean> responseEntity = testRestTemplate.postForEntity("/auth/registerUser", userDto, Boolean.class);
		Boolean responseBody = responseEntity.getBody();
		assertFalse(responseBody);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	public void registerAdmin_badRequest() {
		UserDTO userDto = new UserDTO(1L, "Name", "Surname", "user1", "n@n", "123",  null, false, null);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<UserDTO> httpEntity = new HttpEntity<UserDTO>(userDto, headers);
		ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange("/auth/registerAdmin", HttpMethod.POST,
				httpEntity, Boolean.class);
		
		
		Boolean responseBody = responseEntity.getBody();
		assertFalse(responseBody);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	/*
	@Test
	public void registerAdmin_unauthorized() {
		UserDTO userDto = new UserDTO(1L, "Name", "Surname", "user1", "n@n", "123",  null, false, null);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+"wwww");
		
		HttpEntity<UserDTO> httpEntity = new HttpEntity<UserDTO>(userDto, headers);
		ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange("/auth/registerAdmin", HttpMethod.POST,
				httpEntity, Boolean.class);
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}
	
	@Test
	public void registerAdmin_unauthorized_noToken() {
		UserDTO userDto = new UserDTO(1L, "Name", "Surname", "user1", "n@n", "123",  null, false, null);
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<UserDTO> httpEntity = new HttpEntity<UserDTO>(userDto, headers);
		ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange("/auth/registerAdmin", HttpMethod.POST,
				httpEntity, Boolean.class);
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}
	*/
	/*
	Uraditi rollback nad bazom
	@Test
	public void registerUser_successfull() {
		UserDTO userDto = new UserDTO(1L, "Name", "Surname", "newRegularUser", "n@n", "123",  null, false, null);
		ResponseEntity<Boolean> responseEntity = testRestTemplate.postForEntity("/auth/registerUser", userDto, Boolean.class);
		Boolean responseBody = responseEntity.getBody();
		assertTrue(responseBody);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	*/
	/*
	Uraditi rollback nad bazom
	@Test
	public void registerUser_successfull() {
		UserDTO userDto = new UserDTO(1L, "Name", "Surname", "newRegularUser", "n@n", "123",  null, false, null);
		ResponseEntity<Boolean> responseEntity = testRestTemplate.postForEntity("/auth/registerUser", userDto, Boolean.class);
		Boolean responseBody = responseEntity.getBody();
		assertTrue(responseBody);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	*/
	/*
	Uraditi rollback nad bazom.
	@Test
	public void registerAdmin_successfull() {
		UserDTO userDto = new UserDTO(1L, "Name", "Surname", "newAdmin", "n@n", "123",  null, false, null);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<UserDTO> httpEntity = new HttpEntity<UserDTO>(userDto, headers);
		ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange("/auth/registerAdmin", HttpMethod.POST,
				httpEntity, Boolean.class);
		
		
		Boolean responseBody = responseEntity.getBody();
		assertTrue(responseBody);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	*/
	@Test
	public void login_admin_test_successfull() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		assertEquals(UserRoleName.ROLE_ADMIN, result.getBody().getUserRoleName());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		
	}
	
	@Test
	public void login_regularUser_test_successfull() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user1", "123"),
				UserTokenState.class);
		assertEquals(UserRoleName.ROLE_USER, result.getBody().getUserRoleName());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		
	}
	
	@Test
	public void login_user_test_badCredentials() {
		ResponseEntity<MessageDTO> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123787"),
						MessageDTO.class);
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		MessageDTO messageDto = result.getBody();
		assertEquals("Wrong username or password.", messageDto.getMessage());
		assertEquals("Error", messageDto.getHeader());
	}
	
	@Test
	public void login_user_test_notVerified() {
		ResponseEntity<MessageDTO> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user5", "123"),
						MessageDTO.class);
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
		MessageDTO messageDto = result.getBody();
		assertEquals("Account is not verified. Check your email.", messageDto.getMessage());
		assertEquals("Error", messageDto.getHeader());
	}

}
