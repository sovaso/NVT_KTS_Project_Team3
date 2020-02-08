package com.nvt.kts.team3.controllerTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

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

import com.nvt.kts.team3.dto.EventDTO;
import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.MaintenanceDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.LeasedZoneRepository;
import com.nvt.kts.team3.repository.LocationZoneRepository;
import com.nvt.kts.team3.repository.MaintenanceRepository;
import com.nvt.kts.team3.repository.TicketRepository;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EventControllerIntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	public MaintenanceRepository maintenanceRepository;
	
	@Autowired
	public LocationZoneRepository locationZoneRepository;
	
	@Autowired
	public TicketRepository ticketRepository;
	
	@Autowired
	public EventRepository eventRepository;
	
	@Autowired
	public LeasedZoneRepository leasedZoneRepository;
	
	private String token;
	
	private static final long NONEXISTENT_EVENT_ID = 10000L;
	private static final long INACTIVE_EVENT_ID = 4L;
	private static final long INACTIVE_LOCATION_ID = 3L;
	private static final long LOCATION_ID = 1L;
	private static final long VALID_EVENT_ID = 1L;
	private static final long LOCATION_ZONE_ID = 1L;
	private static final long INVALID_LOCATION_ZONE_ID = 2L;
	private static final long INACTIVE_LOCATION_EVENT_ID = 10L;
	private static final long ID = 0L;
	
	private static final String VALID_START_DATE = "2021-01-01 22:00";
	private static final String VALID_END_DATE = "2021-01-01 23:00";
	private static final String NOT_AVAILABLE_START_DATE = "2021-01-25 21:00";
	private static final String NOT_AVAILABLE_END_DATE = "2021-01-25 23:00";
	private static final String NOT_AVAILABLE_START_DATE_2 = "2021-01-15 21:00";
	private static final String NOT_AVAILABLE_END_DATE_2 = "2021-01-15 23:00";
	private static final String EXPIERED_START_DATE = "2018-01-01 22:00";
	private static final String EXPIERED_END_DATE = "2018-01-01 23:00";
	private static final String MAINTENANCE_25H_START = "2021-01-01 22:00";
	private static final String MAINTENANCE_25H_END = "2021-01-02 23:00";
	private static final String MAINTENANCE_30MIN_START = "2021-01-01 22:00";
	private static final String MAINTENANCE_30MIN_END = "2021-01-01 22:25";
	private static final String BAD_FORMAT_START_DATE = "2021.01.01, 22:00";
	private static final String BAD_FORMAT_END_DATE = "2021.01.01, 23:00";
	
	private static final double VALID_PRICE = 30;
	private static final double INVALID_HIGH_PRICE = 11000;
	private static final double INVALID_LOW_PRICE = 0;
	
	private static final String INVALID_EVENT_TYPE = "UNEXPECTED_TYPE";
	private static final String VALID_EVENT_TYPE = "SPORTS";
	
	
	
	@Test
	public void test_create_Unauthorized(){
		EventDTO request = new EventDTO();
		
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}

	@Test
	public void test_create_InvalidEventType(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		EventDTO request = new EventDTO();
		request.setEventType(INVALID_EVENT_TYPE);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Event Type", responseEntity.getBody().getMessage());
		assertEquals("Choosen event type does not exist. Please choose: SPORTS, CULTURAL or ENTERTAINMENT event type.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_create_LocationNotFound(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		EventDTO request = new EventDTO();
		request.setEventType(VALID_EVENT_TYPE);
		request.setId(INACTIVE_LOCATION_ID);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Location Not Found", responseEntity.getBody().getMessage());
		assertEquals("Choosen location does not exist or is not active.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_create_InvalidDate_1(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				MAINTENANCE_30MIN_START,
				MAINTENANCE_30MIN_END,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_create_InvalidDate_2(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				MAINTENANCE_25H_START,
				MAINTENANCE_25H_END,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_create_InvalidDate_3(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_END_DATE,
				VALID_START_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_create_InvalidDate_4(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				EXPIERED_START_DATE,
				EXPIERED_END_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_create_PaseException(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				BAD_FORMAT_START_DATE,
				BAD_FORMAT_END_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Bad date format", responseEntity.getBody().getMessage());
		assertEquals("Please make sure that your date format is: yyyy-MM-dd HH:mm.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_create_LocationNotAvailable(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				NOT_AVAILABLE_START_DATE,
				NOT_AVAILABLE_END_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Location Not Available", responseEntity.getBody().getMessage());
		assertEquals("Location is not available for specified period.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_create_InvalidLocationZone(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Location Zone", responseEntity.getBody().getMessage());
		assertEquals("Arguments for location zone are not valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_create_LocationZoneNotAvailable(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				INVALID_LOCATION_ZONE_ID,
				ID,
				VALID_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				LOCATION_ID);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Location Zone Not Available", responseEntity.getBody().getMessage());
		assertEquals("Please make sure to choose location zone that exists and belongs to choosen location.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_create_InvalidPrice_1(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				INVALID_LOW_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				LOCATION_ID);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Invalid Price", responseEntity.getBody().getMessage());
		assertEquals("Please set a price of ticket that is between 1$ and 10000$.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_create_InvalidPrice_2(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				INVALID_HIGH_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				LOCATION_ID);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Invalid Price", responseEntity.getBody().getMessage());
		assertEquals("Please set a price of ticket that is between 1$ and 10000$.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_Unauthorized(){
		EventDTO request = new EventDTO();
		
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}
	
	@Test
	public void test_update_EventNotFound(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		EventDTO request = new EventDTO();
		request.setId(NONEXISTENT_EVENT_ID);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Event Not Found", responseEntity.getBody().getMessage());
		assertEquals("Event with this ID does not exist.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_EventNotChangeable(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		EventDTO request = new EventDTO();
		request.setId(INACTIVE_EVENT_ID);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Event Not Changeable", responseEntity.getBody().getMessage());
		assertEquals("Event is not active, so, it could not be updated.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_InvalidEventType(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		EventDTO request = new EventDTO();
		request.setId(VALID_EVENT_ID);
		request.setEventType(INVALID_EVENT_TYPE);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Event Type", responseEntity.getBody().getMessage());
		assertEquals("Choosen event type does not exist. Please choose: SPORTS, CULTURAL or ENTERTAINMENT event type.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_LocationNotFound(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		EventDTO request = new EventDTO();
		request.setId(INACTIVE_LOCATION_EVENT_ID);
		request.setEventType(VALID_EVENT_TYPE);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Location Not Found", responseEntity.getBody().getMessage());
		assertEquals("Choosen location does not exist or is not active.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_LocationNotChangeable(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		EventDTO request = new EventDTO();
		request.setId(VALID_EVENT_ID);
		request.setEventType(VALID_EVENT_TYPE);
		request.setLocationId(2L);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Location Not Changeable", responseEntity.getBody().getMessage());
		assertEquals("Location could not be updated because there are reserved tickets for this location.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_InvalidDate_1(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_END_DATE,
				VALID_START_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				1L);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_InvalidDate_2(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				EXPIERED_START_DATE,
				EXPIERED_END_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				1L);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_InvalidDate_3(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				MAINTENANCE_25H_START,
				MAINTENANCE_25H_END,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				1L);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_InvalidDate_4(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				MAINTENANCE_30MIN_START,
				MAINTENANCE_30MIN_END,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				1L);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_ParseException(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				BAD_FORMAT_START_DATE,
				BAD_FORMAT_END_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				1L);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Bad date format", responseEntity.getBody().getMessage());
		assertEquals("Please make sure that your date format is: yyyy-MM-dd HH:mm.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_LocationNotAvailable(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				NOT_AVAILABLE_START_DATE_2,
				NOT_AVAILABLE_END_DATE_2,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				1L);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Location Not Available", responseEntity.getBody().getMessage());
		assertEquals("Location is not available for specified period.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_InvalidLocationZone(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				1L);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Location Zone", responseEntity.getBody().getMessage());
		assertEquals("Arguments for location zone are not valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_LocationZoneNotAvailable(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				2L,
				ID,
				VALID_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				1L);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Location Zone Not Available", responseEntity.getBody().getMessage());
		assertEquals("Please make sure to choose location zone that exists and belongs to choosen location.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_InvalidPrice_1(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				1L,
				ID,
				INVALID_HIGH_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				1L);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Invalid Price", responseEntity.getBody().getMessage());
		assertEquals("Please set a price of ticket that is between 1$ and 10000$.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_InvalidPrice_2(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				1L,
				ID,
				INVALID_LOW_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				1L);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateEvent", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Invalid Price", responseEntity.getBody().getMessage());
		assertEquals("Please set a price of ticket that is between 1$ and 10000$.", responseEntity.getBody().getHeader());
	}
	
	@Test
	@Transactional
	public void test_findById_Found(){
		ResponseEntity<Event> responseEntity = testRestTemplate.getForEntity("/api/getEvent/"+VALID_EVENT_ID, Event.class);
		
		Optional<Event> event = eventRepository.findById(VALID_EVENT_ID);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(event.get().getId(), responseEntity.getBody().getId());
		assertEquals(event.get().getName(), responseEntity.getBody().getName());
		assertEquals(event.get().getType(), responseEntity.getBody().getType());
	}
	
	@Test
	public void test_findById_NotFound(){
		ResponseEntity<Event> responseEntity = testRestTemplate.getForEntity("/api/getEvent/"+NONEXISTENT_EVENT_ID, Event.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	public void test_findAll(){
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/getAllEvents", Event[].class);
		
		List<Event> events = eventRepository.findAll();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(events.size(), responseEntity.getBody().length);
		
		for(int i = 0; i < responseEntity.getBody().length; i++){
			assertEquals(responseEntity.getBody()[i].getName(), events.get(i).getName());
		}
	}
	
	@Test
	public void test_findActiveEvents(){
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/getActiveEvents", Event[].class);
		
		List<Event> events = eventRepository.getActiveEvents();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(events.size(), responseEntity.getBody().length);
		
		for(int i = 0; i < responseEntity.getBody().length; i++){
			assertEquals(responseEntity.getBody()[i].getName(), events.get(i).getName());
		}
	}
	
	@Test
	public void test_getEventLocation_NotFound(){
		ResponseEntity<Location> responseEntity = testRestTemplate.getForEntity("/api/getEventLocation/"+NONEXISTENT_EVENT_ID, Location.class);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	public void test_getEventLocation_Found(){
		ResponseEntity<Location> responseEntity = testRestTemplate.getForEntity("/api/getEventLocation/"+VALID_EVENT_ID, Location.class);
		
		Optional<Event> event = eventRepository.findById(VALID_EVENT_ID);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(event.get().getLocationInfo().getId(), responseEntity.getBody().getId());
	}
	
	@Test
	public void test_deleteEvent_Unauthorized(){
		EventDTO request = new EventDTO();
		
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<EventDTO> httpEntity = new HttpEntity<EventDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteEvent/"+NONEXISTENT_EVENT_ID, HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}
	
	@Test
	public void test_deleteEvent_EventNotFound(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Long> httpEntity = new HttpEntity<Long>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteEvent/"+NONEXISTENT_EVENT_ID, HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Event Not Found", responseEntity.getBody().getMessage());
		assertEquals("Event with this ID does not exist.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_deleteEvent_EventNotChangeable(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Long> httpEntity = new HttpEntity<Long>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteEvent/"+1L, HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Event Not Changeable", responseEntity.getBody().getMessage());
		assertEquals("Event is not active, so, it could not be updated.", responseEntity.getBody().getHeader());
	}
}
