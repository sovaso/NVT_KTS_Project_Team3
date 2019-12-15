package com.nvt.kts.team3.repositoryTests;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReservationRepositoryIntegrationTest {
		@Autowired
		ReservationRepository reservationRepository;
		
		@Autowired
		UserRepository userRepository;
		
		@Test
		@Transactional
		public void test_findByEvent() {
			//RegularUser u=new RegularUser(1L,"user1","user1","user1","a@a","$2a$10$.0EvoW1g2cAX.fcXuvrgzO2e6iOpeWUhAdLJDJHv8xSFZOcrR8uUa");
			List<Reservation> reservations=reservationRepository.findByUser((RegularUser)userRepository.findById(1L));
			
//		   Set<Event> events=new HashSet<>();
//		   Set<LocationZone> locationZones=new HashSet<>();
//		   
//		   Location location=new Location(1L,"Name1","Address23","Description1",true,events,locationZones);
//		   LeasedZone leasedZone=new LeasedZone();
//		   Maintenance mt=new Ma
//		   leasedZone.setId(1L);
//		   
//	       LocationZone locationZone=new LocationZone(1L, 10, "Name1", 200, true, 20, leasedZone, location);
		}
		
}
