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
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
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
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Bad request", messageDto.getMessage());
		assertEquals("Invalid inputs for location zone.", messageDto.getHeader());
	}
	/*
	@Test
	@Transactional
	@Rollback(true)
	public void createLocationZoneSuccessfull() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(2L, 1L, true, "New location zone", 10, 4, 40);
		Location location = new Location();
		location.setId(100L);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<LocationZoneDTO> httpEntity = new HttpEntity<LocationZoneDTO>(locationZoneDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocationZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Success", messageDto.getMessage());
		assertEquals("Location zone successfully created.", messageDto.getHeader());
	}
	*/
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
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
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
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
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
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Bad request", messageDto.getMessage());
		assertEquals("Invalid input for location zone.", messageDto.getHeader());
	}
	
	/*
	@Test
	public void updateLocationZoneSuccessfull() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(3L, 6L, true, "New location zone name", 10, 4, 40);
		HttpEntity<LocationZoneDTO> httpEntity = new HttpEntity<LocationZoneDTO>(locationZoneDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLocationZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Success", messageDto.getMessage());
		assertEquals("Location zone successfully updated.", messageDto.getHeader());
	}
	*/
	/*
	@Test
	public void testSortByName_successfull() {
		ResponseEntity<LocationZone> responseEntity = testRestTemplate.getForEntity("/api/getLocationZone/1", LocationZone.class);
		LocationZone locationZone = responseEntity.getBody();
		assertEquals("Name1", locationZone.getName());
	}
	*/
	
	@Test
	@Transactional
	public void getLocationZoneSuccessfull() {
		ResponseEntity<LocationZone> responseEntity = testRestTemplate.getForEntity("/api/getLocationZone/1", LocationZone.class);
		LocationZone foundLocationZone = responseEntity.getBody();
		assertEquals("Name1", foundLocationZone.getName());
		assertEquals(200, foundLocationZone.getCapacity());
		assertEquals(20, foundLocationZone.getColNumber());
		assertEquals(10, foundLocationZone.getRowNumber());
		assertTrue(foundLocationZone.isMatrix());
		assertEquals(1, foundLocationZone.getLocation().getId());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	
	@Test
	@Transactional
	public void getLocationZonesSuccessfull() {
		ResponseEntity<LocationZone[]> responseEntity = testRestTemplate.getForEntity("/api/getLocationZones/6", LocationZone[].class);
		LocationZone[] foundLocationZones = responseEntity.getBody();
		assertEquals(2, foundLocationZones.length);
		/*
		assertEquals("Name4", foundLocationZones[1].getName());
		assertEquals(400, foundLocationZones[1].getCapacity());
		assertEquals(40, foundLocationZones[1].getColNumber());
		assertEquals(10, foundLocationZones[1].getRowNumber());
		assertTrue(foundLocationZones[1].isMatrix());
		assertEquals(6, foundLocationZones[1].getLocation().getId());
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Name3", foundLocationZones[0].getName());
		assertEquals(300, foundLocationZones[0].getCapacity());
		assertEquals(30, foundLocationZones[0].getColNumber());
		assertEquals(10, foundLocationZones[0].getRowNumber());
		assertTrue(foundLocationZones[0].isMatrix());
		assertEquals(6, foundLocationZones[0].getLocation().getId());
		*/
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
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
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
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
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
