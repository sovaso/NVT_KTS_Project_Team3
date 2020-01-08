package com.nvt.kts.team3.controllerTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;

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

import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.MaintenanceDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.LeasedZoneRepository;
import com.nvt.kts.team3.repository.LocationZoneRepository;
import com.nvt.kts.team3.repository.MaintenanceRepository;
import com.nvt.kts.team3.repository.TicketRepository;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MaintenanceControllerIntegrationTest {
	
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
	
	//private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	//private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	private static final long NONEXISTENT_EVENT_ID = 10000L;
	private static final long INACTIVE_EVENT_ID = 4L;
	private static final long INACTIVE_LOCATION_EVENT_ID = 10L;
	private static final long VALID_EVENT_ID = 1L;
	private static final long LOCATION_ZONE_ID = 1L;
	private static final long INVALID_LOCATION_ZONE_ID = 2L;
	private static final long ID = 0L;
	
	private static final String VALID_START_DATE = "2021-01-01 22:00";
	private static final String VALID_END_DATE = "2021-01-01 23:00";
	private static final String VALID_START_DATE_2 = "2022-01-01 22:00";
	private static final String VALID_END_DATE_2 = "2022-01-01 23:00";
	private static final String INVALID_START_DATE = "2021-01-15 21:00";
	private static final String INVALID_END_DATE = "2021-01-15 23:00";
	//private static final String VALID_EXPIRY_DATE = "2020-12-12 23:00";
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
	
	@Before
	public void logIn() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
	}
	
	@Test
	public void test_createMaintenance_EventNotFound() {
		MaintenanceDTO request = new MaintenanceDTO();
		request.setEventId(NONEXISTENT_EVENT_ID);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Event Not Found", responseEntity.getBody().getMessage());
		assertEquals("Event with this ID does not exist.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_createMaintenance_EventNotChangeable() {
		MaintenanceDTO request = new MaintenanceDTO();
		request.setEventId(INACTIVE_EVENT_ID);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Event Not Changeable", responseEntity.getBody().getMessage());
		assertEquals("Event is not active, so, it could not be updated.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_createMaintenance_LocationNotFound() {
		MaintenanceDTO request = new MaintenanceDTO();
		request.setEventId(INACTIVE_LOCATION_EVENT_ID);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Location Not Found", responseEntity.getBody().getMessage());
		assertEquals("Choosen location does not exist or is not active.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_createMaintenance_InvalidDate_1() {
		MaintenanceDTO request = new MaintenanceDTO(
				EXPIERED_START_DATE,
				EXPIERED_END_DATE,
				ID,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_createMaintenance_InvalidDate_2() {
		MaintenanceDTO request = new MaintenanceDTO(
				MAINTENANCE_25H_START,
				MAINTENANCE_25H_END,
				ID,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_createMaintenance_InvalidDate_3() {
		MaintenanceDTO request = new MaintenanceDTO(
				MAINTENANCE_30MIN_START,
				MAINTENANCE_30MIN_END,
				ID,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_createMaintenance_InvalidDate_4() {
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_END_DATE,
				VALID_START_DATE,
				ID,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_createMaintenance_InvalidDate_5() {
		MaintenanceDTO request = new MaintenanceDTO(
				BAD_FORMAT_START_DATE,
				BAD_FORMAT_END_DATE,
				ID,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Bad date format", responseEntity.getBody().getMessage());
		assertEquals("Please make sure that your date format is: yyyy-MM-dd HH:mm.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_createMaintenance_LocationNotAvailable() {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				VALID_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				INVALID_START_DATE,
				INVALID_END_DATE,
				ID,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Location Not Available", responseEntity.getBody().getMessage());
		assertEquals("Location is not available for specified period.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_createMaintenance_InvalidLocationZone() {
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				VALID_EVENT_ID,
				null);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Location Zone", responseEntity.getBody().getMessage());
		assertEquals("Arguments for location zone are not valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_createMaintenance_LocationZoneNotAvailable() {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				INVALID_LOCATION_ZONE_ID,
				ID,
				VALID_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				VALID_EVENT_ID,
				leasedZones);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Location Zone Not Available", responseEntity.getBody().getMessage());
		assertEquals("Please make sure to choose location zone that exists and belongs to choosen location.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_createMaintenance_InvalidPrice_1() {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				INVALID_HIGH_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				VALID_EVENT_ID,
				leasedZones);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Invalid Price", responseEntity.getBody().getMessage());
		assertEquals("Please set a price of ticket that is between 1$ and 10000$.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_createMaintenance_InvalidPrice_2() {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				INVALID_LOW_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				VALID_EVENT_ID,
				leasedZones);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Invalid Price", responseEntity.getBody().getMessage());
		assertEquals("Please set a price of ticket that is between 1$ and 10000$.", responseEntity.getBody().getHeader());
	}
	
	/*
	@Test
	@Transactional
	public void test_createMaintenance_Success() throws ParseException {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				VALID_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE_2,
				VALID_END_DATE_2,
				ID,
				VALID_EVENT_ID,
				leasedZones);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals("Success", responseEntity.getBody().getMessage());
		assertEquals("Maintenance successfully created.", responseEntity.getBody().getHeader());
		
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE_2)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE_2)),df);
		
		ArrayList<Maintenance> savedMaintenance = maintenanceRepository.getMaintenancesForDate(1L, startDate, endDate);
		assertEquals(savedMaintenance.size(), 1);
		
		Maintenance m = savedMaintenance.get(0);
		assertEquals(startDate, m.getMaintenanceDate());
		assertEquals(endDate, m.getMaintenanceEndTime());
		assertEquals(m.getEvent().getId(), VALID_EVENT_ID);
		
		Iterator<LeasedZone> iterator = m.getLeasedZones().iterator();
		LeasedZone savedZone = iterator.next();
		assertEquals(LOCATION_ZONE_ID, savedZone.getZone().getId());
		assertEquals(savedZone.getTickets().size(), savedZone.getZone().getCapacity());
		
		test_remove_Success(m.getId());
	}
	*/
	
	@Test
	public void test_updateMaintenance_MaintenanceNotFound() {
		MaintenanceDTO request = new MaintenanceDTO();
		request.setId(NONEXISTENT_EVENT_ID);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Maintenance Not Found", responseEntity.getBody().getMessage());
		assertEquals("Maintenance with this ID does not exist.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_updateMaintenance_EventNotChangeable() {
		MaintenanceDTO request = new MaintenanceDTO();
		request.setId(6L);
		request.setEventId(INACTIVE_EVENT_ID);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Event Not Changeable", responseEntity.getBody().getMessage());
		assertEquals("Event is not active, so, it could not be updated.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_updateMaintenance_LocationNotFound() {
		MaintenanceDTO request = new MaintenanceDTO();
		request.setId(14L);
		request.setEventId(INACTIVE_LOCATION_EVENT_ID);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Location Not Found", responseEntity.getBody().getMessage());
		assertEquals("Choosen location does not exist or is not active.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_updateMaintenance_InvalidDate_1() {
		MaintenanceDTO request = new MaintenanceDTO(
				EXPIERED_START_DATE,
				EXPIERED_END_DATE,
				1L,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_updateMaintenance_InvalidDate_2() {
		MaintenanceDTO request = new MaintenanceDTO(
				MAINTENANCE_25H_START,
				MAINTENANCE_25H_END,
				1L,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_updateMaintenance_InvalidDate_3() {
		MaintenanceDTO request = new MaintenanceDTO(
				MAINTENANCE_30MIN_START,
				MAINTENANCE_30MIN_END,
				1L,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_updateMaintenance_InvalidDate_4() {
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_END_DATE,
				VALID_START_DATE,
				1L,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Date", responseEntity.getBody().getMessage());
		assertEquals("Event must be created at least 7 days before maintenance and date must be valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_updateMaintenance_InvalidDate_5() {
		MaintenanceDTO request = new MaintenanceDTO(
				BAD_FORMAT_START_DATE,
				BAD_FORMAT_END_DATE,
				1L,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Bad date format", responseEntity.getBody().getMessage());
		assertEquals("Please make sure that your date format is: yyyy-MM-dd HH:mm.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_updateMaintenance_MaintenanceNotChangeable() {
		MaintenanceDTO request = new MaintenanceDTO(
				INVALID_START_DATE,
				INVALID_END_DATE,
				1L,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Maintenance Not Changeable", responseEntity.getBody().getMessage());
		assertEquals("Choosen maintenance could not be updated because there are reserved tickets for it.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_updateMaintenance_LocationNotAvailable() {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				VALID_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				INVALID_START_DATE,
				INVALID_END_DATE,
				16L,
				VALID_EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Location Not Available", responseEntity.getBody().getMessage());
		assertEquals("Location is not available for specified period.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_updateMaintenance_InvalidLocationZone() {
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				16L,
				VALID_EVENT_ID,
				null);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Location Zone", responseEntity.getBody().getMessage());
		assertEquals("Arguments for location zone are not valid.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_updateMaintenance_LocationZoneNotAvailable() {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				INVALID_LOCATION_ZONE_ID,
				ID,
				VALID_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				16L,
				VALID_EVENT_ID,
				leasedZones);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Location Zone Not Available", responseEntity.getBody().getMessage());
		assertEquals("Please make sure to choose location zone that exists and belongs to choosen location.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_updateMaintenance_InvalidPrice_1() {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				INVALID_HIGH_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				16L,
				VALID_EVENT_ID,
				leasedZones);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Invalid Price", responseEntity.getBody().getMessage());
		assertEquals("Please set a price of ticket that is between 1$ and 10000$.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_updateMaintenance_InvalidPrice_2() {
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				INVALID_LOW_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				16L,
				VALID_EVENT_ID,
				leasedZones);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<MaintenanceDTO> httpEntity = new HttpEntity<MaintenanceDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateMaintenance", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Invalid Price", responseEntity.getBody().getMessage());
		assertEquals("Please set a price of ticket that is between 1$ and 10000$.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_remove_MaintenanceNotFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteMaintenance/"+NONEXISTENT_EVENT_ID, HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Maintenance Not Found", responseEntity.getBody().getMessage());
		assertEquals("Maintenance with this ID does not exist.", responseEntity.getBody().getHeader());
		
	}
	
	@Test
	public void test_remove_EventNotActive() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteMaintenance/"+6L, HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Event not active", responseEntity.getBody().getMessage());
		assertEquals("Event with this ID is no more active.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_remove_MaintenanceNotChangeable() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteMaintenance/"+1L, HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Maintenance Not Changeable", responseEntity.getBody().getMessage());
		assertEquals("Choosen maintenance could not be updated because there are reserved tickets for it.", responseEntity.getBody().getHeader());
	}
	
	/*
	@Test
	public void test_remove_Success(long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteMaintenance/"+id, HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Success", responseEntity.getBody().getMessage());
		assertEquals("Maintenance successfully deleted.", responseEntity.getBody().getHeader());
	}
	*/
	
	@Test
	public void test_getMaintenance_MaintenanceNotFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/getMaintenance/"+1000L, HttpMethod.GET,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}
	
	@Test
	public void test_getMaintenance_Success() {
		ResponseEntity<Maintenance> responseEntity = testRestTemplate.getForEntity("/api/getMaintenance/"+1L, Maintenance.class);
		
		Maintenance maintenance = maintenanceRepository.getOne(1L);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(maintenance.getId(), responseEntity.getBody().getId());
	}
	
	@Test
	@Transactional
	public void test_getMaintenances_Success() {
		ResponseEntity<Maintenance[]> responseEntity = testRestTemplate.getForEntity("/api/getMaintenances/"+1L, Maintenance[].class);
		
		Event event = eventRepository.getOne(1L);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(event.getMaintenances().size(), responseEntity.getBody().length);
	}
	
	@Test
	@Transactional
	public void test_getMaintenances_EventNotFound() {
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/getMaintenances/"+NONEXISTENT_EVENT_ID, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Event Not Found", responseEntity.getBody().getMessage());
		assertEquals("Event with this ID does not exist.", responseEntity.getBody().getHeader());
	}
}
