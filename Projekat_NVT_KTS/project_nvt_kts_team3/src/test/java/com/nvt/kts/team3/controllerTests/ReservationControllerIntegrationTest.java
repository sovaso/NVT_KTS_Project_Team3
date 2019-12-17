package com.nvt.kts.team3.controllerTests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.dto.ReservationDTO;
import com.nvt.kts.team3.dto.TicketDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class ReservationControllerIntegrationTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;

	private String token;
	
	

	
	
	@Test
	@Rollback(true)
	public void test_createReservation() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user6", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		Event e=new Event();
		e.setId(2L);
		TicketDTO t=new TicketDTO(24L);
		ArrayList<TicketDTO> tickets=new ArrayList<>();
		tickets.add(t);
		ReservationDTO reservationDTO=new ReservationDTO(e,tickets,"aaa");
		HttpEntity<ReservationDTO> httpEntity = new HttpEntity<ReservationDTO>(reservationDTO, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createReservation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals("Success", messageDto.getMessage());
		assertEquals("Reservation successfuly created!", messageDto.getHeader());
		
	}
	
	@Test
	@Rollback(true)
	public void test_createReservation_partlySuccess() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user6", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		Event e=new Event();
		e.setId(2L);
		TicketDTO t=new TicketDTO(1L);
		TicketDTO t2=new TicketDTO(2L);
		ArrayList<TicketDTO> tickets=new ArrayList<>();
		tickets.add(t);
		tickets.add(t2);
		ReservationDTO reservationDTO=new ReservationDTO(e,tickets,"aaa");
		HttpEntity<ReservationDTO> httpEntity = new HttpEntity<ReservationDTO>(reservationDTO, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createReservation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals("Partly success", messageDto.getMessage());
		assertEquals("Some tickets could not be reserved, but reservation is created!", messageDto.getHeader());	
	}
	
	@Test
	@Rollback(true)
	public void test_createReservation_eventNotFound() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user6", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		Event e=new Event();
		e.setId(100L);
		TicketDTO t=new TicketDTO(24L);
		ArrayList<TicketDTO> tickets=new ArrayList<>();
		tickets.add(t);
		ReservationDTO reservationDTO=new ReservationDTO(e,tickets,"aaa");
		HttpEntity<ReservationDTO> httpEntity = new HttpEntity<ReservationDTO>(reservationDTO, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createReservation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Event Not Found", messageDto.getMessage());
		assertEquals("Event with this ID does not exist.", messageDto.getHeader());
		
	}
	
	@Test
	@Rollback(true)
	public void test_createReservation_eventNotActive() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user6", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		Event e=new Event();
		e.setId(7L);
		TicketDTO t=new TicketDTO(24L);
		ArrayList<TicketDTO> tickets=new ArrayList<>();
		tickets.add(t);
		ReservationDTO reservationDTO=new ReservationDTO(e,tickets,"aaa");
		HttpEntity<ReservationDTO> httpEntity = new HttpEntity<ReservationDTO>(reservationDTO, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createReservation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Event not active", messageDto.getMessage());
		assertEquals("Event with this ID is no more active.", messageDto.getHeader());	
		
	}
	
	
	@Test
	@Rollback(true)
	public void test_createReservation_tooManyTicketsReserved() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user1", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		Event e=new Event();
		e.setId(2L);
		TicketDTO t=new TicketDTO(24L);
		ArrayList<TicketDTO> tickets=new ArrayList<>();
		tickets.add(t);
		ReservationDTO reservationDTO=new ReservationDTO(e,tickets,"aaa");
		HttpEntity<ReservationDTO> httpEntity = new HttpEntity<ReservationDTO>(reservationDTO, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createReservation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Too many tickets reserved", messageDto.getMessage());
		assertEquals("Cannot reserve more than 10 tickets for one event.", messageDto.getHeader());	
		
	}
	
	
	@Test
	@Rollback(true)
	public void test_createReservation_reservationCannotBeCreated() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user6", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		Event e=new Event();
		e.setId(2L);
		TicketDTO t=new TicketDTO(3L);
		ArrayList<TicketDTO> tickets=new ArrayList<>();
		tickets.add(t);
		ReservationDTO reservationDTO=new ReservationDTO(e,tickets,"aaa");
		HttpEntity<ReservationDTO> httpEntity = new HttpEntity<ReservationDTO>(reservationDTO, headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/createReservation", HttpMethod.POST,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Reservation cannot be created", messageDto.getMessage());
		assertEquals("None of the tickets you chose is available.", messageDto.getHeader());	
		
	}
	
	@Test
	@Rollback(true)
	public void test_payReservation_ok() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user1", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/payReservation/3", HttpMethod.PUT,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Success", messageDto.getMessage());
		assertEquals("Reservation successfuly paid!", messageDto.getHeader());	
	}
	
	@Test
	@Rollback(true)
	public void test_payReservation_reservationNotFound() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user1", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/payReservation/100", HttpMethod.PUT,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Reservation Not Found", messageDto.getMessage());
		assertEquals("Reservation with this ID does not exist.", messageDto.getHeader());	
	}
	
	
	@Test
	@Rollback(true)
	public void test_payReservation_notUserReservation() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user6", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/payReservation/1", HttpMethod.PUT,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
		assertEquals("Not user reservation", messageDto.getMessage());
		assertEquals("Reservation does not belong to logged user.", messageDto.getHeader());	
	}
	
	@Test
	@Rollback(true)
	public void test_payReservation_reservationAlreadyPaid() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user1", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/payReservation/2", HttpMethod.PUT,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Reservation is already paid", messageDto.getMessage());
		assertEquals("Reservation is already paid.", messageDto.getHeader());	
	}
	
	@Test
	@Rollback(true)
	public void test_payReservation_eventNotActive() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user4", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/payReservation/8", HttpMethod.PUT,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Event not active", messageDto.getMessage());
		assertEquals("Event with this ID is no more active.", messageDto.getHeader());	
	}
	
	@Test
	@Rollback(true)
	public void test_payReservation_reservationExpired() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user6", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/payReservation/6", HttpMethod.PUT,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Reservation expired", messageDto.getMessage());
		assertEquals("All the reserved tickets expired.", messageDto.getHeader());	
	}
	
	
	@Test
	@Rollback(true)
	public void test_deleteReservation_ok() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user6", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteReservation/4", HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Success", messageDto.getMessage());
		assertEquals("Reservation successfuly cancelled!", messageDto.getHeader());	
	}
	
	
	@Test
	@Rollback(true)
	public void test_deleteReservation_reservationAlreadyFound() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user6", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteReservation/2", HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Reservation is already paid", messageDto.getMessage());
		assertEquals("Reservation is already paid.", messageDto.getHeader());		
	}
	
	@Test
	@Rollback(true)
	public void test_deleteReservation_notUserReservation() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user6", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteReservation/1", HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
		assertEquals("Not user reservation", messageDto.getMessage());
		assertEquals("Reservation does not belong to logged user.", messageDto.getHeader());	
	}
	
	@Test
	@Rollback(true)
	public void test_deleteReservation_reservationCannotBeCancelled() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user6", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/deleteReservation/6", HttpMethod.DELETE,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Reservation cannot be cancelled", messageDto.getMessage());
		assertEquals("Some tickets in reservation could have expired.", messageDto.getHeader());
		
	}
	
	@Test
	public void test_getUserResrvations_ok() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user1", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getUserReservations/user1", HttpMethod.GET,
				httpEntity, Object.class);
		@SuppressWarnings("unchecked")
		List<Reservation> reservations= (List<Reservation>) responseEntity.getBody();
		assertEquals(3,reservations.size());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		
	}
	
	
	@Test
	public void test_getUserResrvations_userIsNull() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user1", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/getUserReservations/user100", HttpMethod.GET,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto=responseEntity.getBody();
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("No success", messageDto.getMessage());
		assertEquals("User with given id does not exist!", messageDto.getHeader());
		
	}
	
	
	@Test
	public void test_getReservation_ok() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Reservation> responseEntity = testRestTemplate.exchange("/api/getReservation/1", HttpMethod.GET,
				httpEntity, Reservation.class);
		Reservation res=responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(1, res.getUser().getId());
		assertEquals(200,res.getTotalPrice(),0);
		
	}
	
	
	@Test
	public void test_getReservation_reservationNotFound() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/getReservation/100", HttpMethod.GET,
				httpEntity, MessageDTO.class);
		MessageDTO messageDto=responseEntity.getBody();
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Reservation Not Found", messageDto.getMessage());
		assertEquals("Reservation with this ID does not exist.",messageDto.getHeader());
		
	}
	
	@Test
	public void test_getEventReservations_ok() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getEventReservations/3", HttpMethod.GET,
				httpEntity,Object.class);
		@SuppressWarnings("unchecked")
		List<Reservation> reservations= (List<Reservation>) responseEntity.getBody();
		assertEquals(3, reservations.size());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	
	
	@Test
	public void test_getEventReservations_eventNotFound() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/getEventReservations/100", HttpMethod.GET,
				httpEntity,MessageDTO.class);
		MessageDTO messageDto=responseEntity.getBody();
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Event Not Found", messageDto.getMessage());
		assertEquals("Event with this ID does not exist.", messageDto.getHeader());
	}
	
	@Test
	public void test_getLocationReservations() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> responseEntity = testRestTemplate.exchange("/api/getLocationReservations/5", HttpMethod.GET,
				httpEntity,Object.class);
		@SuppressWarnings("unchecked")
		List<Reservation> reservations=(List<Reservation>) responseEntity.getBody();
		assertEquals(1, reservations.size());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		
	}
	
	@Test
	public void test_locationNotFound() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user2", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/getLocationReservations/100", HttpMethod.GET,
				httpEntity,MessageDTO.class);
		MessageDTO messageDto=responseEntity.getBody();
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Location Not Found", messageDto.getMessage());
		assertEquals("Choosen location does not exist or is not active.", messageDto.getHeader());
		
	}
	
	@Test
	public void test_cancelTicket_ok() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user1", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/cancelTicket/12", HttpMethod.PUT,
				httpEntity,MessageDTO.class);
		MessageDTO messageDto=responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Success", messageDto.getMessage());
		assertEquals("Ticket successfuly cancelled!", messageDto.getHeader());
	}
	
	@Test
	public void test_cancelTicket_ticketNotFound() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user1", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/cancelTicket/100", HttpMethod.PUT,
				httpEntity,MessageDTO.class);
		MessageDTO messageDto=responseEntity.getBody();
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals("Ticket not found", messageDto.getMessage());
		assertEquals("Ticket with this ID does not exist.", messageDto.getHeader());
	}
	
	@Test
	public void test_cancelTicket_ticketNotReserved() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user1", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/cancelTicket/25", HttpMethod.PUT,
				httpEntity,MessageDTO.class);
		MessageDTO messageDto=responseEntity.getBody();
		//assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Ticket is not reserved", messageDto.getMessage());
		assertEquals("Ticket is not reserved.", messageDto.getHeader());
	}
	
	@Test
	public void test_cancelTicket_notUserReservation() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user6", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/cancelTicket/2", HttpMethod.PUT,
				httpEntity,MessageDTO.class);
		MessageDTO messageDto=responseEntity.getBody();
		assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
		assertEquals("Not user reservation", messageDto.getMessage());
		assertEquals("Reservation does not belong to logged user.", messageDto.getHeader());
	}
	
	@Test
	public void test_cancelTicket_ticketExpired() {
		ResponseEntity<UserTokenState> result =
				testRestTemplate.postForEntity("/auth/login", new JwtAuthenticationRequest("user6", "123"),
				UserTokenState.class);
		token = result.getBody().getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.exchange("/api/cancelTicket/21", HttpMethod.PUT,
				httpEntity,MessageDTO.class);
		MessageDTO messageDto=responseEntity.getBody();
		assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
		assertEquals("Ticket has expired", messageDto.getMessage());
		assertEquals("Unable to cancel ticket it has expired.", messageDto.getHeader());
	}
	

}
