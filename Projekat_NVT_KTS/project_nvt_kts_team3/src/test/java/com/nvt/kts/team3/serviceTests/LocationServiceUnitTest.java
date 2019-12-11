package com.nvt.kts.team3.serviceTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.dto.LocationDTO;
import com.nvt.kts.team3.dto.LocationReportDTO;
import com.nvt.kts.team3.dto.LocationZoneDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.repository.LocationRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.impl.LocationServiceImpl;

import exception.InvalidLocationZone;
import exception.LocationExists;
import exception.LocationNotChangeable;
import exception.LocationNotFound;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationServiceUnitTest {

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
	@Transactional
	public void saveLocation_locationalreadyExist() {
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(new Location());
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocation_locationZoneEmpty() {
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	

	@Test(expected = InvalidLocationZone.class)
	public void saveLocation_locationZoneNull() {
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, null);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	//matrix = true, row < 0, col < 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void saveLocation_invalidLocationZone() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_INVALID, CAPACITY_INVALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	//capacity > 0, matrix = true, col <  0, row < 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocation_invalidLocationZoneCaseTwo() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_INVALID, CAPACITY_VALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	
	//col > 0, row > 0, matrix = false, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocation_invalidLocationZoneCaseThree() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, NOT_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_VALID, CAPACITY_INVALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	//matrix = true, columns > 0, rows < 0, capacity > 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocation_invalidLocationZoneCaseFour() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_VALID, CAPACITY_VALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	//matrix = true, columns > 0, rows < 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	public void saveLocation_invalidLocationZoneCaseFive() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_INVALID, COL_VALID, CAPACITY_INVALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	//matrix = true, columns < 0, rows > 0, capacity > 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocation_invalidLocationZoneCaseSix() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_INVALID, CAPACITY_VALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	//matrix = true, columns < 0, rows > 0, capacity < 0
	@Test(expected = InvalidLocationZone.class)
	@Transactional
	public void saveLocation_invalidLocationZoneCaseSeven() {
		LocationZoneDTO locationZoneDto = new LocationZoneDTO(LOCATION_ZONE_ID, ID_OF_LOCATION, IS_MATRIX, LOCATION_ZONE_NAME, ROW_VALID, COL_INVALID, CAPACITY_INVALID);
		LOCATION_ZONE.add(locationZoneDto);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationRepositoryMock.findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS)).thenReturn(null);
		locationService.save(locationDto);
		verify(locationRepositoryMock).findByNameAndAddress(LOCATION_NAME, LOCATION_ADDRESS);
	}
	
	public void saveLocation_oneLocationZone() {
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
	
	public void saveLocation_moreThanOneLocationZone() {
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
	@Transactional
	public void update_locationNull() {
		when(locationRepositoryMock.findById(LOCATION_ID)).thenReturn(Optional.empty());
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		locationService.update(locationDto);
	}
	
	@Test(expected = LocationNotFound.class)
	@Transactional
	public void update_locationNotActive() {
		Location location = new Location();
		location.setStatus(false);
		when(locationRepositoryMock.findById(LOCATION_ID)).thenReturn(Optional.of(location));
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		locationService.update(locationDto);
	}
	
	@Test(expected = LocationExists.class)
	@Transactional
	public void update_locationFindByNameAndAddressLocation_exist() {
		Location location = new Location();
		location.setId(1L);
		location.setStatus(true);
		Location location2 = new Location();
		location2.setStatus(true);
		location2.setId(2L);
		when(locationRepositoryMock.findById(LOCATION_ID)).thenReturn(Optional.of(location));
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationService.findByNameAndAddress(locationDto.getName(), locationDto.getAddress())).thenReturn(location2);
		locationService.update(locationDto);
	}
	

	@Test
	public void update_LocationFindByNameAndAddres_successfull() {
		Location location = new Location();
		location.setId(1L);
		location.setStatus(true);
		Location updatedLocation = new Location();
		updatedLocation.setId(1L);
		updatedLocation.setName(LOCATION_NAME);
		updatedLocation.setAddress(LOCATION_ADDRESS);
		updatedLocation.setDescription(LOCATION_DESCRIPTION);
		when(locationRepositoryMock.findById(LOCATION_ID)).thenReturn(Optional.of(location));
		when(locationRepositoryMock.save(location)).thenReturn(updatedLocation);
		LocationDTO locationDto = new LocationDTO(LOCATION_ID, LOCATION_NAME, LOCATION_ADDRESS, LOCATION_DESCRIPTION, LOCATION_ZONE);
		when(locationService.findByNameAndAddress(locationDto.getName(), locationDto.getAddress())).thenReturn(location);
		Location returnedLocation = locationService.update(locationDto);
		assertEquals(1L, returnedLocation.getId());
		assertEquals(LOCATION_NAME, returnedLocation.getName());
		assertEquals(LOCATION_ADDRESS, returnedLocation.getAddress());
		assertEquals(LOCATION_DESCRIPTION, returnedLocation.getDescription());
		
	}
	
	@Test(expected = LocationNotFound.class)
	public void remove_locationNotFound() {
		when(locationRepositoryMock.getOne(1L)).thenReturn(null);
		locationService.remove(1L);
	}
	
	@Test(expected = LocationNotFound.class)
	public void remove_locationNotFound_caseTwo() {
		Location location = new Location();
		location.setStatus(false);
		when(locationRepositoryMock.getOne(1L)).thenReturn(location);
		locationService.remove(1L);
	}
	
	@Test(expected = LocationNotChangeable.class)
	public void remove_locationNotChangeable() {
		Location location = new Location();
		location.setStatus(true);
		ArrayList<Event> events = new ArrayList<Event>();
		Event e1 = new Event();
		events.add(e1);
		when(locationRepositoryMock.findById(1L)).thenReturn(Optional.of(location));
		when(locationRepositoryMock.getActiveEvents(1L)).thenReturn(events);
		locationService.remove(1L);
	}
	
	public void remove_locationSuccessfull() {
		Location location = new Location();
		location.setStatus(true);
		ArrayList<Event> events = new ArrayList<Event>();
		when(locationRepositoryMock.getOne(1L)).thenReturn(location);
		when(locationRepositoryMock.getActiveEvents(1L)).thenReturn(events);
		locationService.remove(1L);
	}
	
	@Test
	@Transactional
	public void findAll_nonFound() {
		List<Location> locations = new ArrayList<Location>();
		when(locationRepositoryMock.findAll()).thenReturn(locations);
		List<Location> foundLocations = locationService.findAll();
		assertEquals(0, foundLocations.size());
	}
	
	@Test
	@Transactional
	public void findAll_successfull() {
		List<Location> locations = new ArrayList<Location>();
		Location l1 = new Location();
		Location l2 = new Location();
		Location l3 = new Location();
		locations.add(l1);
		locations.add(l2);
		locations.add(l3);
		when(locationRepositoryMock.findAll()).thenReturn(locations);
		List<Location> foundLocations = locationService.findAll();
		assertEquals(3, foundLocations.size());
	}
	
	@Test
	@Transactional
	public void findByNameAndAddress_nonFound() {
		when(locationRepositoryMock.findByNameAndAddress("someName", "someAddress")).thenReturn(null);
		Location foundLocation = locationService.findByNameAndAddress("someName", "someAddress");
		assertNull(foundLocation);
	}
	
	@Test
	public void findByNameAndAddress_successfull() {
		Location location = new Location();
		location.setName("someName");
		location.setAddress("someAddress");
		when(locationRepositoryMock.findByNameAndAddress("someName", "someAddress")).thenReturn(location);
		Location foundLocation = locationService.findByNameAndAddress("someName", "someAddress");
		assertEquals("someName", foundLocation.getName());
		assertEquals("someAddress", foundLocation.getAddress());
	}
	
	@Test
	@Transactional
	public void getActiveEvents_nonFound() {
		ArrayList<Event> events = new ArrayList<Event>();
		when(locationRepositoryMock.getActiveEvents(1L)).thenReturn(events);
		List<Event> foundEvents = locationService.getActiveEvents(1L);
		assertEquals(0, foundEvents.size());
	}
	
	@Test
	@Transactional
	public void getActiveEvents_successfull() {
		ArrayList<Event> events = new ArrayList<Event>();
		Event e1 = new Event();
		e1.setId(2L);
		events.add(e1);
		when(locationRepositoryMock.getActiveEvents(1L)).thenReturn(events);
		List<Event> foundEvents = locationService.getActiveEvents(1L);
		assertEquals(1, foundEvents.size());
		assertEquals(2L, foundEvents.get(0).getId());
	}
	
	@Test
	@Transactional
	public void findAllActive_nonFound() {
		ArrayList<Location> locations = new ArrayList<Location>();
		when(locationRepositoryMock.getActiveLocations()).thenReturn(locations);
		ArrayList<Location> foundLocations = locationService.findAllActive();
		assertEquals(0, foundLocations.size());
	}
	/*
	
	@Test
	public void findAllActive_successfull() {
		ArrayList<Location> locations = new ArrayList<Location>();
		Location l1 = new Location();
		l1. setId(2L);
		when(locationRepositoryMock.getActiveLocations()).thenReturn(locations);
		ArrayList<Location> foundLocations = locationService.findAllActive();
		assertEquals(1, foundLocations.size());
		assertEquals(2L, foundLocations.get(0).getId());
	}
	*/
	
	/*
	@Test
	@Transactional
	public void checkIfAvailable_successfull() {
		ArrayList<Event> events = new ArrayList<Event>();
		Event e1 = new Event();
		e1.setId(2L);
		LocalDateTime startDate = LocalDateTime.of(2020, Month.SEPTEMBER, 1, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.SEPTEMBER, 11, 10, 10, 30);
		when(locationRepositoryMock.checkIfAvailable(1L, startDate, endDate)).thenReturn(events);
		ArrayList<Event> foundEvents = locationService.checkIfAvailable(1L, startDate, endDate);
		assertEquals(1, foundEvents.size());
		assertEquals(2L, foundEvents.get(0).getId());
	}
	*/
	
	
	@Test
	@Transactional
	public void checkIfAvailable_nonAvailable() {
		ArrayList<Event> events = new ArrayList<Event>();
		LocalDateTime startDate = LocalDateTime.of(2020, Month.SEPTEMBER, 1, 10, 10, 30);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.SEPTEMBER, 11, 10, 10, 30);
		when(locationRepositoryMock.checkIfAvailable(1L, startDate, endDate)).thenReturn(events);
		ArrayList<Event> foundEvents = locationService.checkIfAvailable(1L, startDate, endDate);
		assertEquals(0, foundEvents.size());
	}
	
	
	@Test
	@Transactional
	public void findByAddress_nonFound() {
		when(locationRepositoryMock.findByAddress("someAddress")).thenReturn(null);
		Location foundLocation = locationService.findByAddress("someAddress");
		assertNull(foundLocation);
	}
	
	@Test
	@Transactional
	public void findByAddress_successfull() {
		Location location = new Location();
		location.setId(1L);
		when(locationRepositoryMock.findByAddress("someAddress")).thenReturn(location);
		Location foundLocation = locationService.findByAddress("someAddress");
		assertEquals(1L, foundLocation.getId());
	}
	
	@Test(expected = LocationNotFound.class)
	@Transactional
	public void getLocationReport_locationNotFound() {
		when(locationRepositoryMock.findById(100L)).thenReturn(Optional.empty());
		LocationReportDTO report = locationService.getLocationReport(100L);
		assertNull(report);
	}
	
	@Test
	@Transactional
	public void getLocationReport_findAllNoReservations() {
		Location location = new Location();
		location.setId(1L);
		when(locationRepositoryMock.findById(1L)).thenReturn(Optional.of(location));
		List<Reservation> reservations = new ArrayList<Reservation>();
		when(reservationRepositoryMock.findAll()).thenReturn(reservations);
		LocationReportDTO report = locationService.getLocationReport(1L);
		assertEquals(0, report.getDailyLabels().size());
		assertEquals(0, report.getDailyValues().size());
		assertEquals(0, report.getWeeklyLabels().size());
		assertEquals(0, report.getWeeklyValues().size());
		assertEquals(0, report.getMonthlyLabels().size());
		assertEquals(0, report.getMonthlyValues().size());
		
	}
	
	@Test
	@Transactional
	public void getLocationReport_noReservationsForLocation() {
		Location location = new Location();
		location.setId(1L);
		when(locationRepositoryMock.findById(1L)).thenReturn(Optional.of(location));
		List<Reservation> reservations = new ArrayList<Reservation>();
		Reservation r1 = new Reservation();
		Event e1 = new Event();
		Location l1 = new Location();
		l1. setId(2L);
		e1.setLocationInfo(l1);
		r1.setEvent(e1);
		reservations.add(r1);
		when(reservationRepositoryMock.findAll()).thenReturn(reservations);
		LocationReportDTO report = locationService.getLocationReport(1L);
		assertEquals(0, report.getDailyLabels().size());
		assertEquals(0, report.getDailyValues().size());
		assertEquals(0, report.getWeeklyLabels().size());
		assertEquals(0, report.getWeeklyValues().size());
		assertEquals(0, report.getMonthlyLabels().size());
		assertEquals(0, report.getMonthlyValues().size());
		
	}
	
	@Test
	@Transactional
	public void getLocationReport_successfull() {
		Location location = new Location();
		location.setId(1L);
		
		Event e1 = new Event();
		e1.setLocationInfo(location);
		
		
		long DAY_IN_MILI = 86400000;
		Date currentDate = new Date();
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM");
		Date today = null;
		try {
			today = df1.parse(df1.format(currentDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date thisMonth = null;
		try {
			thisMonth = df2.parse(df2.format(currentDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		List<Reservation> reservations = new ArrayList<Reservation>();
		Reservation r1 = new Reservation();
		r1.setEvent(e1);
		Date date1 = new Date(today.getTime() - DAY_IN_MILI);
		r1.setDateOfReservation(date1);
		reservations.add(r1);
		
		Reservation r2 = new Reservation();
		r2.setEvent(e1);
		Date date2 = new Date(today.getTime() - 2*DAY_IN_MILI);
		r2.setDateOfReservation(date2);
		reservations.add(r2);
		
		Reservation r3 = new Reservation();
		r3.setEvent(e1);
		Date date3 = new Date(today.getTime() - 3*DAY_IN_MILI);
		r3.setDateOfReservation(date3);
		reservations.add(r3);
		
		Reservation r4 = new Reservation();
		r4.setEvent(e1);
		Date date4 = new Date(today.getTime() - 3*DAY_IN_MILI);
		r4.setDateOfReservation(date4);
		reservations.add(r4);
		
		when(locationRepositoryMock.findById(1L)).thenReturn(Optional.of(location));
		when(reservationRepositoryMock.findAll()).thenReturn(reservations);
		
		LocationReportDTO report = locationService.getLocationReport(1L);
		assertEquals(7, report.getDailyLabels().size());
		assertEquals(7, report.getDailyValues().size());
		assertEquals(7, report.getWeeklyLabels().size());
		assertEquals(7, report.getWeeklyValues().size());
		assertEquals(7, report.getMonthlyLabels().size());
		assertEquals(7, report.getMonthlyValues().size());
		
		assertEquals(df1.format(date1), report.getDailyLabels().get(0));
		assertEquals(df1.format(date2), report.getDailyLabels().get(1));
		assertEquals(df1.format(date3), report.getDailyLabels().get(2));
		
		assertEquals(1, (int) report.getDailyValues().get(0));
		assertEquals(1, (int) report.getDailyValues().get(1));
		assertEquals(2, (int) report.getDailyValues().get(2));
		
		assertEquals(4, (int) report.getWeeklyValues().get(0));
		assertEquals(0, (int) report.getWeeklyValues().get(1));
		assertEquals(0, (int) report.getWeeklyValues().get(2));
		
		assertEquals(0, (int) report.getMonthlyValues().get(0));
	
		
		
	}
	
	

}
