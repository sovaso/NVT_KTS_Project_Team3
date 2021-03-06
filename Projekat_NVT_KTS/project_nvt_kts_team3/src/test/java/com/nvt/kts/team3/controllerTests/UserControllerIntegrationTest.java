package com.nvt.kts.team3.controllerTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.dto.LocationDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.dto.UserDTO;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

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
	@Transactional
	public void getLoggedSuccessfull() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(null, headers);
		ResponseEntity<RegularUser> responseEntity = testRestTemplate.exchange("/api/getLogged", HttpMethod.GET,
				httpEntity, RegularUser.class);
		RegularUser user = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("user2", user.getUsername());
		
	}
	
	
	@Test
	@Transactional
	public void findByIdSuccessfull() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(null, headers);
		ResponseEntity<RegularUser> responseEntity = testRestTemplate.exchange("/api/user/2", HttpMethod.GET,
				httpEntity, RegularUser.class);
		RegularUser user = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("user2", user.getUsername());
		assertEquals("user2", user.getName());
		assertEquals("user2", user.getSurname());
	}
	
	@Test
	@Transactional
	public void getAllUsers() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(null, headers);
		ResponseEntity<User[]> responseEntity = testRestTemplate.exchange("/api/user/all", HttpMethod.GET,
				httpEntity, User[].class);
		User[] users = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(6, users.length);
	}
	

	

	
}
