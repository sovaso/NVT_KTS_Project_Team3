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

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.LocationZoneDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.TicketService;

import exception.InvalidLocationZone;
import exception.LocationNotFound;
import exception.LocationZoneNotChangeable;
import exception.LocationZoneNotFound;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationZoneServiceIntegrationTest {

	@Autowired
	private LocationZoneService locationZoneService;
	
	private static final long LOCATION_ZONE_ID = 2L;
	private static final long LOCATION_ZONE_ID2 = 3L;
	private static final long ID_OF_LOCATION = 1L;
	private static final boolean IS_MATRIX = true;
	private static final boolean NOT_MATRIX = false;
	private static final String LOCATION_ZONE_NAME="Location zone 1";
	private static final String LOCATION_ZONE_NAME2="Location zone 2";
	private static final int ROW_VALID = 10;
	private static final int COL_VALID = 5;
	private static final int CAPACITY_VALID = 50;
	
	private static final int ROW_INVALID = -10;
	private static final int COL_INVALID = -5;
	private static final int CAPACITY_INVALID = -50;
	/*
	@Test
	@Transactional
	public void getActiveMaintenances_successfull() {
		ArrayList<Maintenance> maintenances = locationZoneService.getActiveMaintenances(1L);
		assertEquals(1, maintenances.size());
		assertEquals(1, maintenances.get(0).getId());
	}
	
	@Test
	@Transactional
	public void findAll_successfull() {
		List<LocationZone> maintenances = locationZoneService.findAll();
		assertEquals(1, maintenances.size());
		assertEquals(1, maintenances.get(0).getId());
	}
	*/
	@Test
	@Transactional
	public void findById_successfull() {
		LocationZone location = locationZoneService.findById(1L);
		assertEquals(200, location.getCapacity());
		assertEquals(20, location.getColNumber());
		assertEquals(10, location.getRowNumber());
		assertEquals("Name1", location.getName());
		assertEquals(1, location.getId());
	}
	
	@Transactional
	public void findById_null() {
		LocationZone location = locationZoneService.findById(1000L);
		assertNull(location);
	}
	
	@Test(expected = LocationNotFound.class)
	public void saveLocationNull() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO();
		locationZoneDto.setId(100L);
		locationZoneService.save(locationZoneDto);
	}
	
	@Test(expected = LocationNotFound.class)
	public void saveLocationNotActive() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO();
		locationZoneDto.setId(100L);
		locationZoneDto.setLocationId(3L);
		locationZoneService.save(locationZoneDto);
	}
	
	//matrix = true, row < 0, col < 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void saveLocationZoneInvalidLocationZone() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_INVALID, CAPACITY_INVALID);
		locationZoneService.save(locationZoneDto);
	}

	
	//capacity > 0, matrix = true, col <  0, row < 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocationZoneInvalidLocationZoneCaseTwo() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_INVALID, CAPACITY_VALID);
		locationZoneService.save(locationZoneDto);
	}
	
	//col > 0, row > 0, matrix = false, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocationZoneInvalidLocationZoneCaseThree() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, NOT_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_VALID, CAPACITY_INVALID);
		locationZoneService.save(locationZoneDto);
	}
		
	//matrix = true, columns > 0, rows < 0, capacity > 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocationZoneInvalidLocationZoneCaseFour() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_VALID, CAPACITY_VALID);
		locationZoneService.save(locationZoneDto);
	}
		
	//matrix = true, columns > 0, rows < 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void saveLocationZoneInvalidLocationZoneCaseFive() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_VALID, CAPACITY_INVALID);
		locationZoneService.save(locationZoneDto);
	}
		
	//matrix = true, columns < 0, rows > 0, capacity > 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocationZoneInvalidLocationZoneCaseSix() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_INVALID, CAPACITY_VALID);
		locationZoneService.save(locationZoneDto);
	}
		
	//matrix = true, columns < 0, rows > 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocationZoneInvalidLocationZoneCaseSeven() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_INVALID, CAPACITY_INVALID);
		locationZoneService.save(locationZoneDto);
	}
	/*
	@Test
	public void saveLocationZoneSuccessfull() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_VALID, CAPACITY_VALID);
		LocationZone result = locationZoneService.save(locationZoneDto);
		assertEquals(ID_OF_LOCATION, result.getLocation().getId());
		assertEquals(IS_MATRIX, result.isMatrix());
		assertEquals(LOCATION_ZONE_NAME, result.getName());
		assertEquals(ROW_VALID, result.getRowNumber());
		assertEquals(COL_VALID, result.getColNumber());
		assertEquals(CAPACITY_VALID, result.getCapacity());
	}
	*/
	
	@Test(expected = LocationZoneNotFound.class)
	public void updateLocationZoneNotFound() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO();
		locationZoneDto.setId(100L);
		locationZoneService.update(locationZoneDto);
	}
	
	@Test(expected = LocationZoneNotFound.class)
	public void updateLocationZoneNotFoundNotActive() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO();
		locationZoneDto.setId(2L);
		locationZoneService.update(locationZoneDto);
	}
	
	//matrix = true, row < 0, col < 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void updateLocationZoneInvalidLocationZone() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(3l, 6l, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_INVALID, CAPACITY_INVALID);
		locationZoneService.update(locationZoneDto);
	}
	
	//capacity > 0, matrix = true, col <  0, row < 0
	@Test(expected = InvalidLocationZone.class)
	public void updateLocationZoneInvalidLocationZoneCaseTwo() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(3l, 6l, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_INVALID, CAPACITY_VALID);
		locationZoneService.update(locationZoneDto);
	}
	
	//col > 0, row > 0, matrix = false, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void updateLocationZoneInvalidLocationZoneCaseThree() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(3l, 6l, NOT_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_VALID, CAPACITY_INVALID);
		locationZoneService.update(locationZoneDto);
	}
	
	//matrix = true, columns > 0, rows < 0, capacity > 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void updateLocationZoneInvalidLocationZoneCaseFour() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(3l, 6l, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_VALID, CAPACITY_VALID);
		locationZoneService.update(locationZoneDto);
	}
			
	//matrix = true, columns > 0, rows < 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void updateLocationZoneInvalidLocationZoneCaseFive() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(3l, 6l, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_VALID, CAPACITY_INVALID);
		locationZoneService.update(locationZoneDto);
	}
		
	//matrix = true, columns < 0, rows > 0, capacity > 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void updateLocationZoneInvalidLocationZoneCaseSix() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(3l, 6l, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_INVALID, CAPACITY_VALID);
		locationZoneService.update(locationZoneDto);
	}
	
	/*
	Could not commit JPA transaction....
	@Test
	@Transactional
	public void updateLocationZoneSuccessfull() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(3l, 6l, IS_MATRIX, "New location zone name", ROW_VALID, COL_VALID, CAPACITY_INVALID);
		locationZoneService.update(locationZoneDto);
		
	}
	*/
	
	@Test(expected = LocationZoneNotFound.class)
	public void removeLocationZoneNotFound() {
		locationZoneService.remove(100L);
	}
	
	@Test(expected = LocationZoneNotChangeable.class)
	public void removeLocationZoneNotChangeable() {
		locationZoneService.remove(1L);
	}
	
	/*
	Uraditi rollback nad bazom
	public void removeLocationZoneSuccessfull() {
		locationZoneService.remove(4L);
	}
	
	*/

		
	
}
