package com.nvt.kts.team3.repositoryTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.LocationRepository;
import com.nvt.kts.team3.repository.LocationZoneRepository;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LocationZoneRepositoryIntegrationTest {
	@Autowired
	public LocationZoneRepository locationZoneRepository;
	
	@Test
	public void getActiveMaintenance_successfull() {
		ArrayList<Maintenance> maintenances =  locationZoneRepository.getActiveMaintenances(1L);
		assertEquals(2, maintenances.size());
		
	}
	
	@Test
	public void getActiveMaintenance_empty() {
		ArrayList<Maintenance> maintenances =  locationZoneRepository.getActiveMaintenances(34L);
		assertEquals(0, maintenances.size());
	}

}
