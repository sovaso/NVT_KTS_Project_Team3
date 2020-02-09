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
public class UserControllerEditUserIntegrationTest {

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
	
	/*
	@Test
	public void editUserSuccessfull() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		UserDTO userDto = new UserDTO();
		userDto.setUsername("user1");
		userDto.setName("NewNameOfUser1");
		userDto.setSurname("NewSurnameOfUser1");
		HttpEntity<UserDTO> httpEntity = new HttpEntity<UserDTO>(userDto, headers);
		ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange("/api/editUser", HttpMethod.PUT,
				httpEntity, Boolean.class);
	
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	*/
}
