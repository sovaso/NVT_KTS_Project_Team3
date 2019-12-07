package com.nvt.kts.team3.serviceTests;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.repository.UserRepository;
import com.nvt.kts.team3.service.ReservationService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application.properties")
public class ReservationServiceIntegrationTest {
	
	@Autowired
	ReservationService reservationService;
	
//	@MockBean
//	UserRepository userRepositoryMocked;
//	
//	@MockBean
//	private SecurityContext securityContext;
//	
//	@MockBean
//	Authentication authentication;
//	
//	@Test
//	@Transactional
//	public void test_create_ok() {
//		Event e=new Event();
//		e.setId(1L);
//		Ticket t=new Ticket();
//		t.setId(3L);
//		Set<Ticket> tickets=new HashSet<>();
//		tickets.add(t);
//		RegularUser u=new RegularUser();
//		u.setUsername("user1");
//		u.setId(1L);
//		Reservation r=new Reservation(1L, new Date(), false, 0, u, tickets, e, "aaa");
//		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//		SecurityContextHolder.setContext(securityContext);
//		Mockito.when(authentication.getName()).thenReturn("user1");
//		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("user1");
//		Mockito.when(userRepositoryMocked.findByUsername("user1")).thenReturn(u);
//		Reservation r2=reservationService.create(r);
//	}
	
	@Test
	public void test_getLocationReservations_ok() {
		List<Reservation> reservations=reservationService.getLocationReservations(1L);
		
		
	}
	

}
