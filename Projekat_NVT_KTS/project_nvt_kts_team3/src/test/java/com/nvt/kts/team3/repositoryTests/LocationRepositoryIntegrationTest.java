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
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.LocationRepository;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LocationRepositoryIntegrationTest {

	@Autowired
	public LocationRepository locationRepository;
	
	@Test
	public void findByNameAndAddress_nonFound() {
		Location location = locationRepository.findByNameAndAddress("someName", "someAddress");
		assertNull(location);
	}
	
	@Test
	public void findByNameAndAddress_successfull() {
		Location location = locationRepository.findByNameAndAddress("Name1", "Address1");
		assertEquals("Name1", location.getName());
		assertEquals("Address1", location.getAddress());
		assertEquals("Description1", location.getDescription());
		assertTrue(location.isStatus());
		
	}
	
	@Test
	public void getActiveLocations_successfull() {
		List<Location> activeLocations = locationRepository.getActiveLocations();
		assertEquals(5, activeLocations.size());
	}
	
	@Test
	public void findByAddress_nonFound() {
		Location location = locationRepository.findByAddress("someAddress");
		assertNull(location);
	}
	
	@Test
	public void findByAddress_successfull() {
		Location location = locationRepository.findByAddress("Address1");
		assertEquals("Name1", location.getName());
		assertEquals("Address1", location.getAddress());
		assertEquals("Description1", location.getDescription());
		assertTrue(location.isStatus());
	}
	
	//Postoje dva maintaince-a za event1, ali uzimamo distinct..
	//Postoji i event3 za tu lokaciju, ali je neaktivan, vidimo da ga je metoda repozitorijuma zaobisla
	//Postoji i event8 za tu lokaciju, ali je maintance u proslosti, metoda ga je uspesno izostavila
	@Test
	public void getActiveEvents_successfull() {
		ArrayList<Event> events = locationRepository.getActiveEvents(1L);
		assertEquals(2, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals("Event2", events.get(1).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(EventType.CULTURAL, events.get(1).getType());
		assertTrue(events.get(0).isStatus());
		assertTrue(events.get(1).isStatus());
		assertEquals(1L, events.get(0).getLocationInfo().getId());
		assertEquals(1L, events.get(1).getLocationInfo().getId());
	}
	
	@Test
	public void getActiveEvents_unexistingIdOfLocationGiven() {
		ArrayList<Event> events = locationRepository.getActiveEvents(99L);
		assertEquals(0, events.size());
	}
	
	@Test
	public void getActiveEvents_eventsOnThatLocationNotActive() {
		ArrayList<Event> events = locationRepository.getActiveEvents(6L);
		assertEquals(0, events.size());
	}
	
	@Test
	public void getActiveEvents_eventActiveButMaintenanceInPast() {
		ArrayList<Event> events = locationRepository.getActiveEvents(8L);
		assertEquals(0, events.size());
	}
	
	@Test
	public void getActiveEvents_eventNotActiveMaintenanceInPast() {
		ArrayList<Event> events = locationRepository.getActiveEvents(4L);
		assertEquals(0, events.size());
	}
	
	@Test
	public void checkIfAvailable_successfull() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.SEPTEMBER, 1, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.SEPTEMBER, 11, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationRepository.checkIfAvailable(1L, startDate, endDate);
		assertEquals(0, eventsOnThatLocationInThatPeriod.size());
	}
	
	//CASE: (m.maintenanceDate <= ?3 AND m.maintenanceEndTime >= ?3)
	@Test
	public void checkIfAvailable_unsuccessfull1() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 17, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 19, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationRepository.checkIfAvailable(1L, startDate, endDate);
		assertEquals(1, eventsOnThatLocationInThatPeriod.size());
		assertEquals("Event1", eventsOnThatLocationInThatPeriod.get(0).getName());
		assertEquals(EventType.SPORTS, eventsOnThatLocationInThatPeriod.get(0).getType());
		assertTrue(eventsOnThatLocationInThatPeriod.get(0).isStatus());
		assertEquals(1L, eventsOnThatLocationInThatPeriod.get(0).getLocationInfo().getId());
	
	}
	

	//CASE: (m.maintenanceDate <= ?2 AND m.maintenanceEndTime >= ?3) 
	@Test
	public void checkIfAvailable_unsuccessfull2() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 16, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 19, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationRepository.checkIfAvailable(1L, startDate, endDate);
		assertEquals(1, eventsOnThatLocationInThatPeriod.size());
		assertEquals(1, eventsOnThatLocationInThatPeriod.size());
		assertEquals("Event1", eventsOnThatLocationInThatPeriod.get(0).getName());
		assertEquals(EventType.SPORTS, eventsOnThatLocationInThatPeriod.get(0).getType());
		assertTrue(eventsOnThatLocationInThatPeriod.get(0).isStatus());
		assertEquals(1L, eventsOnThatLocationInThatPeriod.get(0).getLocationInfo().getId());
	}
	
	//CASE:  (m.maintenanceDate >= ?2 AND m.maintenanceEndTime <= ?3)
	@Test
	public void checkIfAvailable_unsuccessfull3() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 14, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 19, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationRepository.checkIfAvailable(1L, startDate, endDate);
		assertEquals(1, eventsOnThatLocationInThatPeriod.size());
		assertEquals(1, eventsOnThatLocationInThatPeriod.size());
		assertEquals("Event1", eventsOnThatLocationInThatPeriod.get(0).getName());
		assertEquals(EventType.SPORTS, eventsOnThatLocationInThatPeriod.get(0).getType());
		assertTrue(eventsOnThatLocationInThatPeriod.get(0).isStatus());
		assertEquals(1L, eventsOnThatLocationInThatPeriod.get(0).getLocationInfo().getId());
	}
	//CASE: What to take location when it is already reserved according to maintenance, but event 
	//is not active so it is possible.
	@Test
	public void checkIfAvailable_successfull2() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 3, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.FEBRUARY, 10, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationRepository.checkIfAvailable(1L, startDate, endDate);
		assertEquals(0, eventsOnThatLocationInThatPeriod.size());
	}
	
	//CASE: What to take location when it is already reserved according to maintenance, but event 
	//is not active so it is possible.
	@Test
	public void checkIfAvailable_successfull3() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 2, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.FEBRUARY, 12, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationRepository.checkIfAvailable(1L, startDate, endDate);
		assertEquals(0, eventsOnThatLocationInThatPeriod.size());
	}
		
	//CASE: What to take location when it is already reserved according to maintenance, but event 
	//is not active so it is possible.
	@Test
	public void checkIfAvailable_successfull4() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 1, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.FEBRUARY, 11, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationRepository.checkIfAvailable(1L, startDate, endDate);
		assertEquals(0, eventsOnThatLocationInThatPeriod.size());
	}
	
	//CASE: What to take location when it is already reserved according to maintenance, but event 
	//is not active so it is possible.
	@Test
	public void checkIfAvailable_successfull5() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 1, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.FEBRUARY, 14, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationRepository.checkIfAvailable(1L, startDate, endDate);
		assertEquals(0, eventsOnThatLocationInThatPeriod.size());
	}
		
	//CASE: What to take location when it is already reserved according to maintenance, but event 
//is not active so it is possible.
	@Test
	public void checkIfAvailable_successfull6() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.FEBRUARY, 3, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.FEBRUARY, 13, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationRepository.checkIfAvailable(1L, startDate, endDate);
		assertEquals(0, eventsOnThatLocationInThatPeriod.size());
	}

}
