package com.nvt.kts.team3.serviceTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.service.ReservationService;

import exception.EventNotActive;
import exception.EventNotFound;
import exception.LocationNotFound;
import exception.NoLoggedUser;
import exception.NotUserReservation;
import exception.ReservationAlreadyPaid;
import exception.ReservationCannotBeCancelled;
import exception.ReservationCannotBeCreated;
import exception.ReservationExpired;
import exception.ReservationNotFound;
import exception.TooManyTicketsReserved;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application.properties")
@Transactional
public class ReservationServiceIntegrationTest {
	
	@Autowired
	ReservationService reservationService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
//	@Before
//	public void login() {
//		Authentication authentication = Mockito.mock(Authentication.class);
//		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//				"user1", "123"));
//		// Mockito.whens() for your authorization object
//		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//		SecurityContextHolder.setContext(securityContext);
//		
//	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void test_create_ok() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user6", "123"));
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Event e=new Event();
		e.setId(2L);
		Ticket t=new Ticket();
		t.setId(1L);
		Set<Ticket> tickets=new HashSet<>();
		tickets.add(t);
		Reservation r=new Reservation(1L, new Date(), false, 0, null, tickets, e, "aaa");
		Reservation r2=reservationService.create(r);
	}
	
	@Test(expected=EventNotFound.class)
	public void test_create_eventNotFound() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user1", "123"));
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Event e=new Event();
		e.setId(400L);
		Ticket t=new Ticket();
		t.setId(5L);
		Set<Ticket> tickets=new HashSet<>();
		tickets.add(t);
		Reservation r=new Reservation(1L, new Date(), false, 0, null, tickets, e, "aaa");
		reservationService.create(r);
	}
	
	@Test(expected=EventNotActive.class)
	public void test_create_eventNotActive() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user1", "123"));
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Event e=new Event();
		e.setId(9L);
		Ticket t=new Ticket();
		t.setId(5L);
		Set<Ticket> tickets=new HashSet<>();
		tickets.add(t);
		Reservation r=new Reservation(1L, new Date(), false, 0, null, tickets, e, "aaa");
		reservationService.create(r);
	}
	
	@Test(expected=TooManyTicketsReserved.class)
	public void test_create_tooManyTicketsReserved() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user1", "123"));
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Event e=new Event();
		e.setId(2L);
		Ticket t=new Ticket();
		t.setId(5L);
		Set<Ticket> tickets=new HashSet<>();
		tickets.add(t);
		Reservation r=new Reservation(1L, new Date(), false, 0, null, tickets, e, "aaa");
		reservationService.create(r);
	}
	
	@Test(expected=ReservationCannotBeCreated.class)
	public void test_create_reservationCannotBeCreated() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user6", "123"));
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Event e=new Event();
		e.setId(2L);
		Ticket t=new Ticket();
		t.setId(2L);
		Set<Ticket> tickets=new HashSet<>();
		tickets.add(t);
		Reservation r=new Reservation(1L, new Date(), false, 0, null, tickets, e, "aaa");
		reservationService.create(r);
	}
	
	
	@Test
	@Transactional
	@Rollback(true)
	public void test_removeReservation_ok() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user6", "123"));
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		boolean success=reservationService.remove(4L);
		assertTrue(success);
	}
	
	@Test(expected=ReservationNotFound.class)
	public void test_reservationNotFound() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user6", "123"));
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		reservationService.remove(500L);
	}
	
	@Test(expected=ReservationAlreadyPaid.class)
	public void test_reservationAlreadyPaid() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user6", "123"));
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		reservationService.remove(5L);
	}
	
	@Test(expected=NotUserReservation.class)
	public void test_notUserReservation() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user6", "123"));
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		reservationService.remove(3L);
	}
	
	@Test(expected=ReservationCannotBeCancelled.class)
	public void test_reservationCannotBeCancelled() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user6", "123"));
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		reservationService.remove(6L);
	}
	

	@Test
	public void test_findByUser() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user1", "123"));
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		User user = (User) authentication.getPrincipal();
		List<Reservation> reservations=reservationService.findByUser((RegularUser) user);
		assertEquals(3,reservations.size());
		assertEquals(2,reservations.get(0).getEvent().getId());
		assertEquals(2,reservations.get(1).getEvent().getId());
		assertEquals(3,reservations.get(2).getEvent().getId());
		assertEquals(200,reservations.get(0).getTotalPrice(),0);
		assertEquals(200,reservations.get(1).getTotalPrice(),0);
		assertEquals(2600,reservations.get(2).getTotalPrice(),0);
		assertFalse(reservations.get(0).isPaid());
		assertTrue(reservations.get(1).isPaid());
		assertFalse(reservations.get(2).isPaid());
	}
	
	@Test
	public void test_findByEvent() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user6", "123"));
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		User user = (User) authentication.getPrincipal();
		List<Reservation> reservations=reservationService.findByUser((RegularUser) user);
		Event event=reservations.get(0).getEvent();
		List<Reservation> reservations2=reservationService.findByEvent(event);
		assertEquals(3,reservations2.size());
		assertEquals(2600,reservations2.get(0).getTotalPrice(),0);
		assertEquals(200,reservations2.get(1).getTotalPrice(),0);
		assertEquals(200,reservations2.get(2).getTotalPrice(),0);
	
	}
	
	
	@Test
	public void test_findByUserAndPaid() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user6", "123"));
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		User user = (User) authentication.getPrincipal();
		List<Reservation> reservations=reservationService.findByUserAndPaid((RegularUser) user,true);
		assertEquals(1,reservations.size());
		assertEquals(5,reservations.get(0).getId());
		assertEquals(200,reservations.get(0).getTotalPrice(),0);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void test_payReservation() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user1", "123"));
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		boolean success=reservationService.payReservation(3L);
		assertTrue(success);
		
	}
	
	@Test(expected=ReservationNotFound.class)
	public void test_payReservation_reservationNotFound() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user1", "123"));
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		reservationService.payReservation(500L);
	}
	
	@Test(expected=NotUserReservation.class)
	public void test_payReservation_notUserReservation() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user1", "123"));
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
        reservationService.payReservation(5L);
	}
	
	@Test(expected=ReservationAlreadyPaid.class)
	public void test_payReservation_reservationAlreadyPaid() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user1", "123"));
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
        reservationService.payReservation(2L);
	}
	
	@Test(expected=EventNotActive.class)
	public void test_payReservation_eventNotActive() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user4", "123"));
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
        reservationService.payReservation(8L);
	}
	
	@Test (expected=ReservationExpired.class)
	public void test_payReservation_reservationExpired() {
		Authentication authentication = Mockito.mock(Authentication.class);
		authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				"user6", "123"));
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
        reservationService.payReservation(6L);
	}
	
	
	@Test(expected=LocationNotFound.class)
	public void test_getLocationReservations_LocationNotFound() {
		List<Reservation> reservation=reservationService.getLocationReservations(500L);
	}
	
	@Test
	public void test_getLocationReservations_ok() {
		List<Reservation> reservations=reservationService.getLocationReservations(1L);
		assertEquals(3,reservations.size());
		
	}
	

}