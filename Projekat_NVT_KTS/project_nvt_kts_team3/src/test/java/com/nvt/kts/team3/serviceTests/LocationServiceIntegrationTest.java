package com.nvt.kts.team3.serviceTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.LocationDTO;
import com.nvt.kts.team3.dto.LocationReportDTO;
import com.nvt.kts.team3.dto.LocationZoneDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.TicketService;

import exception.InvalidLocationZone;
import exception.LocationExists;
import exception.LocationNotChangeable;
import exception.LocationNotFound;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationServiceIntegrationTest {

	@Autowired
	private LocationService locationService;
	
	private static final long LOCATION_ID = 1L;
	private static final String LOCATION_NAME = "NewLocation2";
	private static final String LOCATION_ADDRESS = "NewAddress2";
	private static final String LOCATION_DESCRIPTION = "NewDescription2";
	private static final List<LocationZoneDTO> LOCATION_ZONE = new ArrayList<LocationZoneDTO>();
	
	private static final long LOCATION_ZONE_ID = 20L;
	private static final long LOCATION_ZONE_ID2 = 30L;
	private static final long ID_OF_LOCATION = 10L;
	private static final boolean IS_MATRIX = true;
	private static final boolean NOT_MATRIX = false;
	private static final String LOCATION_ZONE_NAME="NewLocationZone1";
	private static final String LOCATION_ZONE_NAME2="NewLocationZone2";
	private static final int ROW_VALID = 10;
	private static final int COL_VALID = 5;
	private static final int CAPACITY_VALID = 50;
	
	private static final int ROW_INVALID = -10;
	private static final int COL_INVALID = -5;
	private static final int CAPACITY_INVALID = -50;
	
	@Test(expected = LocationExists.class)
	@Transactional
	public void save_locationAlreadyExist() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setId(1L);
		locationDto.setName("Name1");
		locationDto.setAddress("Address1");
		locationService.save(locationDto);
	}
	
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocation_locationZoneEmpty() {
		LocationDTO locationDto = new LocationDTO();
		locationService.save(locationDto);
	}
	
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocation_LocationZoneNull() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setLocationZone(null);
		locationService.save(locationDto);
	}
	
	
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocation_invalidLocationZone() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setName("NewLocation");
		locationDto.setAddress("NewAddress");
		LocationZoneDTO lzDto = new LocationZoneDTO();
		lzDto.setMatrix(false);
		lzDto.setCapacity(-100);
		lzDto.setRow(-10);
		lzDto.setCol(-20);
		locationDto.getLocationZone().add(lzDto);
		locationService.save(locationDto);
	}
	
	
	//capacity > 0, matrix = true, col <  0, row < 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocation_invalidLocationZone_caseTwo() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setName("NewLocation");
		locationDto.setAddress("NewAddress");
		LocationZoneDTO lzDto = new LocationZoneDTO();
		lzDto.setMatrix(true);
		lzDto.setCapacity(100);
		lzDto.setRow(-10);
		lzDto.setCol(-20);
		locationDto.getLocationZone().add(lzDto);
		locationService.save(locationDto);
	}
	
	//col > 0, row > 0, matrix = false, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocation_invalidLocationZone_caseThree() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setName("NewLocation");
		locationDto.setAddress("NewAddress");
		LocationZoneDTO lzDto = new LocationZoneDTO();
		lzDto.setMatrix(false);
		lzDto.setCapacity(-1100);
		lzDto.setRow(10);
		lzDto.setCol(20);
		locationDto.getLocationZone().add(lzDto);
		locationService.save(locationDto);
		
	}

	
	//matrix = true, columns > 0, rows < 0, capacity > 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocation_invalidLocationZone_caseFour() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setName("NewLocation");
		locationDto.setAddress("NewAddress");
		LocationZoneDTO lzDto = new LocationZoneDTO();
		lzDto.setMatrix(true);
		lzDto.setCapacity(1100);
		lzDto.setRow(-10);
		lzDto.setCol(20);
		locationDto.getLocationZone().add(lzDto);
		locationService.save(locationDto);
	}
	
	//matrix = true, columns > 0, rows < 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void saveLocation_invalidLocationZone_caseFive() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setName("NewLocation");
		locationDto.setAddress("NewAddress");
		LocationZoneDTO lzDto = new LocationZoneDTO();
		lzDto.setMatrix(true);
		lzDto.setCapacity(-1100);
		lzDto.setRow(-10);
		lzDto.setCol(20);
		locationDto.getLocationZone().add(lzDto);
		locationService.save(locationDto);
	}
	
	//matrix = true, columns < 0, rows > 0, capacity > 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocationZoneInvalidLocationZoneCaseSix() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setName("NewLocation");
		locationDto.setAddress("NewAddress");
		LocationZoneDTO lzDto = new LocationZoneDTO();
		lzDto.setMatrix(true);
		lzDto.setCapacity(1100);
		lzDto.setRow(10);
		lzDto.setCol(-20);
		locationDto.getLocationZone().add(lzDto);
		locationService.save(locationDto);
	}
	
	//matrix = true, columns < 0, rows > 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocation_invalidLocationZone_caseSeven() {
		LocationDTO locationDto = new LocationDTO();
		locationDto.setName("NewLocation");
		locationDto.setAddress("NewAddress");
		LocationZoneDTO lzDto = new LocationZoneDTO();
		lzDto.setMatrix(true);
		lzDto.setCapacity(-1100);
		lzDto.setRow(10);
		lzDto.setCol(-20);
		locationDto.getLocationZone().add(lzDto);
		locationService.save(locationDto);
	}
	
	
	@Test
	@Transactional
	@Rollback(true)
	public void saveLocationZoneOneLocationZone() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_VALID, CAPACITY_INVALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(33l, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
	
		Location location = locationService.save(locationDto);
	
		assertEquals(location.getName(), locationDto.getName());
		assertEquals(location.getAddress(), locationDto.getAddress());
		assertEquals(location.getDescription(), locationDto.getDescription());
		assertEquals(location.getLocationZones().size(), locationDto.getLocationZone().size());
		//checkResultOfSave(location.getLocationZones().iterator().next() , locationDto.getLocationZone().get(0));
	}
	
	
	
	public void checkResultOfSave(LocationZone locationZone, LocationZoneDTO locationZoneDTO) {
		assertEquals(locationZone.getName(), locationZoneDTO.getName());
		assertEquals(locationZone.getColNumber(), locationZoneDTO.getCol());
		assertEquals(locationZone.getRowNumber(), locationZoneDTO.getRow());
		assertEquals(locationZone.getCapacity(), locationZoneDTO.getCapacity());
		assertEquals(locationZone.isMatrix(), locationZoneDTO.isMatrix());
		assertEquals(locationZone.getLocation().getId(), locationZoneDTO.getLocationId());
	}
	
	@Test(expected = LocationNotFound.class)
	@Transactional
	public void update_locationNull() {
		long unexistingLocationId = 100L;
		LocationDTO locationDto = new LocationDTO(unexistingLocationId, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		locationService.update(locationDto);
	}
	
	@Test(expected = LocationNotFound.class)
	@Transactional
	public void update_locationNotActive() {
		LocationDTO locationDto = new LocationDTO(3L, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		locationService.update(locationDto);
	}
	
	@Test(expected = LocationExists.class)
	@Transactional
	public void update_findByNameAndAddressLocationExist() {
		LocationDTO locationDto = new LocationDTO(1L, "Name2", "Address2", LOCATION_DESCRIPTION, LOCATION_ZONE);
		locationService.update(locationDto);
	}
	
	@Test
	@Transactional
	public void updateLocationFindByNameAndAddresSuccessfull() {
		LocationDTO locationDto = new LocationDTO(6L, "UpdatedName", "UpdatedAddress", "UpdatedDescription", LOCATION_ZONE);
		Location returnedLocation = locationService.update(locationDto);
		assertEquals(6L, returnedLocation.getId());
		assertEquals("UpdatedName", returnedLocation.getName());
		assertEquals("UpdatedAddress", returnedLocation.getAddress());
		assertEquals("UpdatedDescription", returnedLocation.getDescription());
		
	}

	@Test(expected = LocationNotFound.class)
	public void remove_locationNotFound() {
		locationService.remove(1000L);
	}
	
	@Test(expected = LocationNotFound.class)
	public void remove_locationNotFound_locationStatusIsFalse() {
		locationService.remove(3L);
	}
	
	@Test(expected = LocationNotChangeable.class)
	public void remove_locationNotChangeable() {
		locationService.remove(1L);
	}
	
	@Test
	@Transactional
	public void removeLocationSuccessfull() {
		locationService.remove(6L);
	}
	
	
	@Test
	@Transactional
	public void findAll_successfull() {
		List<Location> foundLocations = locationService.findAll();
		assertEquals(7, foundLocations.size());
	}
	
	@Test
	@Transactional
	public void findByNameAndAddress_nonFound() {
		Location foundLocation = locationService.findByNameAndAddress("someName", "someAddress");
		assertNull(foundLocation);
	}
	
	@Test
	public void findByNameAndAddress_successfull() {
		Location foundLocation = locationService.findByNameAndAddress("Name1", "Address1");
		assertEquals("Name1", foundLocation.getName());
		assertEquals("Address1", foundLocation.getAddress());
	}
	
	
	@Test
	@Transactional
	public void getActiveEvents_nonFound() {
		List<Event> foundEvents = locationService.getActiveEvents(3L);
		assertEquals(0, foundEvents.size());
	}
	
	
	@Test
	@Transactional
	public void getActiveEvents_successfull() {
		List<Event> foundEvents = locationService.getActiveEvents(1L);
		assertEquals(2, foundEvents.size());
		assertEquals(1L, foundEvents.get(0).getId());
		assertEquals("Event1", foundEvents.get(0).getName());
		assertEquals(EventType.SPORTS, foundEvents.get(0).getType());
		
		assertEquals(3L, foundEvents.get(1).getId());
		assertEquals("Event2", foundEvents.get(1).getName());
		assertEquals(EventType.CULTURAL, foundEvents.get(1).getType());
	}
	

	@Test
	@Transactional
	public void findAllActive_successfull() {
		ArrayList<Location> foundLocations = locationService.findAllActive();
		assertEquals(5, foundLocations.size());
	}
	
	@Test
	@Transactional
	public void findByAddress_nonFound() {
		Location foundLocation = locationService.findByAddress("someAddress");
		assertNull(foundLocation);
	}
	
	@Test
	@Transactional
	public void findByAddress_successfull() {
		Location foundLocation = locationService.findByAddress("Address1");
		assertEquals(1L, foundLocation.getId());
	}
	
	//Dodati test za checkIfAvailable
	
	@Test
	@Transactional
	public void findById_successfull() {
		Location foundLocation = locationService.findById(1L);
		assertEquals("Address1", foundLocation.getAddress());
		assertEquals("Name1", foundLocation.getName());
		assertEquals("Description1", foundLocation.getDescription());
	}
	
	@Test
	@Transactional
	public void findById_nonFound_null() {
		Location foundLocation = locationService.findById(100L);
		assertNull(foundLocation);
	}
	
	@Test(expected = LocationNotFound.class)
	@Transactional
	public void getLocationReport_locationNotFound() {
		LocationReportDTO report = locationService.getLocationReport(100L);
		assertNull(report);
	}
	
	
	@Test
	@Transactional
	public void getLocationReport_noReservationsForLocation() {
		LocationReportDTO report = locationService.getLocationReport(3L);
		assertEquals(0, report.getDailyLabels().size());
		assertEquals(0, report.getDailyValues().size());
		assertEquals(0, report.getWeeklyLabels().size());
		assertEquals(0, report.getWeeklyValues().size());
		assertEquals(0, report.getMonthlyLabels().size());
		assertEquals(0, report.getMonthlyValues().size());
		
	}
	
	@Test
	@Transactional
	public void checkIfAvailable_successfull() {
		LocalDateTime startDate = LocalDateTime.of(2022, Month.SEPTEMBER, 1, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2022, Month.SEPTEMBER, 11, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationService.checkIfAvailable(1L, startDate, endDate);
		assertEquals(0, eventsOnThatLocationInThatPeriod.size());
	}
	
	//CASE: (m.maintenanceDate <= ?3 AND m.maintenanceEndTime >= ?3)
	@Test
	@Transactional
	public void checkIfAvailable_unsuccessfull1() {
		LocalDateTime startDate = LocalDateTime.of(2021, Month.JANUARY, 17, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2021, Month.JANUARY, 19, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationService.checkIfAvailable(1L, startDate, endDate);
		assertEquals(1, eventsOnThatLocationInThatPeriod.size());
		assertEquals("Event1", eventsOnThatLocationInThatPeriod.get(0).getName());
		assertEquals(EventType.SPORTS, eventsOnThatLocationInThatPeriod.get(0).getType());
		assertTrue(eventsOnThatLocationInThatPeriod.get(0).isStatus());
		assertEquals(1L, eventsOnThatLocationInThatPeriod.get(0).getLocationInfo().getId());
	}
	
	//CASE: (m.maintenanceDate <= ?2 AND m.maintenanceEndTime >= ?3) 
	@Test
	@Transactional
	public void checkIfAvailable_unsuccessfull2() {
		LocalDateTime startDate = LocalDateTime.of(2021, Month.JANUARY, 16, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2021, Month.JANUARY, 19, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationService.checkIfAvailable(1L, startDate, endDate);
		assertEquals(1, eventsOnThatLocationInThatPeriod.size());
		assertEquals(1, eventsOnThatLocationInThatPeriod.size());
		assertEquals("Event1", eventsOnThatLocationInThatPeriod.get(0).getName());
		assertEquals(EventType.SPORTS, eventsOnThatLocationInThatPeriod.get(0).getType());
		assertTrue(eventsOnThatLocationInThatPeriod.get(0).isStatus());
		assertEquals(1L, eventsOnThatLocationInThatPeriod.get(0).getLocationInfo().getId());
	}
	
	//CASE:  (m.maintenanceDate >= ?2 AND m.maintenanceEndTime <= ?3)
	@Test
	@Transactional
	public void checkIfAvailable_unsuccessfull3() {
		LocalDateTime startDate = LocalDateTime.of(2021, Month.JANUARY, 14, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2021, Month.JANUARY, 19, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationService.checkIfAvailable(1L, startDate, endDate);
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
	@Transactional
	public void checkIfAvailable_successfull2() {
		LocalDateTime startDate = LocalDateTime.of(2021, Month.FEBRUARY, 3, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2021, Month.FEBRUARY, 10, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationService.checkIfAvailable(1L, startDate, endDate);
		assertEquals(0, eventsOnThatLocationInThatPeriod.size());
	}
	
	//CASE: What to take location when it is already reserved according to maintenance, but event 
		//is not active so it is possible.
	@Test
	@Transactional
	public void checkIfAvailable_successfull3() {
		LocalDateTime startDate = LocalDateTime.of(2021, Month.FEBRUARY, 2, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2021, Month.FEBRUARY, 12, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationService.checkIfAvailable(1L, startDate, endDate);
		assertEquals(0, eventsOnThatLocationInThatPeriod.size());
	}
	
	//CASE: What to take location when it is already reserved according to maintenance, but event 
	//is not active so it is possible.
	@Test
	@Transactional
	public void checkIfAvailable_successfull4() {
		LocalDateTime startDate = LocalDateTime.of(2021, Month.FEBRUARY, 1, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2021, Month.FEBRUARY, 11, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationService.checkIfAvailable(1L, startDate, endDate);
		assertEquals(0, eventsOnThatLocationInThatPeriod.size());
	}
		
	//CASE: What to take location when it is already reserved according to maintenance, but event 
	//is not active so it is possible.
	@Test
	@Transactional
	public void checkIfAvailable_successfull5() {
		LocalDateTime startDate = LocalDateTime.of(2021, Month.FEBRUARY, 1, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2021, Month.FEBRUARY, 14, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationService.checkIfAvailable(1L, startDate, endDate);
		assertEquals(0, eventsOnThatLocationInThatPeriod.size());
	}
			
	//CASE: What to take location when it is already reserved according to maintenance, but event 
	//is not active so it is possible.
	@Test
	@Transactional
	public void checkIfAvailable_successfull6() {
		LocalDateTime startDate = LocalDateTime.of(2021, Month.FEBRUARY, 3, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2021, Month.FEBRUARY, 13, 10, 10, 30);
		ArrayList<Event> eventsOnThatLocationInThatPeriod =  locationService.checkIfAvailable(1L, startDate, endDate);
		assertEquals(0, eventsOnThatLocationInThatPeriod.size());
	}
}
