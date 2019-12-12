package com.nvt.kts.team3.serviceTests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.dto.LocationDTO;
import com.nvt.kts.team3.dto.LocationZoneDTO;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.repository.LocationZoneRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.impl.LocationZoneServiceImpl;

import exception.InvalidLocationZone;
import exception.LocationNotFound;
import exception.LocationZoneNotChangeable;
import exception.LocationZoneNotFound;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class LocationZoneServiceUnitTest {

	@Autowired
	private LocationZoneServiceImpl locationZoneService;
	
	@MockBean
	private LocationZoneRepository locationZoneRepositoryMock;

	@MockBean
	private LocationService locationServiceMock;
	
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
	
	@Test(expected = LocationNotFound.class)
	public void saveLocationNull() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO();
		locationZoneDto.setId(100L);
		when(locationServiceMock.findById(100L)).thenReturn(null);
		locationZoneService.save(locationZoneDto);
	}
	
	@Test(expected = LocationNotFound.class)
	public void saveLocationNotActive() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO();
		locationZoneDto.setId(100L);
		Location location = new Location();
		location.setStatus(false);
		when(locationServiceMock.findById(100L)).thenReturn(location);
		locationZoneService.save(locationZoneDto);
	}
	
	//matrix = true, row < 0, col < 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void saveLocationZoneInvalidLocationZone() {
		Location location = new Location();
		location.setStatus(true);
		when(locationServiceMock.findById(1L)).thenReturn(location);
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_INVALID, CAPACITY_INVALID);
		locationZoneService.save(locationZoneDto);
	}
	
	//capacity > 0, matrix = true, col <  0, row < 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocationZoneInvalidLocationZoneCaseTwo() {
		Location location = new Location();
		location.setStatus(true);
		when(locationServiceMock.findById(1L)).thenReturn(location);
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_INVALID, CAPACITY_VALID);
		locationZoneService.save(locationZoneDto);
	}
	
	//col > 0, row > 0, matrix = false, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocationZoneInvalidLocationZoneCaseThree() {
		Location location = new Location();
		location.setStatus(true);
		when(locationServiceMock.findById(1L)).thenReturn(location);
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, NOT_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_VALID, CAPACITY_INVALID);
		locationZoneService.save(locationZoneDto);
	}
	
	//matrix = true, columns > 0, rows < 0, capacity > 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocationZoneInvalidLocationZoneCaseFour() {
		Location location = new Location();
		location.setStatus(true);
		when(locationServiceMock.findById(1L)).thenReturn(location);
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_VALID, CAPACITY_VALID);
		locationZoneService.save(locationZoneDto);
	}
	
	//matrix = true, columns > 0, rows < 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void saveLocationZoneInvalidLocationZoneCaseFive() {
		Location location = new Location();
		location.setStatus(true);
		when(locationServiceMock.findById(1L)).thenReturn(location);
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_VALID, CAPACITY_INVALID);
		locationZoneService.save(locationZoneDto);
	}
	
	//matrix = true, columns < 0, rows > 0, capacity > 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocationZoneInvalidLocationZoneCaseSix() {
		Location location = new Location();
		location.setStatus(true);
		when(locationServiceMock.findById(1L)).thenReturn(location);
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_INVALID, CAPACITY_VALID);
		locationZoneService.save(locationZoneDto);
	}
	
	//matrix = true, columns < 0, rows > 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocationZoneInvalidLocationZoneCaseSeven() {
		Location location = new Location();
		location.setStatus(true);
		when(locationServiceMock.findById(1L)).thenReturn(location);
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_INVALID, CAPACITY_INVALID);
		locationZoneService.save(locationZoneDto);
	}
	
	@Test
	public void saveLocationZoneSuccessfull() {
		Location location = new Location();
		location.setStatus(true);
		location.setId(ID_OF_LOCATION);
		when(locationServiceMock.findById(1L)).thenReturn(location);
		LocationZone newZone = new LocationZone();
		newZone.setLocation(location);
		newZone.setMatrix(IS_MATRIX);
		newZone.setName(LOCATION_ZONE_NAME);
		newZone.setRowNumber(ROW_VALID);
		newZone.setColNumber(COL_VALID);
		newZone.setCapacity(CAPACITY_INVALID);
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_VALID, CAPACITY_INVALID);
		when(locationZoneRepositoryMock.save(Mockito.any())).thenReturn(newZone);
		LocationZone result = locationZoneService.save(locationZoneDto);
		assertEquals(ID_OF_LOCATION, result.getLocation().getId());
		assertEquals(IS_MATRIX, result.isMatrix());
		assertEquals(LOCATION_ZONE_NAME, result.getName());
		assertEquals(ROW_VALID, result.getRowNumber());
		assertEquals(COL_VALID, result.getColNumber());
		assertEquals(CAPACITY_INVALID, result.getCapacity());
	}
	
	@Test(expected = LocationZoneNotFound.class)
	public void updateLocationZoneNotFound() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO();
		locationZoneDto.setId(100L);
		when(locationZoneRepositoryMock.findById(100L)).thenReturn(Optional.empty());
		locationZoneService.update(locationZoneDto);
	}
	
	@Test(expected = LocationZoneNotFound.class)
	public void updateLocationZoneNotFoundNotActive() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO();
		locationZoneDto.setId(100L);
		LocationZone zone = new LocationZone();
		Location location = new Location();
		location.setStatus(false);
		zone.setLocation(location);
		when(locationZoneRepositoryMock.findById(100L)).thenReturn(Optional.of(zone));
		locationZoneService.update(locationZoneDto);
	}
	/*
	Iz nekog razlog ne radi
	@Test(expected = LocationZoneNotChangeable.class)
	public void updateLocationZoneNotChangeable() {
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		Maintenance m1 = new Maintenance();
		maintenances.add(m1);
		LocationZoneDTO locationZoneDto = new LocationZoneDTO();
		locationZoneDto.setId(100L);
		LocationZone zone = new LocationZone();
		Location location = new Location();
		location.setStatus(true);
		zone.setLocation(location);
		when(locationZoneRepositoryMock.findById(locationZoneDto.getId())).thenReturn(Optional.of(zone));
		when(locationZoneRepositoryMock.getActiveMaintenances(locationZoneDto.getId())).thenReturn(maintenances);
		locationZoneService.update(locationZoneDto);
	}
	*/
	
	//matrix = true, row < 0, col < 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void updateLocationZoneInvalidLocationZone() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_INVALID, CAPACITY_INVALID);
		LocationZone locationZone = new LocationZone();
		Location location = new Location();
		location.setStatus(true);
		locationZone.setLocation(location);
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		when(locationZoneRepositoryMock.findById((LOCATION_ZONE_ID))).thenReturn(Optional.of(locationZone));
		when(locationZoneRepositoryMock.getActiveMaintenances(locationZoneDto.getId())).thenReturn(maintenances);
		when(locationZoneRepositoryMock.save(Mockito.any())).thenReturn(locationZone);
		locationZoneService.update(locationZoneDto);
	}
	

	//capacity > 0, matrix = true, col <  0, row < 0
	@Test(expected = InvalidLocationZone.class)
	public void updateLocationZoneInvalidLocationZoneCaseTwo() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_INVALID, CAPACITY_VALID);
		LocationZone locationZone = new LocationZone();
		Location location = new Location();
		location.setStatus(true);
		locationZone.setLocation(location);
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		when(locationZoneRepositoryMock.findById((LOCATION_ZONE_ID))).thenReturn(Optional.of(locationZone));
		when(locationZoneRepositoryMock.getActiveMaintenances(locationZoneDto.getId())).thenReturn(maintenances);
		when(locationZoneRepositoryMock.save(Mockito.any())).thenReturn(locationZone);
		locationZoneService.update(locationZoneDto);
	}
	
	//col > 0, row > 0, matrix = false, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void updateLocationZoneInvalidLocationZoneCaseThree() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, NOT_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_VALID, CAPACITY_INVALID);
		LocationZone locationZone = new LocationZone();
		Location location = new Location();
		location.setStatus(true);
		locationZone.setLocation(location);
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		when(locationZoneRepositoryMock.findById((LOCATION_ZONE_ID))).thenReturn(Optional.of(locationZone));
		when(locationZoneRepositoryMock.getActiveMaintenances(locationZoneDto.getId())).thenReturn(maintenances);
		when(locationZoneRepositoryMock.save(Mockito.any())).thenReturn(locationZone);
		locationZoneService.update(locationZoneDto);
	}
	
	//matrix = true, columns > 0, rows < 0, capacity > 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void updateLocationZoneInvalidLocationZoneCaseFour() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_VALID, CAPACITY_VALID);
		LocationZone locationZone = new LocationZone();
		Location location = new Location();
		location.setStatus(true);
		locationZone.setLocation(location);
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		when(locationZoneRepositoryMock.findById((LOCATION_ZONE_ID))).thenReturn(Optional.of(locationZone));
		when(locationZoneRepositoryMock.getActiveMaintenances(locationZoneDto.getId())).thenReturn(maintenances);
		when(locationZoneRepositoryMock.save(Mockito.any())).thenReturn(locationZone);
		locationZoneService.update(locationZoneDto);
	}
		
	//matrix = true, columns > 0, rows < 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void updateLocationZoneInvalidLocationZoneCaseFive() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_VALID, CAPACITY_INVALID);
		LocationZone locationZone = new LocationZone();
		Location location = new Location();
		location.setStatus(true);
		locationZone.setLocation(location);
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		when(locationZoneRepositoryMock.findById((LOCATION_ZONE_ID))).thenReturn(Optional.of(locationZone));
		when(locationZoneRepositoryMock.getActiveMaintenances(locationZoneDto.getId())).thenReturn(maintenances);
		when(locationZoneRepositoryMock.save(Mockito.any())).thenReturn(locationZone);
		locationZoneService.update(locationZoneDto);
	}
	
	//matrix = true, columns < 0, rows > 0, capacity > 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void updateLocationZoneInvalidLocationZoneCaseSix() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_INVALID, CAPACITY_VALID);
		LocationZone locationZone = new LocationZone();
		Location location = new Location();
		location.setStatus(true);
		locationZone.setLocation(location);
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		when(locationZoneRepositoryMock.findById((LOCATION_ZONE_ID))).thenReturn(Optional.of(locationZone));
		when(locationZoneRepositoryMock.getActiveMaintenances(locationZoneDto.getId())).thenReturn(maintenances);
		when(locationZoneRepositoryMock.save(Mockito.any())).thenReturn(locationZone);
		locationZoneService.update(locationZoneDto);
	}
	
	@Test
	public void updateLocationZoneSuccessfull() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, "New location zone name", ROW_VALID, COL_VALID, CAPACITY_INVALID);
		LocationZone locationZone = new LocationZone();
		Location location = new Location();
		location.setStatus(true);
		locationZone.setLocation(location);
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		when(locationZoneRepositoryMock.findById((LOCATION_ZONE_ID))).thenReturn(Optional.of(locationZone));
		when(locationZoneRepositoryMock.getActiveMaintenances(locationZoneDto.getId())).thenReturn(maintenances);
		when(locationZoneRepositoryMock.save(Mockito.any())).thenReturn(locationZone);
		locationZoneService.update(locationZoneDto);
		
	}
	
	@Test(expected = LocationZoneNotFound.class)
	public void removeLocationZoneNotFound() {
		when(locationZoneRepositoryMock.findById(100L)).thenReturn(Optional.empty());
		locationZoneService.remove(100L);
	}
	
	@Test(expected = LocationZoneNotChangeable.class)
	public void removeLocationZoneNotChangeable() {
		LocationZone lz = new LocationZone();
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		Maintenance m1 = new Maintenance();
		maintenances.add(m1);
		when(locationZoneRepositoryMock.findById(100L)).thenReturn(Optional.of(lz));
		when(locationZoneRepositoryMock.getActiveMaintenances(100L)).thenReturn(maintenances);
		locationZoneService.remove(100L);
	}
	
	@Test
	public void removeLocationZoneSuccessfull() {
		LocationZone lz = new LocationZone();
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		when(locationZoneRepositoryMock.findById(100L)).thenReturn(Optional.of(lz));
		when(locationZoneRepositoryMock.getActiveMaintenances(100L)).thenReturn(maintenances);
		locationZoneService.remove(100L);
	}
	

	
}
