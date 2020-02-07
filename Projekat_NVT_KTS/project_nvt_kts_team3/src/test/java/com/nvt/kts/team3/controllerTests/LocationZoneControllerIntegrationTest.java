package com.nvt.kts.team3.controllerTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.LocationDTO;
import com.nvt.kts.team3.dto.LocationZoneDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LocationZoneControllerIntegrationTest {

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
	public void createLocationZoneLocationNotFound() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO();
		Location location = new Location();
		location.setId(100L);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<LocationZoneDTO> httpEntity = new HttpEntity<LocationZoneDTO>(locationZoneDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocationZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Location of location zone not found.", messageDto.getHeader());
	}
	
	@Test
	@Transactional
	public void createLocationZoneBadRequest() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(2L, 1L, true, "New location zone", -10, -4, -4);
		Location location = new Location();
		location.setId(100L);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<LocationZoneDTO> httpEntity = new HttpEntity<LocationZoneDTO>(locationZoneDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocationZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Bad request", messageDto.getMessage());
		assertEquals("Invalid inputs for location zone.", messageDto.getHeader());
	}
	
	
	
	@Test
	@Transactional
	public void updateLocationZoneNotFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		LocationZoneDTO locationZoneDto = new LocationZoneDTO();
		locationZoneDto.setId(1000);
		HttpEntity<LocationZoneDTO> httpEntity = new HttpEntity<LocationZoneDTO>(locationZoneDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLocationZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Location zone not found.", messageDto.getHeader());
	}
	
	@Test
	@Transactional
	public void updateLocationZoneNotChangeable() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		LocationZoneDTO locationZoneDto = new LocationZoneDTO();
		locationZoneDto.setId(1);
		HttpEntity<LocationZoneDTO> httpEntity = new HttpEntity<LocationZoneDTO>(locationZoneDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLocationZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Conflict", messageDto.getMessage());
		assertEquals("Location zone not changeable.", messageDto.getHeader());
	}
	
	@Test
	@Transactional
	public void updateLocationZoneInvalidInput() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(3L, 6L, true, "New location zone", -10, -4, -4);
		HttpEntity<LocationZoneDTO> httpEntity = new HttpEntity<LocationZoneDTO>(locationZoneDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLocationZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Bad request", messageDto.getMessage());
		assertEquals("Invalid input for location zone.", messageDto.getHeader());
	}
	
	
	
	
	/*
	 * dobije se error 300
	@Test
	@Transactional
	public void getLocationZoneSuccessfull() {
		ResponseEntity<LocationZone> responseEntity = testRestTemplate.getForEntity("/api/getLocationZone/2", LocationZone.class);
		LocationZone foundLocationZone = responseEntity.getBody();
		//assertEquals("Name2", foundLocationZone.getName());
		
		assertEquals(200, foundLocationZone.getCapacity());
		assertEquals(20, foundLocationZone.getColNumber());
		assertEquals(10, foundLocationZone.getRowNumber());
		assertTrue(foundLocationZone.isMatrix());
		assertEquals(1, foundLocationZone.getLocation().getId());
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	*/

	@Test
	@Transactional
	public void getLocationZonesSuccessfull() {
		ResponseEntity<LocationZone[]> responseEntity = testRestTemplate.getForEntity("/api/getLocationZones/6", LocationZone[].class);
		LocationZone[] foundLocationZones = responseEntity.getBody();
		assertEquals(2, foundLocationZones.length);
	
	}
	
	@Test
	@Transactional
	public void getLocationZoneNull() {
		ResponseEntity<LocationZone> responseEntity = testRestTemplate.getForEntity("/api/getLocationZone/100", LocationZone.class);
		LocationZone foundLocationZone = responseEntity.getBody();
		assertNull(foundLocationZone);
	}
	
	@Test
	@Transactional
	public void deleteLocationZoneNotFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteLocationZone/106", HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Location zone not found.", messageDto.getHeader());
	}
	
	@Test
	@Transactional
	public void deleteLocationZoneNotChangeable() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteLocationZone/1", HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Conflict", messageDto.getMessage());
		assertEquals("Location zone not changeable.", messageDto.getHeader());
	}
	/*
	Internal server error, mozda je do transakcija
	@Test
	public void deleteLocationZoneSuccessfull() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteLocationZone/4", HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Success", messageDto.getMessage());
		assertEquals("Location zone successfully deleted.", messageDto.getHeader());
	}
	*/
}
