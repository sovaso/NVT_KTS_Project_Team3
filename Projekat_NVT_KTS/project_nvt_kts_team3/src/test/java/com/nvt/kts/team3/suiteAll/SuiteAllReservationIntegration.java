package com.nvt.kts.team3.suiteAll;

import static org.junit.Assert.assertEquals;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import com.nvt.kts.team3.controllerTests.ReservationControllerIntegrationTest;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.repositoryTests.ReservationRepositoryIntegrationTest;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;
import com.nvt.kts.team3.serviceTests.ReservationServiceIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({ReservationServiceIntegrationTest.class,ReservationRepositoryIntegrationTest.class,ReservationControllerIntegrationTest.class})
public class SuiteAllReservationIntegration {
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	//private String accessToken;
	
	
	@Before
	public void login() {
		JwtAuthenticationRequest authenticationRequest=new JwtAuthenticationRequest();
		authenticationRequest.setUsername("user1");
		authenticationRequest.setPassword("string");
		ResponseEntity<Object> responseEntity = 
				restTemplate.postForEntity("/auth/login", 
						authenticationRequest,Object.class);
		System.out.println(responseEntity.getBody());
		// preuzmemo token jer ce nam trebati za testiranje REST kontrolera
		//accessToken = responseEntity.getBody();
		
	}
	
	

}
