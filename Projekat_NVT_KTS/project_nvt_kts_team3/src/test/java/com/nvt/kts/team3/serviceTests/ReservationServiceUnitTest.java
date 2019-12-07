package com.nvt.kts.team3.serviceTests;

import static org.junit.Assert.assertEquals;
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
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.LocationRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.repository.TicketRepository;
import com.nvt.kts.team3.repository.UserRepository;
import com.nvt.kts.team3.service.ReservationService;

import exception.EventNotActive;
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
public class ReservationServiceUnitTest {
	
	@Autowired
	private ReservationService reservationService;
	
	@LocalServerPort
	private int port;

	@MockBean
	private ReservationRepository reservationRepositoryMocked;
	
	@MockBean
	private TicketRepository ticketRepositoryMocked;
	
	@MockBean
	private UserRepository userRepositoryMocked;
	
	@MockBean
	private EventRepository eventRepositoryMocked;
	
	@MockBean
	private LocationRepository locationRepositoryMocked;
	
	@MockBean
	private SecurityContext securityContext;
	
	@MockBean
	Authentication authentication;
	
	@Test
	public void test_findAll_reservationsExist() {
		Reservation r1=new Reservation();
		r1.setId(1L);
		Reservation r2=new Reservation();
		r2.setId(2L);
		ArrayList<Reservation> res=new ArrayList<Reservation>();
		res.add(r1);
		res.add(r2);
		Mockito.when(reservationRepositoryMocked.findAll()).thenReturn(res);
		List<Reservation> back=this.reservationService.findAll();
		assertEquals(2,back.size());
		assertEquals(1L,back.get(0).getId());
		assertEquals(2L,back.get(1).getId());
	}
	
	@Test
	public void test_findAll_reservationsDoNotExist() {
		List<Reservation> res=new ArrayList<Reservation>();
		List<Reservation> ret=this.reservationRepositoryMocked.findAll();
		verify(reservationRepositoryMocked).findAll();
		assertEquals(res,ret);
	}
	
	@Test
	public void test_findByUser_userExists() {
		User u=new RegularUser();
		u.setName("User1");
		Reservation r1=new Reservation();
		r1.setId(2L);
		List<Reservation> res=new ArrayList<Reservation>();
		res.add(r1);
		Mockito.when(reservationRepositoryMocked.findByUser((RegularUser) u)).thenReturn(res);
		List<Reservation> userReservation=reservationService.findByUser((RegularUser) u);
		verify(reservationRepositoryMocked).findByUser((RegularUser) u);
		assertEquals(1,userReservation.size());
		assertEquals(2L,userReservation.get(0).getId());
	}
	
	@Test
	public void test_findByEvent_eventExists() {
		Event e=new Event();
		Reservation r1=new Reservation();
		r1.setId(2L);
		List<Reservation> res=new ArrayList<Reservation>();
		res.add(r1);
		Mockito.when(reservationRepositoryMocked.findByEvent(e)).thenReturn(res);
		List<Reservation> userReservation=reservationService.findByEvent(e);
		//verify(reservationRepositoryMocked).findByUser((RegularUser) u);
		assertEquals(1,userReservation.size());
		assertEquals(2L,userReservation.get(0).getId());
	}

	@Test
	public void test_create_ok() {
		Reservation r=new Reservation();
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		Ticket t1=new Ticket();
		t1.setId(1L);
		t1.setReserved(false);
		t1.setZone(new LeasedZone());
		t1.getZone().setMaintenance(new Maintenance());
		t1.getZone().getMaintenance().setReservationExpiry(ldt);
		r.getReservedTickets().add(t1);
		Event e=new Event();
		e.setId(1L);
		e.setStatus(true);
		r.setEvent(e);
		RegularUser u=new RegularUser();
		u.setUsername("user1");
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		ArrayList<Reservation> userReservations=new ArrayList<Reservation>();
		Mockito.when(eventRepositoryMocked.findById(r.getEvent().getId())).thenReturn(java.util.Optional.of(e));
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.when(reservationRepositoryMocked.findByUserAndPaid((RegularUser) u, false)).thenReturn(userReservations);
		Mockito.when(ticketRepositoryMocked.findById(t1.getId())).thenReturn(java.util.Optional.of(t1));
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		Mockito.when(ticketRepositoryMocked.save(t1)).thenReturn(t1);
		Reservation res=reservationService.create(r);
		assertEquals(r,res);
	}
	
	@Test(expected = EventNotActive.class)
	public void test_create_eventNotActive() {
		Reservation r=new Reservation();
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		Ticket t1=new Ticket();
		t1.setId(1L);
		t1.setReserved(false);
		t1.setZone(new LeasedZone());
		t1.getZone().setMaintenance(new Maintenance());
		t1.getZone().getMaintenance().setReservationExpiry(ldt);
		r.getReservedTickets().add(t1);
		Event e=new Event();
		e.setId(1L);
		e.setStatus(false);
		r.setEvent(e);
		RegularUser u=new RegularUser();
		u.setUsername("user1");
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		ArrayList<Reservation> userReservations=new ArrayList<Reservation>();
		Mockito.when(eventRepositoryMocked.findById(r.getEvent().getId())).thenReturn(java.util.Optional.of(e));
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.when(reservationRepositoryMocked.findByUserAndPaid((RegularUser) u, false)).thenReturn(userReservations);
		Mockito.when(ticketRepositoryMocked.findById(t1.getId())).thenReturn(java.util.Optional.of(t1));
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		Mockito.when(ticketRepositoryMocked.save(t1)).thenReturn(t1);
		reservationService.create(r);
	}
	
	@Test(expected = NoLoggedUser.class)
	public void test_create_noLoggedUser() {
		Reservation r=new Reservation();
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		Ticket t1=new Ticket();
		t1.setId(1L);
		t1.setReserved(false);
		t1.setZone(new LeasedZone());
		t1.getZone().setMaintenance(new Maintenance());
		t1.getZone().getMaintenance().setReservationExpiry(ldt);
		r.getReservedTickets().add(t1);
		Event e=new Event();
		e.setId(1L);
		e.setStatus(true);
		r.setEvent(e);
		RegularUser u=new RegularUser();
		u.setUsername("user1");
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		ArrayList<Reservation> userReservations=new ArrayList<Reservation>();
		Mockito.when(eventRepositoryMocked.findById(r.getEvent().getId())).thenReturn(java.util.Optional.of(e));
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user2")).thenReturn(u);
		Mockito.when(reservationRepositoryMocked.findByUserAndPaid((RegularUser) u, false)).thenReturn(userReservations);
		Mockito.when(ticketRepositoryMocked.findById(t1.getId())).thenReturn(java.util.Optional.of(t1));
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		Mockito.when(ticketRepositoryMocked.save(t1)).thenReturn(t1);
		reservationService.create(r);
	}
	
	@Test(expected = TooManyTicketsReserved.class)
	public void test_create_tooManyTicketsReserved() {
		Reservation r=new Reservation();
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		Ticket t1=new Ticket();
		t1.setId(1L);
		t1.setReserved(false);
		t1.setZone(new LeasedZone());
		t1.getZone().setMaintenance(new Maintenance());
		t1.getZone().getMaintenance().setReservationExpiry(ldt);
		r.getReservedTickets().add(t1);
		
		Event e=new Event();
		e.setId(1L);
		e.setStatus(true);
		r.setEvent(e);
		RegularUser u=new RegularUser();
		u.setUsername("user1");
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		ArrayList<Reservation> userReservations=new ArrayList<Reservation>();
		for(int i=0;i<101;i++) {
			Reservation tic=new Reservation();
			tic.getReservedTickets().add(new Ticket());
			userReservations.add(tic);
		}
		Mockito.when(eventRepositoryMocked.findById(r.getEvent().getId())).thenReturn(java.util.Optional.of(e));
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.when(reservationRepositoryMocked.findByUserAndPaid((RegularUser) u, false)).thenReturn(userReservations);
		Mockito.when(ticketRepositoryMocked.findById(t1.getId())).thenReturn(java.util.Optional.of(t1));
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		Mockito.when(ticketRepositoryMocked.save(t1)).thenReturn(t1);
		reservationService.create(r);
	}
	
	@Test(expected = ReservationCannotBeCreated.class)
	public void test_create_reservationCannotBeCreated() {
		Reservation r=new Reservation();
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		Ticket t1=new Ticket();
		t1.setId(1L);
		t1.setReserved(true);
		t1.setZone(new LeasedZone());
		t1.getZone().setMaintenance(new Maintenance());
		t1.getZone().getMaintenance().setReservationExpiry(ldt);
		r.getReservedTickets().add(t1);
		Event e=new Event();
		e.setId(1L);
		e.setStatus(true);
		r.setEvent(e);
		RegularUser u=new RegularUser();
		u.setUsername("user1");
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		ArrayList<Reservation> userReservations=new ArrayList<Reservation>();
		Mockito.when(eventRepositoryMocked.findById(r.getEvent().getId())).thenReturn(java.util.Optional.of(e));
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.when(reservationRepositoryMocked.findByUserAndPaid((RegularUser) u, false)).thenReturn(userReservations);
		Mockito.when(ticketRepositoryMocked.findById(t1.getId())).thenReturn(java.util.Optional.of(t1));
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		Mockito.when(ticketRepositoryMocked.save(t1)).thenReturn(t1);
	    reservationService.create(r);
	 
	}
	
	@Test
	public void test_findByUserAndPaid() {
		RegularUser u=new RegularUser();
		Reservation r=new Reservation();
		r.setId(1L);
		Set<Reservation> res=new HashSet<>();
		res.add(r);
		r.setPaid(false);
		u.setReservations(res);
		ArrayList<Reservation> arrayRes=new ArrayList<>();
		for(Reservation rr: res) {
			arrayRes.add(rr);
		}
		Mockito.when(reservationRepositoryMocked.findByUserAndPaid(u, false)).thenReturn(arrayRes);
		List<Reservation> reservations=reservationService.findByUserAndPaid(u, false);
		assertEquals(1,reservations.size());
		assertEquals(1L,reservations.get(0).getId());
	
	}
	
	@Test
	public void test_findByUserAndPaidEmpty() {
		RegularUser u=new RegularUser();
		Set<Reservation> res=new HashSet<>();
		u.setReservations(res);
		ArrayList<Reservation> arrayRes=new ArrayList<>();
		for(Reservation rr: res) {
			arrayRes.add(rr);
		}
		Mockito.when(reservationRepositoryMocked.findByUserAndPaid(u, false)).thenReturn(arrayRes);
		List<Reservation> reservations=reservationService.findByUserAndPaid(u, false);
		assertEquals(0,reservations.size());
	
	}
	
	
	@Test
	public void test_payReservation_ok() {
		Reservation r=new Reservation();
		r.setId(1L);
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		Ticket t1=new Ticket();
		t1.setId(1L);
		t1.setReserved(false);
		t1.setZone(new LeasedZone());
		t1.getZone().setMaintenance(new Maintenance());
		t1.getZone().getMaintenance().setReservationExpiry(ldt);
		r.getReservedTickets().add(t1);
		Event e=new Event();
		r.setEvent(e);
		e.setStatus(true);
		r.setPaid(false);
		r.setId(1L);
		RegularUser u=new RegularUser();
		r.setUser(u);
		u.setUsername("user1");
		u.setId(1L);
		u.getReservations().add(r);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.when(reservationRepositoryMocked.findById(1L)).thenReturn(java.util.Optional.of(r));
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		boolean value=reservationService.payReservation(1L);
		assertEquals(true,value);
		assertEquals(true,r.isPaid());
	}
	
	@Test(expected = ReservationExpired.class)
	public void test_payReservation_reservationExpired() {
		Reservation r=new Reservation();
		r.setId(1L);
		LocalDateTime ldt=LocalDateTime.of(2018,12,2,11,11);
		Ticket t1=new Ticket();
		t1.setId(1L);
		t1.setReserved(false);
		t1.setZone(new LeasedZone());
		t1.getZone().setMaintenance(new Maintenance());
		t1.getZone().getMaintenance().setReservationExpiry(ldt);
		r.getReservedTickets().add(t1);
		Event e=new Event();
		r.setEvent(e);
		e.setStatus(true);
		r.setPaid(false);
		r.setId(1L);
		RegularUser u=new RegularUser();
		r.setUser(u);
		u.setUsername("user1");
		u.setId(1L);
		u.getReservations().add(r);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.when(reservationRepositoryMocked.findById(1L)).thenReturn(java.util.Optional.of(r));
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		reservationService.payReservation(1L);
	}
	
	@Test(expected = EventNotActive.class)
	public void test_payReservation_eventNotActive() {
		Reservation r=new Reservation();
		r.setId(1L);
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		Ticket t1=new Ticket();
		t1.setId(1L);
		t1.setReserved(false);
		t1.setZone(new LeasedZone());
		t1.getZone().setMaintenance(new Maintenance());
		t1.getZone().getMaintenance().setReservationExpiry(ldt);
		r.getReservedTickets().add(t1);
		Event e=new Event();
		r.setEvent(e);
		e.setStatus(false);
		r.setPaid(false);
		r.setId(1L);
		RegularUser u=new RegularUser();
		r.setUser(u);
		u.setUsername("user1");
		u.setId(1L);
		u.getReservations().add(r);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.when(reservationRepositoryMocked.findById(1L)).thenReturn(java.util.Optional.of(r));
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		reservationService.payReservation(1L);
	}
	
	@Test(expected = ReservationAlreadyPaid.class)
	public void test_payReservation_reservationAlreadyPaid() {
		Reservation r=new Reservation();
		r.setId(1L);
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		Ticket t1=new Ticket();
		t1.setId(1L);
		t1.setReserved(false);
		t1.setZone(new LeasedZone());
		t1.getZone().setMaintenance(new Maintenance());
		t1.getZone().getMaintenance().setReservationExpiry(ldt);
		r.getReservedTickets().add(t1);
		Event e=new Event();
		r.setEvent(e);
		e.setStatus(true);
		r.setPaid(true);
		r.setId(1L);
		RegularUser u=new RegularUser();
		r.setUser(u);
		u.setUsername("user1");
		u.setId(1L);
		u.getReservations().add(r);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.when(reservationRepositoryMocked.findById(1L)).thenReturn(java.util.Optional.of(r));
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		reservationService.payReservation(1L);
	}
	
	@Test(expected = NotUserReservation.class)
	public void test_payReservation_notUserReservation() {
		Reservation r=new Reservation();
		r.setId(1L);
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		Ticket t1=new Ticket();
		t1.setId(1L);
		t1.setReserved(false);
		t1.setZone(new LeasedZone());
		t1.getZone().setMaintenance(new Maintenance());
		t1.getZone().getMaintenance().setReservationExpiry(ldt);
		r.getReservedTickets().add(t1);
		Event e=new Event();
		r.setEvent(e);
		e.setStatus(false);
		r.setPaid(false);
		r.setId(1L);
		RegularUser u=new RegularUser();
		RegularUser u2=new RegularUser();
		u2.setId(2L);
		u2.setUsername("user2");
		r.setUser(u2);
		u.setUsername("user1");
		u.setId(1L);
		u.getReservations().add(r);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.when(reservationRepositoryMocked.findById(1L)).thenReturn(java.util.Optional.of(r));
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		reservationService.payReservation(1L);
	}
	
	@Test(expected = ReservationNotFound.class)
	public void test_payReservation_reservationNotFound() {
		Reservation r=new Reservation();
		r.setId(3L);
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		Ticket t1=new Ticket();
		t1.setId(1L);
		t1.setReserved(false);
		t1.setZone(new LeasedZone());
		t1.getZone().setMaintenance(new Maintenance());
		t1.getZone().getMaintenance().setReservationExpiry(ldt);
		r.getReservedTickets().add(t1);
		Event e=new Event();
		r.setEvent(e);
		e.setStatus(false);
		r.setPaid(false);
		r.setId(1L);
		RegularUser u=new RegularUser();
		RegularUser u2=new RegularUser();
		u2.setId(2L);
		u2.setUsername("user2");
		r.setUser(u2);
		u.setUsername("user1");
		u.setId(1L);
		u.getReservations().add(r);
		Optional<Reservation> empty = Optional.empty();
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.when(reservationRepositoryMocked.findById(1L)).thenReturn(empty);
		Mockito.when(reservationRepositoryMocked.save(r)).thenReturn(r);
		reservationService.payReservation(1L);
	}
	
	@Test
	public void test_getLocationReservations_ok() {
		Location loc=new Location();
		loc.setId(1L);
		Reservation r=new Reservation();
		r.setEvent(new Event());
		r.getEvent().setLocationInfo(loc);
		Reservation r2=new Reservation();
		r2.setEvent(new Event());
		r2.getEvent().setLocationInfo(new Location());
		ArrayList<Reservation> res=new ArrayList<>();
		res.add(r);
		res.add(r2);
		Mockito.when(locationRepositoryMocked.findById(1L)).thenReturn(java.util.Optional.of(loc));
		Mockito.when(reservationRepositoryMocked.findAll()).thenReturn(res);
		List<Reservation> reservations=reservationService.getLocationReservations(1L);
		assertEquals(1,reservations.size());
		
	}
	
	
	@Test(expected=LocationNotFound.class)
	public void test_getLocationReservations_LocationNotFound() {
		Mockito.when(locationRepositoryMocked.findById(1L)).thenReturn(Optional.empty());
		reservationService.getLocationReservations(1L);		
	}
	
	@Test
	public void test_remove_ok() {
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
		Reservation r=new Reservation(1L,new Date(),false,200.0,u,tickets,e,"aaa");
		t.setReservation(r);
		Mockito.when(reservationRepositoryMocked.findById(1L)).thenReturn(Optional.of(r));
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.doNothing().when(reservationRepositoryMocked).deleteById(1L);
		boolean success=reservationService.remove(1L);
		assertTrue(success);
		
	}
	
	
	@Test(expected=ReservationNotFound.class)
	public void test_remove_reservationNotFound() {
		Mockito.when(reservationRepositoryMocked.findById(1L)).thenReturn(Optional.empty());
		reservationService.remove(1L);
		
	}
	
	@Test(expected=ReservationAlreadyPaid.class)
	public void test_remove_reservationAlreadyPaid() {
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
		Mockito.when(reservationRepositoryMocked.findById(1L)).thenReturn(Optional.of(r));
		reservationService.remove(1L);
	}
	
	
	@Test(expected=NotUserReservation.class)
	public void test_remove_notUserReservation() {
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
		Reservation r=new Reservation(1L,new Date(),false,200.0,u2,tickets,e,"aaa");
		t.setReservation(r);
		Mockito.when(reservationRepositoryMocked.findById(1L)).thenReturn(Optional.of(r));
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		reservationService.remove(1L);	
	}
	
	
	@Test(expected=ReservationCannotBeCancelled.class)
	public void test_remove_reservationCannotBeCancelled() {
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
		Reservation r=new Reservation(1L,new Date(),false,200.0,u,tickets,e,"aaa");
		t.setReservation(r);
		Mockito.when(reservationRepositoryMocked.findById(1L)).thenReturn(Optional.of(r));
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("user1");
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
		Mockito.doNothing().when(reservationRepositoryMocked).deleteById(1L);
		reservationService.remove(1L);
		
	}
	
	@Test
	public void test_findByUser_userDoesntExist() {
		User u=new RegularUser();
		u.setName("User1");
		Mockito.when(reservationRepositoryMocked.findByUser((RegularUser) u)).thenReturn(new ArrayList<>());
		List<Reservation> userReservation=reservationService.findByUser((RegularUser) u);
		verify(reservationRepositoryMocked).findByUser((RegularUser) u);
		assertEquals(0,userReservation.size());
	}
	
	@Test
	public void test_findByEvent_eventDoesntExist() {
		Event e=new Event();
		Mockito.when(reservationRepositoryMocked.findByEvent(e)).thenReturn(new ArrayList<>());
		List<Reservation> userReservation=reservationService.findByEvent(e);
		//verify(reservationRepositoryMocked).findByUser((RegularUser) u);
		assertEquals(0,userReservation.size());
	}

	
	
	
	

	
	
	
	
	
	
	
	
	
	

}
