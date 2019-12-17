package com.nvt.kts.team3.controllerTests;

import static org.junit.Assert.assertEquals;

import java.util.List;

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

import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class TicketControllerIntegrationTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;

	private String token;
	

	
	
	@Test
	public void test_getEventTickets_eventExists() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getEventTickets/2",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(6,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	
	@Test
	public void test_getEventTickets_eventDoesntExist() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getEventTickets/100",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(0,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	
	@Test
	public void test_getEventReservedTickets_eventExists() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getEventReservedTickets/2",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(4,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	
	@Test
	public void test_getEventReservedTickets_eventDoesntExist() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getEventReservedTickets/100",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(0,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	
	@Test
	public void test_getEventSoldTickets_eventExists() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getEventSoldTickets/2",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(1,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	@Test
	public void test_getEventSoldTickets_eventDoesntExists() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getEventSoldTickets/100",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(0,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	
	@Test
	public void test_getMaintenanceReservedTickets_maintenanceExists() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getMaintenanceReservedTickets/1",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(15,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	@Test
	public void test_getMaintenanceReservedTickets_maintenanceDoesntExist() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getMaintenanceReservedTickets/100",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(0,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	
	@Test
	public void test_getMaintenanceSoldTickets_maintenanceExists() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getMaintenanceSoldTickets/1",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(1,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	
	@Test
	public void test_getMaintenanceSoldTickets_maintenanceDoesntExist() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getMaintenanceSoldTickets/100",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(0,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	
	@Test
	public void test_getMaintenanceTickets_maintenanceExists() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getMaintenanceTickets/1",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(18,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	@Test
	public void test_getMaintenanceTickets_maintenanceDoesntExist() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getMaintenanceTickets/100",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(0,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	@Test
	public void test_getLeasedZoneTickets_leasedZoneExists() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getLeasedZoneTickets/1",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(18,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	
	@Test
	public void test_getLeasedZoneTickets_leasedZoneDoesntExist() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getLeasedZoneTickets/100",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(0,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	
	@Test
	public void test_getLeasedZoneReservedTickets_leasedZoneExists() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getLeasedZoneReservedTickets/1",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(15,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	@Test
	public void test_getLeasedZoneReservedTickets_leasedZoneDoesntExist() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getLeasedZoneReservedTickets/100",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(0,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	
	@Test
	public void test_getLeasedZoneSoldTickets_leasedZoneExists() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getLeasedZoneSoldTickets/1",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(1,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	
	@Test
	public void test_getLeasedZoneSoldTickets_leasedZoneDoesntExists() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getLeasedZoneSoldTickets/100",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(0,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	@Test
	public void test_deleteByZoneId_zoneExist() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/deleteByZoneId/6",
              HttpMethod.DELETE, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(2,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	@Test
	public void test_deleteByZoneId_zoneDoesntExist() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/deleteByZoneId/100",
              HttpMethod.DELETE, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(0,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
	}
	
	@Test
	public void test_getExpiredUnpaidTickets() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getExpiredUnpaidTickets",
              HttpMethod.GET, httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Ticket> tickets=(List<Ticket>) responseEntity.getBody();
		assertEquals(0,tickets.size());
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
		
	}
	
	

}
