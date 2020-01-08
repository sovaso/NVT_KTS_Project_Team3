package com.nvt.kts.team3.controllerTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import com.nvt.kts.team3.dto.EventDTO;
import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.LeasedZone;
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
public class LeasedZoneControllerIntegrationTest {

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
	
	private static final long NONEXISTENT_ID = 10000L;
	private static final long INACTIVE_EVENT_MAINTENANCE_ID = 6L;
	private static final long VALID_MAINTENANCE_ID = 1L;
	private static final long INVALID_LOCATION_ZONE_ID = 2L;
	
	private static final double VALID_PRICE = 30;
	private static final double INVALID_HIGH_PRICE = 11000;
	private static final double INVALID_LOW_PRICE = 0;
	
	@Test
	public void test_create_Unauthorized(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLeasedZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}
	
	@Test
	public void test_create_MaintenanceNotFound(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(NONEXISTENT_ID);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLeasedZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Maintenance Not Found", responseEntity.getBody().getMessage());
		assertEquals("Maintenance with this ID does not exist.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_create_EventNotActive(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(INACTIVE_EVENT_MAINTENANCE_ID);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLeasedZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Event not active", responseEntity.getBody().getMessage());
		assertEquals("Event with this ID is no more active.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_create_LocationZoneNotFound(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(VALID_MAINTENANCE_ID);
		request.setZoneId(NONEXISTENT_ID);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLeasedZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Location Zone Not Found", responseEntity.getBody().getMessage());
		assertEquals("Location zone with this ID does not exist.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_create_LocationZoneNotAvailable(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(VALID_MAINTENANCE_ID);
		request.setZoneId(INVALID_LOCATION_ZONE_ID);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLeasedZone", HttpMethod.POST,
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
		
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(VALID_MAINTENANCE_ID);
		request.setZoneId(1L);
		request.setPrice(INVALID_HIGH_PRICE);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLeasedZone", HttpMethod.POST,
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
		
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(VALID_MAINTENANCE_ID);
		request.setZoneId(1L);
		request.setPrice(INVALID_LOW_PRICE);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createLeasedZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Invalid Price", responseEntity.getBody().getMessage());
		assertEquals("Please set a price of ticket that is between 1$ and 10000$.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_Unauthorized(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLeasedZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}
	
	@Test
	public void test_update_LeasedZoneNotFound(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(NONEXISTENT_ID);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLeasedZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Leased Zone Not Found", responseEntity.getBody().getMessage());
		assertEquals("Leased zone with this ID does not exist.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_EventNotActive(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(9L);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLeasedZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Event not active", responseEntity.getBody().getMessage());
		assertEquals("Event with this ID is no more active.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_LocationZoneNotFound(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(1L);
		request.setZoneId(NONEXISTENT_ID);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLeasedZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Location Zone Not Found", responseEntity.getBody().getMessage());
		assertEquals("Location zone with this ID does not exist.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_LocationZoneNotAvailable(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(1L);
		request.setZoneId(2L);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLeasedZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Location Zone Not Available", responseEntity.getBody().getMessage());
		assertEquals("Please make sure to choose location zone that exists and belongs to choosen location.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_LeasedZoneNotChangeable(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(1L);
		request.setZoneId(1L);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLeasedZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Leased Zone Not Changeable", responseEntity.getBody().getMessage());
		assertEquals("There are reserved tickets for this zone, so, it can not be changed.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_update_InvalidPrice_1(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(8L);
		request.setZoneId(8L);
		request.setPrice(INVALID_HIGH_PRICE);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLeasedZone", HttpMethod.POST,
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
		
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(8L);
		request.setZoneId(8L);
		request.setPrice(INVALID_LOW_PRICE);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/updateLeasedZone", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Invalid Price", responseEntity.getBody().getMessage());
		assertEquals("Please set a price of ticket that is between 1$ and 10000$.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_remove_Unauthorized(){
		LeasedZoneDTO request = new LeasedZoneDTO();
		
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(request, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteLeasedZone/"+1000L, HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	}
	
	@Test
	public void test_remove_LeasedZoneNotFound(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteLeasedZone/"+NONEXISTENT_ID, HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Leased Zone Not Found", responseEntity.getBody().getMessage());
		assertEquals("Leased zone with this ID does not exist.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_remove_EventNotActive(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteLeasedZone/"+9L, HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Event not active", responseEntity.getBody().getMessage());
		assertEquals("Event with this ID is no more active.", responseEntity.getBody().getHeader());
	}
	
	@Test
	public void test_remove_LeasedZoneNotChangeable(){
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<LeasedZoneDTO> httpEntity = new HttpEntity<LeasedZoneDTO>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteLeasedZone/"+1L, HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Leased Zone Not Changeable", responseEntity.getBody().getMessage());
		assertEquals("There are reserved tickets for this zone, so, it can not be changed.", responseEntity.getBody().getHeader());
	}
	
	@Test
	@Transactional
	public void test_getLeasedZone_NotFound(){
		ResponseEntity<LeasedZone> responseEntity = testRestTemplate.getForEntity("/api/getLeasedZone/"+NONEXISTENT_ID, LeasedZone.class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	@Transactional
	public void test_getLeasedZone_Success(){
		ResponseEntity<LeasedZone> responseEntity = testRestTemplate.getForEntity("/api/getLeasedZone/"+1L, LeasedZone.class);
		
		Optional<LeasedZone> leasedZone = leasedZoneRepository.findById(1L);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(leasedZone.get().getId(), responseEntity.getBody().getId());
		assertEquals(leasedZone.get().getMaintenance().getId(), responseEntity.getBody().getMaintenance().getId());
		assertEquals(leasedZone.get().getZone().getId(), responseEntity.getBody().getZone().getId());
	}
	
	@Test
	@Transactional
	public void test_getMaintenanceLeasedZones_MaintenanceNotFound(){
		ResponseEntity<LeasedZone[]> responseEntity = testRestTemplate.getForEntity("/api/getLeasedZones/"+NONEXISTENT_ID, LeasedZone[].class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	@Transactional
	public void test_getMaintenanceLeasedZones_Success(){
		ResponseEntity<LeasedZone[]> responseEntity = testRestTemplate.getForEntity("/api/getLeasedZones/"+1L, LeasedZone[].class);
		
		Optional<Maintenance> maintenance = maintenanceRepository.findById(1L);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(maintenance.get().getLeasedZones().size(), responseEntity.getBody().length);
		int i = 0;
		for(LeasedZone lz : maintenance.get().getLeasedZones()){
			assertEquals(lz.getId(), responseEntity.getBody()[i].getId());
		}
	}
	
	@Test
	@Transactional
	public void test_getEventLeasedZones_EventNotFound(){
		ResponseEntity<LeasedZone[]> responseEntity = testRestTemplate.getForEntity("/api/getEventLeasedZones/"+NONEXISTENT_ID, LeasedZone[].class);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	@Transactional
	public void test_getEventLeasedZones_Success(){
		ResponseEntity<LeasedZone[]> responseEntity = testRestTemplate.getForEntity("/api/getEventLeasedZones/"+1L, LeasedZone[].class);
		
		Optional<Event> event = eventRepository.findById(1L);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		
		List<Long> ids = new ArrayList<Long>();
		
		int leasedZonesNum = 0;
		for(Maintenance m : event.get().getMaintenances()){
			leasedZonesNum += m.getLeasedZones().size();
			for(LeasedZone lz : m.getLeasedZones()){
				ids.add(lz.getId());
			}
		}
		
		assertEquals(leasedZonesNum, responseEntity.getBody().length);
		for(int i = 0; i < responseEntity.getBody().length; i++){
			assertTrue(ids.contains(responseEntity.getBody()[i].getId()));
		}
	}
}
