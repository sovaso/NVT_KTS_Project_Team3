package com.nvt.kts.team3.serviceTests;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.service.TicketService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application.properties")
@Transactional
public class TicketServiceIntegrationTest {
	
	@Autowired
	TicketService ticketService;
	
	
	@Test
	@Transactional
	public void test_getEventTickets() {
		List<Ticket> tickets=ticketService.getEventTickets(2L);
		assertEquals(6, tickets.size());
	}
	
	@Test
	@Transactional
	public void test_getEventReservedTickets() {
		List<Ticket> tickets=ticketService.getEventReservedTickets(2L);
		assertEquals(4,tickets.size());
	}
	
	@Test
	@Transactional
	public void test_getEventSoldTickets() {
		List<Ticket> tickets=ticketService.getEventSoldTickets(2L);
		assertEquals(1,tickets.size());
	}
	
	@Test
	@Transactional
	public void test_getMaintenanceReservedTickets() {
		List<Ticket> tickets=ticketService.getMaintenanceReservedTickets(1L);
		assertEquals(16,tickets.size());
		
	}
	
	@Test
	@Transactional
	public void test_getMaintenanceSoldTickets() {
		List<Ticket> tickets=ticketService.getMaintenanceSoldTickets(1L);
		assertEquals(13,tickets.size());
	}
	
	@Test
	@Transactional
	public void test_getMaintenanceTickets() { //RADI
		List<Ticket> tickets=ticketService.getMaintenanceTickets(1L);
		assertEquals(18, tickets.size());
		for(Ticket t: tickets) {
			check_returnOfGetMaintenanceTickets(t);
		}
	
	}
	
	public void check_returnOfGetMaintenanceTickets(Ticket t) {
		assertEquals(1,t.getCol());
		assertEquals(200,t.getPrice(),0);
		
	}
	
	@Test
	@Transactional
	public void test_getLeasedZoneReservedTickets() { //RADI
		List<Ticket> tickets=ticketService.getLeasedZoneReservedTickets(1L);
		assertEquals(16,tickets.size());
	}
	
	@Test
	@Transactional
	public void test_getLeasedZoneSoldTickets() { //RADI
		List<Ticket> tickets=ticketService.getLeasedZoneSoldTickets(1L);
		assertEquals(13,tickets.size());
	}
	
	@Test
	@Transactional
	public void test_getLeasedZoneTickets() { //Radi
		List<Ticket> tickets=ticketService.getLeasedZoneTickets(1L);
		assertEquals(18, tickets.size());
	
	}
	
	@Test
	@Transactional
	public void test_getExpieredUnpaidTickets() {
		LocalDateTime ldt=LocalDateTime.of(2019,12,01,00,00);
		LocalDateTime ldt2=LocalDateTime.of(2019,12,05,00,00);
		List<Ticket> tickets=ticketService.getExpieredUnpaidTickets(ldt, ldt2);
		assertEquals(3,tickets.size());
	}
	
	@Test
	@Transactional
	public void test_getExpieredUnpaidTickets_success2() {
		LocalDateTime ldt=LocalDateTime.of(2021,01,16,00,00);
		LocalDateTime ldt2=LocalDateTime.of(2021,01,19,00,00);
		List<Ticket> tickets=ticketService.getExpieredUnpaidTickets(ldt, ldt2);
		assertEquals(3,tickets.size());
	}
	
	@Test
	@Transactional
	public void deleteByZoneId() {
		List<Ticket> tickets=ticketService.deleteByZoneId(6L);
		assertEquals(2,tickets.size());
		assertEquals(23,tickets.get(0).getRow());
		assertEquals(24,tickets.get(1).getRow());
		assertEquals(29,ticketService.findAll().size());
	}
	

}
