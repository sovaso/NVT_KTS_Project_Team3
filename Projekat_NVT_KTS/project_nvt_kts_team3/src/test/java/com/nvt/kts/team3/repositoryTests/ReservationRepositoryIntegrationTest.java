package com.nvt.kts.team3.repositoryTests;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReservationRepositoryIntegrationTest {
	
	@Autowired
	ReservationRepository reservationRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	EventRepository eventRepository;
	
	@Test
	public void test_findByUser() {
		RegularUser u=(RegularUser) this.userRepository.findByUsername("user1");
		List<Reservation> userReservations=this.reservationRepository.findByUser(u);
		assertEquals(3,userReservations.size());
	}
	
	
	@Test
	public void test_findByUserAndPaid() {
		RegularUser u=(RegularUser) this.userRepository.findByUsername("user1");
		List<Reservation> userReservations=this.reservationRepository.findByUserAndPaid(u,false);
		assertEquals(1,userReservations.size());
	}
	
	
	@Test
	public void test_findByEvent() {
		Optional<Event> e=this.eventRepository.findById(2L);
		List<Reservation> reservations=this.reservationRepository.findByEvent(e.get());
		assertEquals(6,reservations.size());
	}
	
	
	

}
