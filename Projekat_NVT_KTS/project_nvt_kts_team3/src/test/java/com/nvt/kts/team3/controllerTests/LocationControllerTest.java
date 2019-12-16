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
public class LocationControllerTest {
	
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
	public void getLocation_successfull() {
		
		ResponseEntity<Location> responseEntity = testRestTemplate.getForEntity("/api/getLocation/1", Location.class);
		Location foundLocation = responseEntity.getBody();
		assertEquals("Address1", foundLocation.getAddress());
		assertEquals("Name1", foundLocation.getName());
		assertEquals("Description1", foundLocation.getDescription());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	
		
	}
	
	@Test
	@Transactional
	public void getLocation_null() {
		ResponseEntity<Location> responseEntity = testRestTemplate.getForEntity("/api/getLocation/100", Location.class);
		Location foundLocation = responseEntity.getBody();
		assertNull(foundLocation);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	@Transactional
	public void getLocation_notActive() {
		ResponseEntity<Location> responseEntity = testRestTemplate.getForEntity("/api/getLocation/3", Location.class);
		Location foundLocation = responseEntity.getBody();
		assertNull(foundLocation);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	//Obraditi slucaj i kada se vraca prazna lista
	@Test
	@Transactional
	public void getAllLocations_successfull() {
		ResponseEntity<Location[]> responseEntity = testRestTemplate.getForEntity("/api/getAllLocations", Location[].class);
		Location[] locations = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(7, locations.length);
	}
	
	@Test
	@Transactional
	public void getActiveLocations_successfull() {
		ResponseEntity<Location[]> responseEntity = testRestTemplate.getForEntity("/api/getActiveLocations", Location[].class);
		Location[] locations = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(5, locations.length);
	}
	
	/*
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
	*/
	
	@Test
	@Transactional
	public void createLocation_LocationAlreadyExist() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setId(1L);
		locationDto.setName("Name1");
		locationDto.setAddress("Address1");
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Bad request", messageDto.getMessage());
		assertEquals("Location with that name and address already exist.", messageDto.getHeader());
	}
	
	
	@Test
	@Transactional
	public void createLocation_unatuhorized() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setId(1L);
		locationDto.setName("Name1");
		locationDto.setAddress("Address1");
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+"wwwww");
		
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}
	
	@Test
	@Transactional
	public void createLocation_unatuhorized_noToken() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setId(1L);
		locationDto.setName("Name1");
		locationDto.setAddress("Address1");
		
		HttpHeaders headers = new HttpHeaders();
		
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}
	
	
	
	@Test
	@Transactional
	public void createLocation_locationZoneEmpty() {
		LocationDTO locationDto = new LocationDTO();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Conflict", messageDto.getMessage());
		assertEquals("Invalid location zone.", messageDto.getHeader());
		
	}
	
	@Test
	@Transactional
	public void createLocation_locationZoneNull() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setLocationZone(null);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Conflict", messageDto.getMessage());
		assertEquals("Invalid location zone.", messageDto.getHeader());
	}
	
	@Test
	@Transactional
	public void createLocation_invalidLocationZone() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setName("NewLocation");
		locationDto.setAddress("NewAddress");
		LocationZoneDTO lzDto = new LocationZoneDTO();
		lzDto.setMatrix(false);
		lzDto.setCapacity(-100);
		lzDto.setRow(-10);
		lzDto.setCol(-20);
		locationDto.getLocationZone().add(lzDto);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Conflict", messageDto.getMessage());
		assertEquals("Invalid location zone.", messageDto.getHeader());
	}
	
	//capacity > 0, matrix = true, col <  0, row < 0
	@Test
	@Transactional
	public void createLocation_invalidLocationZoneCaseTwo() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setName("NewLocation");
		locationDto.setAddress("NewAddress");
		LocationZoneDTO lzDto = new LocationZoneDTO();
		lzDto.setMatrix(true);
		lzDto.setCapacity(100);
		lzDto.setRow(-10);
		lzDto.setCol(-20);
		locationDto.getLocationZone().add(lzDto);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Conflict", messageDto.getMessage());
		assertEquals("Invalid location zone.", messageDto.getHeader());
		
	}
	
	//col > 0, row > 0, matrix = false, capacity < 0
	@Test
	@Transactional
	public void createLocation_invalidLocationZoneCaseThree() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setName("NewLocation");
		locationDto.setAddress("NewAddress");
		LocationZoneDTO lzDto = new LocationZoneDTO();
		lzDto.setMatrix(false);
		lzDto.setCapacity(-1100);
		lzDto.setRow(10);
		lzDto.setCol(20);
		locationDto.getLocationZone().add(lzDto);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Conflict", messageDto.getMessage());
		assertEquals("Invalid location zone.", messageDto.getHeader());
	}
	
	@Test
	@Transactional
	public void createLocationZone_invalidLocationZoneCaseFour() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setName("NewLocation");
		locationDto.setAddress("NewAddress");
		LocationZoneDTO lzDto = new LocationZoneDTO();
		lzDto.setMatrix(true);
		lzDto.setCapacity(1100);
		lzDto.setRow(-10);
		lzDto.setCol(20);
		locationDto.getLocationZone().add(lzDto);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Conflict", messageDto.getMessage());
		assertEquals("Invalid location zone.", messageDto.getHeader());
	}
	
	//matrix = true, columns > 0, rows < 0, capacity < 0
	@Test
	@Transactional
	public void createLocation_invalidLocationZoneCaseFive() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setName("NewLocation");
		locationDto.setAddress("NewAddress");
		LocationZoneDTO lzDto = new LocationZoneDTO();
		lzDto.setMatrix(true);
		lzDto.setCapacity(-1100);
		lzDto.setRow(-10);
		lzDto.setCol(20);
		locationDto.getLocationZone().add(lzDto);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Conflict", messageDto.getMessage());
		assertEquals("Invalid location zone.", messageDto.getHeader());
	}
	
	//matrix = true, columns < 0, rows > 0, capacity > 0
	@Test
	@Transactional
	public void createLocation_invalidLocationZoneCaseSix() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setName("NewLocation");
		locationDto.setAddress("NewAddress");
		LocationZoneDTO lzDto = new LocationZoneDTO();
		lzDto.setMatrix(true);
		lzDto.setCapacity(1100);
		lzDto.setRow(10);
		lzDto.setCol(-20);
		locationDto.getLocationZone().add(lzDto);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Conflict", messageDto.getMessage());
		assertEquals("Invalid location zone.", messageDto.getHeader());
	}
	
	//matrix = true, columns < 0, rows > 0, capacity < 0
	@Test
	@Transactional
	public void createLocation_invalidLocationZoneCaseSeven() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setName("NewLocation");
		locationDto.setAddress("NewAddress");
		LocationZoneDTO lzDto = new LocationZoneDTO();
		lzDto.setMatrix(true);
		lzDto.setCapacity(-1100);
		lzDto.setRow(10);
		lzDto.setCol(-20);
		locationDto.getLocationZone().add(lzDto);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Conflict", messageDto.getMessage());
		assertEquals("Invalid location zone.", messageDto.getHeader());
	}
	
	@Test
	@Transactional
	public void updateLocation_locationNull_locationNotFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		LocationDTO locationDto = new LocationDTO(10000, "NewLocationn", "NewAdress", "NewDescriptionn");
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Bad request", messageDto.getMessage());
		assertEquals("Location not found.", messageDto.getHeader());
	}

	
	@Test
	@Transactional
	public void updateLocation_locationNotActive_locationNotFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		LocationDTO locationDto = new LocationDTO(3, "NewLocationn", "NewAdress", "NewDescriptionn");
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Bad request", messageDto.getMessage());
		assertEquals("Location not found.", messageDto.getHeader());
	}
	
	@Test
	@Transactional
	public void updateLocation_locationExists() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		LocationDTO locationDto = new LocationDTO(1, "Name2", "Address2", "Description2");
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Location with submited name and address already exist.", messageDto.getHeader());
	}
	
	@Test
	@Transactional
	public void updateLocation_unauthorized() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+"wwww");
		LocationDTO locationDto = new LocationDTO(1, "NewLocationn", "NewAdress", "NewDescriptionn");
		//LocationZoneDTO locationZoneDto = new LocationZoneDTO(99, 99, true, "NewLocationZone", 20, 10, 200);
		//locationDto.getLocationZone().add(locationZoneDto);
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}
	
	@Test
	@Transactional
	public void updateLocation_unauthorized_noToken() {
		HttpHeaders headers = new HttpHeaders();
		LocationDTO locationDto = new LocationDTO(1, "NewLocationn", "NewAdress", "NewDescriptionn");
		//LocationZoneDTO locationZoneDto = new LocationZoneDTO(99, 99, true, "NewLocationZone", 20, 10, 200);
		//locationDto.getLocationZone().add(locationZoneDto);
		HttpEntity<LocationDTO> httpEntity = new HttpEntity<LocationDTO>(locationDto, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLocation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	
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
	
	/*
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
	*/
	
	@Test
	@Transactional
	public void deleteLocation_unauthorized() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+"wwww");
		//LocationDTO locationDto = new LocationDTO(1, "NewLocationn", "NewAdress", "NewDescriptionn");
		//LocationZoneDTO locationZoneDto = new LocationZoneDTO(99, 99, true, "NewLocationZone", 20, 10, 200);
		//locationDto.getLocationZone().add(locationZoneDto);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteLocation/6", HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}
	
	@Test
	@Transactional
	public void deleteLocation_unauthorized_noToken() {
		HttpHeaders headers = new HttpHeaders();
		//LocationDTO locationDto = new LocationDTO(1, "NewLocationn", "NewAdress", "NewDescriptionn");
		//LocationZoneDTO locationZoneDto = new LocationZoneDTO(99, 99, true, "NewLocationZone", 20, 10, 200);
		//locationDto.getLocationZone().add(locationZoneDto);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteLocation/6", HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}
	
	@Test
	@Transactional
	public void deleteLocation_locationNull_locationNotFound(){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteLocation/100", HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Location not found.", messageDto.getHeader());
	}
	
	@Test
	@Transactional
	public void deleteLocation_locationNull_locationNotActive(){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteLocation/3", HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Location not found.", messageDto.getHeader());
	}
	
	@Test
	@Transactional
	public void deleteLocation_locationNotChangeable(){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteLocation/1", HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Conflict", messageDto.getMessage());
		assertEquals("Location not changeable.", messageDto.getHeader());
	}

}