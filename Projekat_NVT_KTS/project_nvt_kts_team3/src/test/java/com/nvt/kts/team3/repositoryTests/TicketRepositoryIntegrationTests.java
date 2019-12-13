package com.nvt.kts.team3.repositoryTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
import javax.transaction.Transactional;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.repository.TicketRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TicketRepositoryIntegrationTests {
	@Autowired
	TicketRepository ticketRepository;
	
	@Test
	@Transactional
	public void test_getEventTickets() {
		List<Ticket> tickets=ticketRepository.getEventTickets(2L);
		assertEquals(2, tickets.size());
		assertEquals(21,tickets.get(0).getId());
		assertEquals(22,tickets.get(1).getId());
	}
	
	@Test
	@Transactional
	public void test_getEventReservedTickets() {
		List<Ticket> tickets=ticketRepository.getEventReservedTickets(2L);
		assertEquals(2,tickets.size());
	}
	
	@Test
	@Transactional
	public void test_getEventSoldTickets() {
		List<Ticket> tickets=ticketRepository.getEventSoldTickets(2L);
		assertEquals(0,tickets.size());
	}
	
	@Test
	@Transactional
	public void test_getMaintenanceReservedTickets() {
		List<Ticket> tickets=ticketRepository.getMaintenanceReservedTickets(1L);
		assertEquals(17,tickets.size());
		
	}
	
	@Test
	@Transactional
	public void test_getMaintenanceSoldTickets() {
		List<Ticket> tickets=ticketRepository.getMaintenanceSoldTickets(1L);
		assertEquals(2,tickets.size());
	}
	
	@Test
	@Transactional
	public void test_getMaintenanceTickets() { //RADI
		List<Ticket> tickets=ticketRepository.getMaintenanceTickets(1L);
		assertEquals(20, tickets.size());
		int i=1;
		for(Ticket t: tickets) {
			check_returnOfGetMaintenanceTickets(t, i);
			i++;
		}
	
	}
	
	public void check_returnOfGetMaintenanceTickets(Ticket t,int i) {
		assertEquals(i,t.getRow());
		assertEquals(1,t.getCol());
		assertEquals(200,t.getPrice(),0);
		
	}
	
	@Test
	@Transactional
	public void test_getLeasedZoneReservedTickets() { //RADI
		List<Ticket> tickets=ticketRepository.getLeasedZoneReservedTickets(1L);
		assertEquals(17,tickets.size());
	}
	
	@Test
	@Transactional
	public void test_getLeasedZoneSoldTickets() { //RADI
		List<Ticket> tickets=ticketRepository.getLeasedZoneSoldTickets(1L);
		assertEquals(2,tickets.size());
	}
	
	@Test
	@Transactional
	public void test_getLeasedZoneTickets() { //Radi
		List<Ticket> tickets=ticketRepository.getLeasedZoneTickets(1L);
		assertEquals(20, tickets.size());
	
	}
	
	
//	@Test
//	@Transactional
//	@Rollback(true)
//	public void test_deleteByZoneId() {
//		List<Ticket> beforeTickets=ticketRepository.findAll();
//		assertEquals(5,beforeTickets.size());
//		ticketRepository.deleteByZoneId(1L);
//		List<Ticket> afterTickets=ticketRepository.findAll();
//		assertEquals(0,afterTickets.size());
//		
//	}

}
