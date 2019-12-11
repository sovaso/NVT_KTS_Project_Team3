package com.nvt.kts.team3.controllerTests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.LocationReportDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.repository.LocationRepository;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;
import com.nvt.kts.team3.service.impl.LocationServiceImpl;

import exception.LocationNotFound;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LocationControllerUnitTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@MockBean
	private LocationServiceImpl locationServiceMock;
	
	private String token;
	

	@Before
	public void logIn() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
	}
	
	@Test
	public void getLocationReport_locationNotFound(){
		when(locationServiceMock.getLocationReport(100L)).thenThrow(LocationNotFound.class);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/getLocationReport/100", HttpMethod.GET,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Location not found.", messageDto.getHeader());
	}
	
	@Test
	public void getLocationReport_successfull(){
		LocationReportDTO locationReportDto = new LocationReportDTO();
		when(locationServiceMock.getLocationReport(100L)).thenReturn(locationReportDto);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/getLocationReport/100", HttpMethod.GET,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
}
