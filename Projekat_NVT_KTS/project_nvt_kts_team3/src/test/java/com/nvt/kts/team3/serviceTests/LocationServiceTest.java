package com.nvt.kts.team3.serviceTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.LocationDTO;
import com.nvt.kts.team3.dto.LocationZoneDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.repository.LocationRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.impl.LocationServiceImpl;

import exception.InvalidLocationZone;
import exception.LocationExists;
import exception.LocationNotFound;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationServiceTest {

	@Autowired
	private LocationServiceImpl locationService;
	
	@MockBean
	private LocationRepository locationRepositoryMock;

	@MockBean
	private ReservationRepository reservationRepositoryMock;
	
	private static final long LOCATION_ID = 1L;
	private static final String LOCATION_NAME = "Location 1";
	private static final String LOCATION_ADDRESS = "Adress 1";
	private static final String LOCATION_DESCRIPTION = "Description 1";
	private static final List<LocationZoneDTO> LOCATION_ZONE = new ArrayList<LocationZoneDTO>();
	
	
	
	
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
	
	@Test(expected = LocationExists.class)
	public void saveLocationAlreadyExist() {
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(new Location());
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	@Test(expected = InvalidLocationZone.class)
	public void saveLocationLocationZoneEmpty() {
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	

	@Test(expected = InvalidLocationZone.class)
	public void saveLocationLocationZoneNull() {
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, null);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	//matrix = true, row < 0, col < 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void saveLocationZoneInvalidLocationZone() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_INVALID, CAPACITY_INVALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	//capacity > 0, matrix = true, col <  0, row < 0
	@Test(expected = InvalidLocationZone.class)
	public void saveLocationZoneInvalidLocationZoneCaseTwo() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_INVALID, CAPACITY_VALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	
	//col > 0, row > 0, matrix = false, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void saveLocationZoneInvalidLocationZoneCaseThree() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, NOT_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_VALID, CAPACITY_INVALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	//matrix = true, columns > 0, rows < 0, capacity > 0
	@Test(expected = InvalidLocationZone.class)
	public void saveLocationZoneInvalidLocationZoneCaseFour() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_VALID, CAPACITY_VALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	//matrix = true, columns > 0, rows < 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void saveLocationZoneInvalidLocationZoneCaseFive() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_VALID, CAPACITY_INVALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	//matrix = true, columns < 0, rows > 0, capacity > 0
	@Test(expected = InvalidLocationZone.class)
	public void saveLocationZoneInvalidLocationZoneCaseSix() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_INVALID, CAPACITY_VALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	//matrix = true, columns < 0, rows > 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void saveLocationZoneInvalidLocationZoneCaseSeven() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_INVALID, CAPACITY_INVALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	public void saveLocationZoneOneLocationZone() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_VALID, CAPACITY_INVALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		Location location = locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
		assertEquals(location.getName(), locationDto.getName());
		assertEquals(location.getAddress(), locationDto.getAddress());
		assertEquals(location.getDescription(), locationDto.getDescription());
		assertEquals(location.getLocationZones().size(), locationDto.getLocationZone().size());
		checkResultOfSave(location.getLocationZones().iterator().next() , locationDto.getLocationZone().get(0));
	}
	
	public void saveLocationZoneMoreThanOneLocationZone() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_VALID, CAPACITY_INVALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationZoneDTO locationZoneDto2 = new LocationZoneDTO(LOCATION_ZONE_ID2, ID_OF_LOCATION, NOT_MATRIX, LOCATION_ZONE_NAME2, 0, 0, CAPACITY_VALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		Location location = locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
		assertEquals(location.getName(), locationDto.getName());
		assertEquals(location.getAddress(), locationDto.getAddress());
		assertEquals(location.getDescription(), locationDto.getDescription());
		assertEquals(location.getLocationZones().size(), locationDto.getLocationZone().size());
		Iterator it = location.getLocationZones().iterator();
		checkResultOfSave((LocationZone) it.next() , locationDto.getLocationZone().get(0));
		checkResultOfSave((LocationZone) it.next() , locationDto.getLocationZone().get(1));
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
	public void updateLocationNull() {
		when(locationService.findById(LOCATION_ID)).thenReturn(null);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		locationService.update(locationDto);
	}
	
	@Test(expected = LocationNotFound.class)
	public void updateLocationNotActive() {
		Location location = new Location();
		location.setStatus(false);
		when(locationService.findById(LOCATION_ID)).thenReturn(location);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		locationService.update(locationDto);
	}
	
	@Test(expected = LocationExists.class)
	public void updateLocationFindByNameAndAddressLocationExist() {
		Location location = new Location();
		location.setId(1L);
		location.setStatus(true);
		Location location2 = new Location();
		location2.setStatus(true);
		location2.setId(2L);
		when(locationService.findById(LOCATION_ID)).thenReturn(location);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationService.findByNameAndAddress(locationDto.getName(), locationDto.getAddress())).thenReturn(location2);
		locationService.update(locationDto);
	}
	
/*
 	Ovo mora integracionim
	@Test
	public void updateLocationFindByNameAndAddresSuccessfull() {
		Location location = new Location();
		location.setId(1L);
		location.setStatus(true);
		when(locationService.findById(LOCATION_ID)).thenReturn(location);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationService.findByNameAndAddress(locationDto.getName(), locationDto.getAddress())).thenReturn(location);
		Location returnedLocation = locationService.update(locationDto);
		assertEquals(locationDto.getName(), returnedLocation.getName());
		assertEquals(locationDto.getAddress(), returnedLocation.getAddress());
		assertEquals(locationDto.getDescription(), returnedLocation.getDescription());
		
	}

*/
	
	//Remove takodje mora integracionim
	
	//Report takdoje integracioni
	

}
