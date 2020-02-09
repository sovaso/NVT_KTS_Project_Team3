package com.nvt.kts.team3.controllerTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.LocationDTO;
import com.nvt.kts.team3.dto.LocationZoneDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.LocationRepository;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;
import com.nvt.kts.team3.service.impl.LocationServiceImpl;

import exception.InvalidLocationZone;
import exception.LocationExists;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LocationControllerCreateDeleteTest {
	
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
	@Rollback(true)
	public void createLocation_successfull() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		LocationDTO locationDto = new LocationDTO(99, "NewLocationn", "NewAdress", "NewDescriptionn");
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(99, 99, true, "NewLocationZone", 20, 10, 200);
		locationDto.getLocationZone().add(locationZoneDto);
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Success", messageDto.getMessage());
		assertEquals("Location successfully created.", messageDto.getHeader());
	}
	/*
	@Test
	@Transactional
	@Rollback(true)
	public void updateLocation_successfull() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		LocationDTO locationDto = new LocationDTO(1, "NewLocationn", "NewAdress", "NewDescriptionn");
		//LocationZoneDTO locationZoneDto = new LocationZoneDTO(99, 99, true, "NewLocationZone", 20, 10, 200);
		//locationDto.getLocationZone().add(locationZoneDto);
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Success", messageDto.getMessage());
		assertEquals("Location successfully updated.", messageDto.getHeader());
	}
	*/
	
	@Test
	@Transactional
	public void deleteLocation_successfull() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		//LocationDTO locationDto = new LocationDTO(1, "NewLocationn", "NewAdress", "NewDescriptionn");
		//LocationZoneDTO locationZoneDto = new LocationZoneDTO(99, 99, true, "NewLocationZone", 20, 10, 200);
		//locationDto.getLocationZone().add(locationZoneDto);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteLocation/6", HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Success", messageDto.getMessage());
		assertEquals("Location successfully deleted.", messageDto.getHeader());
	}
	

}
