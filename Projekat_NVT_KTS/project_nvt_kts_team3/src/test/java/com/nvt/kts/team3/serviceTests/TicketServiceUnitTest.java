package com.nvt.kts.team3.serviceTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.repository.TicketRepository;
import com.nvt.kts.team3.repository.UserRepository;
import com.nvt.kts.team3.service.TicketService;

import exception.NotUserReservation;
import exception.TicketExpired;
import exception.TicketNotFound;
import exception.TicketNotReserved;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class TicketServiceUnitTest {

	@Autowired
	TicketService ticketService;
	
	@MockBean
	TicketRepository ticketRepositoryMocked;
	
	@MockBean
	private ReservationRepository reservationRepositoryMocked;

	@MockBean
	private UserRepository userRepositoryMocked;
	
	
	@MockBean
	private SecurityContext securityContext;
	
	@MockBean
	Authentication authentication;
	
	@Test
	public void test_cancelTicket_ok() {
		RegularUser u=new RegularUser();
		u.setUsername("user1");
		u.setId(1L);
		Ticket t=new Ticket();
		t.setZone(new LeasedZone());
		t.getZone().setMaintenance(new Maintenance());
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		t.getZone().getMaintenance().setReservationExpiry(ldt);
		t.setReserved(true);
		Set<Ticket> tickets=new HashSet<Ticket>();
		Event e=new Event();
		e.setStatus(true);
		tickets.add(t);
		Reservation r=new Reservation(1L,new Date(),true,200.0,u,tickets,e,"aaa");
		t.setReservation(r);
		Mockito.when(ticketRepositoryMocked.findById(1L)).thenReturn(Optional.of(t));
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.doNothing().when(reservationRepositoryMocked).deleteById(1L);
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		boolean success=ticketService.cancelTicket(1L);
		Mockito.when(ticketRepositoryMocked.save(t)).thenReturn(t);
		assertTrue(success);
		
	}
	
	
	@Test(expected=TicketExpired.class)
	public void test_cancelTicket_ticketExpired() {
		RegularUser u=new RegularUser();
		u.setUsername("user1");
		u.setId(1L);
		Ticket t=new Ticket();
		t.setZone(new LeasedZone());
		t.getZone().setMaintenance(new Maintenance());
		LocalDateTime ldt=LocalDateTime.of(2018,12,2,11,11);
		t.getZone().getMaintenance().setReservationExpiry(ldt);
		t.setReserved(true);
		Set<Ticket> tickets=new HashSet<Ticket>();
		Event e=new Event();
		e.setStatus(true);
		tickets.add(t);
		Reservation r=new Reservation(1L,new Date(),true,200.0,u,tickets,e,"aaa");
		t.setReservation(r);
		Mockito.when(ticketRepositoryMocked.findById(1L)).thenReturn(Optional.of(t));
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.doNothing().when(reservationRepositoryMocked).deleteById(1L);
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		Mockito.when(ticketRepositoryMocked.save(t)).thenReturn(t);
		ticketService.cancelTicket(1L);
		
	}
	
	@Test(expected=NotUserReservation.class)
	public void test_cancelTicket_notUserReservation() {
		RegularUser u=new RegularUser();
		u.setUsername("user1");
		u.setId(1L);
		
		RegularUser u2=new RegularUser();
		u2.setUsername("user2");
		u2.setId(2L);
		Ticket t=new Ticket();
		t.setZone(new LeasedZone());
		t.getZone().setMaintenance(new Maintenance());
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		t.getZone().getMaintenance().setReservationExpiry(ldt);
		t.setReserved(true);
		Set<Ticket> tickets=new HashSet<Ticket>();
		Event e=new Event();
		e.setStatus(true);
		tickets.add(t);
		Reservation r=new Reservation(1L,new Date(),true,200.0,u2,tickets,e,"aaa");
		t.setReservation(r);
		Mockito.when(ticketRepositoryMocked.findById(1L)).thenReturn(Optional.of(t));
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.doNothing().when(reservationRepositoryMocked).deleteById(1L);
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		Mockito.when(ticketRepositoryMocked.save(t)).thenReturn(t);
		ticketService.cancelTicket(1L);
	}
	
	@Test(expected=TicketNotReserved.class)
	public void test_cancelTicket_ticketNotReserved() {
		RegularUser u=new RegularUser();
		u.setUsername("user1");

		Ticket t=new Ticket();
		t.setZone(new LeasedZone());
		t.getZone().setMaintenance(new Maintenance());
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		t.getZone().getMaintenance().setReservationExpiry(ldt);
		t.setReserved(true);
		Set<Ticket> tickets=new HashSet<Ticket>();
		Event e=new Event();
		e.setStatus(true);
		tickets.add(t);
		Reservation r=new Reservation(1L,new Date(),true,200.0,u,tickets,e,"aaa");
		t.setReservation(null);
		Mockito.when(ticketRepositoryMocked.findById(1L)).thenReturn(Optional.of(t));
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.doNothing().when(reservationRepositoryMocked).deleteById(1L);
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		Mockito.when(ticketRepositoryMocked.save(t)).thenReturn(t);
		ticketService.cancelTicket(1L);
	}
	
	@Test(expected=TicketNotFound.class)
	public void test_cancelTicket_ticketNotFound() {
		Mockito.when(ticketRepositoryMocked.findById(1L)).thenReturn(Optional.empty());
		ticketService.cancelTicket(1L);
	}
	
	
	
	
}
