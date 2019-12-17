package com.nvt.kts.team3.serviceTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
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
	
	@Test
	public void test_saveAndFlush() {
		Ticket t=new Ticket(1L,2,2,200,false,null,new LeasedZone());
		Mockito.when(ticketRepositoryMocked.saveAndFlush(t)).thenReturn(t);
		Ticket ticket=ticketRepositoryMocked.saveAndFlush(t);
		assertEquals(2,ticket.getRow());
		assertEquals(2,ticket.getCol());
		assertEquals(200,ticket.getPrice(),0);
		assertNull(ticket.getReservation());	
	}
	
	@Test
	public void test_getMaintenanceReservedTickets() {
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		LocalDateTime ldt2=LocalDateTime.of(2022,12,4,11,11);
		LocalDateTime ldt3=LocalDateTime.of(2022,11,30,11,11);
		Location l=new Location();
		Set<Reservation> reservations=new HashSet<>();
		Set<Maintenance> maintenances=new HashSet<>();
		Set<LeasedZone> leasedZone=new HashSet<>();
		Set<Ticket> tickets=new HashSet<>();
		Event e=new Event(1L,"name1",true,EventType.CULTURAL,reservations,maintenances,l,new ArrayList<String>(),new ArrayList<String>());
		LocationZone lz=new LocationZone(1L,20,"lz1",200,true,10,leasedZone,l);
		LeasedZone ls=new LeasedZone(1L, 200, lz, null, tickets);
		lz.getLeasedZone().add(ls);
		Maintenance m=new Maintenance(ldt, ldt2, ldt3, leasedZone, e);
		m.getLeasedZones().add(ls);
		e.getMaintenances().add(m);
		
		Set<Ticket> reservedTickets=new HashSet<>();
		@SuppressWarnings("deprecation")
		Reservation r=new Reservation(1L, new Date(2019,12,12), false, 600, new RegularUser(), reservedTickets, e, "aaa");
		Ticket t1=new Ticket(1L,1,1,200.0,true,r,ls);
		Ticket t2=new Ticket(2L,1,2,200.0,true,r,ls);
		Ticket t3=new Ticket(3L,1,3,200.0,true,r,ls);
		r.getReservedTickets().add(t1);
		r.getReservedTickets().add(t2);
		r.getReservedTickets().add(t3);
		List<Ticket> tickets2=new ArrayList<>();
		tickets2.add(t1);
		tickets2.add(t2);
		tickets2.add(t3);
		
		Mockito.when(ticketRepositoryMocked.getMaintenanceReservedTickets(1L)).thenReturn(tickets2);
		List<Ticket> tickets3=ticketService.getMaintenanceReservedTickets(1L);
		assertEquals(3,tickets3.size());
		assertEquals(1,tickets3.get(0).getCol());
		assertEquals(2,tickets3.get(1).getCol());
		assertEquals(3,tickets3.get(2).getCol());
	}
	
	
	@Test
	public void test_getMaintenanceSoldTickets() {
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		LocalDateTime ldt2=LocalDateTime.of(2022,12,4,11,11);
		LocalDateTime ldt3=LocalDateTime.of(2022,11,30,11,11);
		Location l=new Location();
		Set<Reservation> reservations=new HashSet<>();
		Set<Maintenance> maintenances=new HashSet<>();
		Set<LeasedZone> leasedZone=new HashSet<>();
		Set<Ticket> tickets=new HashSet<>();
		Event e=new Event(1L,"name1",true,EventType.CULTURAL,reservations,maintenances,l,new ArrayList<String>(),new ArrayList<String>());
		LocationZone lz=new LocationZone(1L,20,"lz1",200,true,10,leasedZone,l);
		LeasedZone ls=new LeasedZone(1L, 200, lz, null, tickets);
		lz.getLeasedZone().add(ls);
		Maintenance m=new Maintenance(ldt, ldt2, ldt3, leasedZone, e);
		m.getLeasedZones().add(ls);
		e.getMaintenances().add(m);
		
		Set<Ticket> reservedTickets=new HashSet<>();
		@SuppressWarnings("deprecation")
		Reservation r=new Reservation(1L, new Date(2019,12,12), false, 600, new RegularUser(), reservedTickets, e, "aaa");
		Ticket t1=new Ticket(1L,1,1,200.0,true,r,ls);
		Ticket t2=new Ticket(2L,1,2,200.0,true,r,ls);
		Ticket t3=new Ticket(3L,1,3,200.0,true,r,ls);
		r.getReservedTickets().add(t1);
		r.getReservedTickets().add(t2);
		r.getReservedTickets().add(t3);
		List<Ticket> tickets2=new ArrayList<>();
		tickets2.add(t1);
		tickets2.add(t2);
		tickets2.add(t3);
		
		Mockito.when(ticketRepositoryMocked.getMaintenanceSoldTickets(1L)).thenReturn(tickets2);
		List<Ticket> tickets3=ticketService.getMaintenanceSoldTickets(1L);
		assertEquals(3,tickets3.size());
		assertEquals(1,tickets3.get(0).getCol());
		assertEquals(2,tickets3.get(1).getCol());
		assertEquals(3,tickets3.get(2).getCol());
	}
	
	@Test
	public void test_getMaintenanceTickets() {
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		LocalDateTime ldt2=LocalDateTime.of(2022,12,4,11,11);
		LocalDateTime ldt3=LocalDateTime.of(2022,11,30,11,11);
		Location l=new Location();
		Set<Reservation> reservations=new HashSet<>();
		Set<Maintenance> maintenances=new HashSet<>();
		Set<LeasedZone> leasedZone=new HashSet<>();
		Set<Ticket> tickets=new HashSet<>();
		Event e=new Event(1L,"name1",true,EventType.CULTURAL,reservations,maintenances,l,new ArrayList<String>(),new ArrayList<String>());
		LocationZone lz=new LocationZone(1L,20,"lz1",200,true,10,leasedZone,l);
		LeasedZone ls=new LeasedZone(1L, 200, lz, null, tickets);
		lz.getLeasedZone().add(ls);
		Maintenance m=new Maintenance(ldt, ldt2, ldt3, leasedZone, e);
		m.getLeasedZones().add(ls);
		e.getMaintenances().add(m);
		
		Set<Ticket> reservedTickets=new HashSet<>();
		@SuppressWarnings("deprecation")
		Reservation r=new Reservation(1L, new Date(2019,12,12), false, 600, new RegularUser(), reservedTickets, e, "aaa");
		Ticket t1=new Ticket(1L,1,1,200.0,true,r,ls);
		Ticket t2=new Ticket(2L,1,2,200.0,true,r,ls);
		Ticket t3=new Ticket(3L,1,3,200.0,true,r,ls);
		r.getReservedTickets().add(t1);
		r.getReservedTickets().add(t2);
		r.getReservedTickets().add(t3);
		List<Ticket> tickets2=new ArrayList<>();
		tickets2.add(t1);
		tickets2.add(t2);
		tickets2.add(t3);
		
		Mockito.when(ticketRepositoryMocked.getMaintenanceTickets(1L)).thenReturn(tickets2);
		List<Ticket> tickets3=ticketService.getMaintenanceTickets(1L);
		assertEquals(3,tickets3.size());
		assertEquals(1,tickets3.get(0).getCol());
		assertEquals(2,tickets3.get(1).getCol());
		assertEquals(3,tickets3.get(2).getCol());
	}
	
	@Test
	public void test_getEventTickets() {
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		LocalDateTime ldt2=LocalDateTime.of(2022,12,4,11,11);
		LocalDateTime ldt3=LocalDateTime.of(2022,11,30,11,11);
		Location l=new Location();
		Set<Reservation> reservations=new HashSet<>();
		Set<Maintenance> maintenances=new HashSet<>();
		Set<LeasedZone> leasedZone=new HashSet<>();
		Set<Ticket> tickets=new HashSet<>();
		Event e=new Event(1L,"name1",true,EventType.CULTURAL,reservations,maintenances,l,new ArrayList<String>(),new ArrayList<String>());
		LocationZone lz=new LocationZone(1L,20,"lz1",200,true,10,leasedZone,l);
		LeasedZone ls=new LeasedZone(1L, 200, lz, null, tickets);
		lz.getLeasedZone().add(ls);
		Maintenance m=new Maintenance(ldt, ldt2, ldt3, leasedZone, e);
		m.getLeasedZones().add(ls);
		e.getMaintenances().add(m);
		
		Set<Ticket> reservedTickets=new HashSet<>();
		@SuppressWarnings("deprecation")
		Reservation r=new Reservation(1L, new Date(2019,12,12), false, 600, new RegularUser(), reservedTickets, e, "aaa");
		Ticket t1=new Ticket(1L,1,1,200.0,true,r,ls);
		Ticket t2=new Ticket(2L,1,2,200.0,true,r,ls);
		Ticket t3=new Ticket(3L,1,3,200.0,true,r,ls);
		r.getReservedTickets().add(t1);
		r.getReservedTickets().add(t2);
		r.getReservedTickets().add(t3);
		List<Ticket> tickets2=new ArrayList<>();
		tickets2.add(t1);
		tickets2.add(t2);
		tickets2.add(t3);
		
		Mockito.when(ticketRepositoryMocked.getEventTickets(1L)).thenReturn(tickets2);
		List<Ticket> tickets3=ticketService.getEventTickets(1L);
		assertEquals(3,tickets3.size());
		assertEquals(1,tickets3.get(0).getCol());
		assertEquals(2,tickets3.get(1).getCol());
		assertEquals(3,tickets3.get(2).getCol());
	}
	
	@Test
	public void test_getEventReservedTickets() {
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		LocalDateTime ldt2=LocalDateTime.of(2022,12,4,11,11);
		LocalDateTime ldt3=LocalDateTime.of(2022,11,30,11,11);
		Location l=new Location();
		Set<Reservation> reservations=new HashSet<>();
		Set<Maintenance> maintenances=new HashSet<>();
		Set<LeasedZone> leasedZone=new HashSet<>();
		Set<Ticket> tickets=new HashSet<>();
		Event e=new Event(1L,"name1",true,EventType.CULTURAL,reservations,maintenances,l,new ArrayList<String>(),new ArrayList<String>());
		LocationZone lz=new LocationZone(1L,20,"lz1",200,true,10,leasedZone,l);
		LeasedZone ls=new LeasedZone(1L, 200, lz, null, tickets);
		lz.getLeasedZone().add(ls);
		Maintenance m=new Maintenance(ldt, ldt2, ldt3, leasedZone, e);
		m.getLeasedZones().add(ls);
		e.getMaintenances().add(m);
		
		Set<Ticket> reservedTickets=new HashSet<>();
		@SuppressWarnings("deprecation")
		Reservation r=new Reservation(1L, new Date(2019,12,12), false, 600, new RegularUser(), reservedTickets, e, "aaa");
		Ticket t1=new Ticket(1L,1,1,200.0,true,r,ls);
		Ticket t2=new Ticket(2L,1,2,200.0,true,r,ls);
		Ticket t3=new Ticket(3L,1,3,200.0,true,r,ls);
		r.getReservedTickets().add(t1);
		r.getReservedTickets().add(t2);
		r.getReservedTickets().add(t3);
		List<Ticket> tickets2=new ArrayList<>();
		tickets2.add(t1);
		tickets2.add(t2);
		tickets2.add(t3);
		
		Mockito.when(ticketRepositoryMocked.getEventReservedTickets(1L)).thenReturn(tickets2);
		List<Ticket> tickets3=ticketService.getEventReservedTickets(1L);
		assertEquals(3,tickets3.size());
		assertEquals(1,tickets3.get(0).getCol());
		assertEquals(2,tickets3.get(1).getCol());
		assertEquals(3,tickets3.get(2).getCol());
	}
	
	@Test
	public void test_getEventSoldTickets() {
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		LocalDateTime ldt2=LocalDateTime.of(2022,12,4,11,11);
		LocalDateTime ldt3=LocalDateTime.of(2022,11,30,11,11);
		Location l=new Location();
		Set<Reservation> reservations=new HashSet<>();
		Set<Maintenance> maintenances=new HashSet<>();
		Set<LeasedZone> leasedZone=new HashSet<>();
		Set<Ticket> tickets=new HashSet<>();
		Event e=new Event(1L,"name1",true,EventType.CULTURAL,reservations,maintenances,l,new ArrayList<String>(),new ArrayList<String>());
		LocationZone lz=new LocationZone(1L,20,"lz1",200,true,10,leasedZone,l);
		LeasedZone ls=new LeasedZone(1L, 200, lz, null, tickets);
		lz.getLeasedZone().add(ls);
		Maintenance m=new Maintenance(ldt, ldt2, ldt3, leasedZone, e);
		m.getLeasedZones().add(ls);
		e.getMaintenances().add(m);
		
		Set<Ticket> reservedTickets=new HashSet<>();
		@SuppressWarnings("deprecation")
		Reservation r=new Reservation(1L, new Date(2019,12,12), false, 600, new RegularUser(), reservedTickets, e, "aaa");
		Ticket t1=new Ticket(1L,1,1,200.0,true,r,ls);
		Ticket t2=new Ticket(2L,1,2,200.0,true,r,ls);
		Ticket t3=new Ticket(3L,1,3,200.0,true,r,ls);
		r.getReservedTickets().add(t1);
		r.getReservedTickets().add(t2);
		r.getReservedTickets().add(t3);
		List<Ticket> tickets2=new ArrayList<>();
		tickets2.add(t1);
		tickets2.add(t2);
		tickets2.add(t3);
		
		Mockito.when(ticketRepositoryMocked.getEventSoldTickets(1L)).thenReturn(tickets2);
		List<Ticket> tickets3=ticketService.getEventSoldTickets(1L);
		assertEquals(3,tickets3.size());
		assertEquals(1,tickets3.get(0).getCol());
		assertEquals(2,tickets3.get(1).getCol());
		assertEquals(3,tickets3.get(2).getCol());
	}
	
	@Test
	public void test_getLeasedZoneReservedTickets() {
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		LocalDateTime ldt2=LocalDateTime.of(2022,12,4,11,11);
		LocalDateTime ldt3=LocalDateTime.of(2022,11,30,11,11);
		Location l=new Location();
		Set<Reservation> reservations=new HashSet<>();
		Set<Maintenance> maintenances=new HashSet<>();
		Set<LeasedZone> leasedZone=new HashSet<>();
		Set<Ticket> tickets=new HashSet<>();
		Event e=new Event(1L,"name1",true,EventType.CULTURAL,reservations,maintenances,l,new ArrayList<String>(),new ArrayList<String>());
		LocationZone lz=new LocationZone(1L,20,"lz1",200,true,10,leasedZone,l);
		LeasedZone ls=new LeasedZone(1L, 200, lz, null, tickets);
		lz.getLeasedZone().add(ls);
		Maintenance m=new Maintenance(ldt, ldt2, ldt3, leasedZone, e);
		m.getLeasedZones().add(ls);
		e.getMaintenances().add(m);
		
		Set<Ticket> reservedTickets=new HashSet<>();
		@SuppressWarnings("deprecation")
		Reservation r=new Reservation(1L, new Date(2019,12,12), false, 600, new RegularUser(), reservedTickets, e, "aaa");
		Ticket t1=new Ticket(1L,1,1,200.0,true,r,ls);
		Ticket t2=new Ticket(2L,1,2,200.0,true,r,ls);
		Ticket t3=new Ticket(3L,1,3,200.0,true,r,ls);
		r.getReservedTickets().add(t1);
		r.getReservedTickets().add(t2);
		r.getReservedTickets().add(t3);
		List<Ticket> tickets2=new ArrayList<>();
		tickets2.add(t1);
		tickets2.add(t2);
		tickets2.add(t3);
		
		Mockito.when(ticketRepositoryMocked.getLeasedZoneReservedTickets(1L)).thenReturn(tickets2);
		List<Ticket> tickets3=ticketService.getLeasedZoneReservedTickets(1L);
		assertEquals(3,tickets3.size());
		assertEquals(1,tickets3.get(0).getCol());
		assertEquals(2,tickets3.get(1).getCol());
		assertEquals(3,tickets3.get(2).getCol());
	}
	
	@Test
	public void test_getLeasedZoneSoldTickets() {
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		LocalDateTime ldt2=LocalDateTime.of(2022,12,4,11,11);
		LocalDateTime ldt3=LocalDateTime.of(2022,11,30,11,11);
		Location l=new Location();
		Set<Reservation> reservations=new HashSet<>();
		Set<Maintenance> maintenances=new HashSet<>();
		Set<LeasedZone> leasedZone=new HashSet<>();
		Set<Ticket> tickets=new HashSet<>();
		Event e=new Event(1L,"name1",true,EventType.CULTURAL,reservations,maintenances,l,new ArrayList<String>(),new ArrayList<String>());
		LocationZone lz=new LocationZone(1L,20,"lz1",200,true,10,leasedZone,l);
		LeasedZone ls=new LeasedZone(1L, 200, lz, null, tickets);
		lz.getLeasedZone().add(ls);
		Maintenance m=new Maintenance(ldt, ldt2, ldt3, leasedZone, e);
		m.getLeasedZones().add(ls);
		e.getMaintenances().add(m);
		
		Set<Ticket> reservedTickets=new HashSet<>();
		@SuppressWarnings("deprecation")
		Reservation r=new Reservation(1L, new Date(2019,12,12), false, 600, new RegularUser(), reservedTickets, e, "aaa");
		Ticket t1=new Ticket(1L,1,1,200.0,true,r,ls);
		Ticket t2=new Ticket(2L,1,2,200.0,true,r,ls);
		Ticket t3=new Ticket(3L,1,3,200.0,true,r,ls);
		r.getReservedTickets().add(t1);
		r.getReservedTickets().add(t2);
		r.getReservedTickets().add(t3);
		List<Ticket> tickets2=new ArrayList<>();
		tickets2.add(t1);
		tickets2.add(t2);
		tickets2.add(t3);
		
		Mockito.when(ticketRepositoryMocked.getLeasedZoneSoldTickets(1L)).thenReturn(tickets2);
		List<Ticket> tickets3=ticketService.getLeasedZoneSoldTickets(1L);
		assertEquals(3,tickets3.size());
		assertEquals(1,tickets3.get(0).getCol());
		assertEquals(2,tickets3.get(1).getCol());
		assertEquals(3,tickets3.get(2).getCol());
	}
	
	@Test
	public void test_getLeasedZoneTickets() {
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		LocalDateTime ldt2=LocalDateTime.of(2022,12,4,11,11);
		LocalDateTime ldt3=LocalDateTime.of(2022,11,30,11,11);
		Location l=new Location();
		Set<Reservation> reservations=new HashSet<>();
		Set<Maintenance> maintenances=new HashSet<>();
		Set<LeasedZone> leasedZone=new HashSet<>();
		Set<Ticket> tickets=new HashSet<>();
		Event e=new Event(1L,"name1",true,EventType.CULTURAL,reservations,maintenances,l,new ArrayList<String>(),new ArrayList<String>());
		LocationZone lz=new LocationZone(1L,20,"lz1",200,true,10,leasedZone,l);
		LeasedZone ls=new LeasedZone(1L, 200, lz, null, tickets);
		lz.getLeasedZone().add(ls);
		Maintenance m=new Maintenance(ldt, ldt2, ldt3, leasedZone, e);
		m.getLeasedZones().add(ls);
		e.getMaintenances().add(m);
		
		Set<Ticket> reservedTickets=new HashSet<>();
		@SuppressWarnings("deprecation")
		Reservation r=new Reservation(1L, new Date(2019,12,12), false, 600, new RegularUser(), reservedTickets, e, "aaa");
		Ticket t1=new Ticket(1L,1,1,200.0,true,r,ls);
		Ticket t2=new Ticket(2L,1,2,200.0,true,r,ls);
		Ticket t3=new Ticket(3L,1,3,200.0,true,r,ls);
		r.getReservedTickets().add(t1);
		r.getReservedTickets().add(t2);
		r.getReservedTickets().add(t3);
		List<Ticket> tickets2=new ArrayList<>();
		tickets2.add(t1);
		tickets2.add(t2);
		tickets2.add(t3);
		
		Mockito.when(ticketRepositoryMocked.getLeasedZoneTickets(1L)).thenReturn(tickets2);
		List<Ticket> tickets3=ticketService.getLeasedZoneTickets(1L);
		assertEquals(3,tickets3.size());
		assertEquals(1,tickets3.get(0).getCol());
		assertEquals(2,tickets3.get(1).getCol());
		assertEquals(3,tickets3.get(2).getCol());
	}
	
	@Test
	public void test_deleteByZoneId() {
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		LocalDateTime ldt2=LocalDateTime.of(2022,12,4,11,11);
		LocalDateTime ldt3=LocalDateTime.of(2022,11,30,11,11);
		Location l=new Location();
		Set<Reservation> reservations=new HashSet<>();
		Set<Maintenance> maintenances=new HashSet<>();
		Set<LeasedZone> leasedZone=new HashSet<>();
		Set<Ticket> tickets=new HashSet<>();
		Event e=new Event(1L,"name1",true,EventType.CULTURAL,reservations,maintenances,l,new ArrayList<String>(),new ArrayList<String>());
		LocationZone lz=new LocationZone(1L,20,"lz1",200,true,10,leasedZone,l);
		LeasedZone ls=new LeasedZone(1L, 200, lz, null, tickets);
		lz.getLeasedZone().add(ls);
		Maintenance m=new Maintenance(ldt, ldt2, ldt3, leasedZone, e);
		m.getLeasedZones().add(ls);
		e.getMaintenances().add(m);
		
		Set<Ticket> reservedTickets=new HashSet<>();
		@SuppressWarnings("deprecation")
		Reservation r=new Reservation(1L, new Date(2019,12,12), false, 600, new RegularUser(), reservedTickets, e, "aaa");
		Ticket t1=new Ticket(1L,1,1,200.0,true,r,ls);
		Ticket t2=new Ticket(2L,1,2,200.0,true,r,ls);
		Ticket t3=new Ticket(3L,1,3,200.0,true,r,ls);
		r.getReservedTickets().add(t1);
		r.getReservedTickets().add(t2);
		r.getReservedTickets().add(t3);
		List<Ticket> tickets2=new ArrayList<>();
		tickets2.add(t1);
		tickets2.add(t2);
		tickets2.add(t3);
		
		Mockito.when(ticketRepositoryMocked.deleteByZoneId(1L)).thenReturn(tickets2);
		List<Ticket> tickets3=ticketService.deleteByZoneId(1L);
		assertEquals(3,tickets3.size());
		assertEquals(1,tickets3.get(0).getCol());
		assertEquals(2,tickets3.get(1).getCol());
		assertEquals(3,tickets3.get(2).getCol());
	}
	
	@Test
	public void test_getExpiredUnpaidTickets() {
		LocalDateTime ldt=LocalDateTime.of(2022,12,2,11,11);
		LocalDateTime ldt2=LocalDateTime.of(2022,12,4,11,11);
		LocalDateTime ldt3=LocalDateTime.of(2022,11,30,11,11);
		Location l=new Location();
		Set<Reservation> reservations=new HashSet<>();
		Set<Maintenance> maintenances=new HashSet<>();
		Set<LeasedZone> leasedZone=new HashSet<>();
		Set<Ticket> tickets=new HashSet<>();
		Event e=new Event(1L,"name1",true,EventType.CULTURAL,reservations,maintenances,l,new ArrayList<String>(),new ArrayList<String>());
		LocationZone lz=new LocationZone(1L,20,"lz1",200,true,10,leasedZone,l);
		LeasedZone ls=new LeasedZone(1L, 200, lz, null, tickets);
		lz.getLeasedZone().add(ls);
		Maintenance m=new Maintenance(ldt, ldt2, ldt3, leasedZone, e);
		m.getLeasedZones().add(ls);
		e.getMaintenances().add(m);
		
		Set<Ticket> reservedTickets=new HashSet<>();
		@SuppressWarnings("deprecation")
		Reservation r=new Reservation(1L, new Date(2019,12,12), false, 600, new RegularUser(), reservedTickets, e, "aaa");
		Ticket t1=new Ticket(1L,1,1,200.0,true,r,ls);
		Ticket t2=new Ticket(2L,1,2,200.0,true,r,ls);
		Ticket t3=new Ticket(3L,1,3,200.0,true,r,ls);
		r.getReservedTickets().add(t1);
		r.getReservedTickets().add(t2);
		r.getReservedTickets().add(t3);
		List<Ticket> tickets2=new ArrayList<>();
		tickets2.add(t1);
		tickets2.add(t2);
		tickets2.add(t3);
		
		Mockito.when(ticketRepositoryMocked.getExpieredUnpaidTickets(ldt, ldt2)).thenReturn(tickets2);
		List<Ticket> tickets3=ticketService.getExpieredUnpaidTickets(ldt, ldt2);
		assertEquals(3,tickets3.size());
		assertEquals(1,tickets3.get(0).getCol());
		assertEquals(2,tickets3.get(1).getCol());
		assertEquals(3,tickets3.get(2).getCol());
	}
	
	
	
	
	
	
}
