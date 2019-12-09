package com.nvt.kts.team3.serviceTests;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.service.ReservationService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application.properties")
public class ReservationServiceIntegrationTest {
	
	@Autowired
	ReservationService reservationService;
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
//	@Before
//	public void login() {
//		JwtAuthenticationRequest authenticationRequest=new JwtAuthenticationRequest();
//		authenticationRequest.setUsername("user1");
//		authenticationRequest.setPassword("123");
//		ResponseEntity<Object> responseEntity = 
//				restTemplate.postForEntity("/auth/login", 
//						authenticationRequest,Object.class);
//		System.out.println(responseEntity.getBody());
//		// preuzmemo token jer ce nam trebati za testiranje REST kontrolera
//		//accessToken = responseEntity.getBody();
//		
//	}
	
	@Test
	public void test_create_ok() {
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
		t.setId(4L);
		Set<Ticket> tickets=new HashSet<>();
		tickets.add(t);
		Reservation r=new Reservation(1L, new Date(), false, 0, null, tickets, e, "aaa");
		Reservation r2=reservationService.create(r);
	}
	
//	@Test
//	public void test_getLocationReservations_ok() {
//		List<Reservation> reservations=reservationService.getLocationReservations(1L);
//		
//		
//	}
	

}
