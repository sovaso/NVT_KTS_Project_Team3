package com.nvt.kts.team3.serviceTests;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.MaintenanceDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.repository.MaintenanceRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LeasedZoneService;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.ReservationService;
import com.nvt.kts.team3.service.TicketService;

import exception.EventNotActive;
import exception.EventNotChangeable;
import exception.EventNotFound;
import exception.InvalidDate;
import exception.InvalidLocationZone;
import exception.InvalidPrice;
import exception.LocationNotAvailable;
import exception.LocationNotFound;
import exception.LocationZoneNotAvailable;
import exception.MaintenanceNotChangeable;
import exception.MaintenanceNotFound;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MaintenanceServiceUnitTest {

	@Autowired
	private MaintenanceService maintenanceService;
	
	@MockBean
	private MaintenanceRepository maintenanceRepositoryMock;

	@MockBean
	private EventService eventServiceMock;

	@MockBean
	private TicketService ticketServiceMock;

	@MockBean
	private LocationService locationServiceMock;

	@MockBean
	private LeasedZoneService leasedZoneServiceMock;

	@MockBean
	private LocationZoneService locationZoneServiceMock;
	
	@MockBean
	private ReservationService reservationServiceMock;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	private static final long LOCATION_ID = 125L;
	private static final long EVENT_ID = 126L;
	private static final long MAINTENANCE_ID = 127L;
	private static final long ID = 0L;
	private static final long VALID_ZONE_ID = 11L;
	private static final long INVALID_ZONE_ID = 12L;
	
	private static final String VALID_START_DATE = "2021-01-01 22:00";
	private static final String VALID_END_DATE = "2021-01-01 23:00";
	private static final String VALID_EXPIRY_DATE = "2020-12-12 23:00";
	private static final String EXPIERED_START_DATE = "2018-01-01 22:00";
	private static final String EXPIERED_END_DATE = "2018-01-01 23:00";
	private static final String MAINTENANCE_25H_START = "2021-01-01 22:00";
	private static final String MAINTENANCE_25H_END = "2021-01-02 23:00";
	private static final String MAINTENANCE_30MIN_START = "2021-01-01 22:00";
	private static final String MAINTENANCE_30MIN_END = "2021-01-01 22:25";
	private static final String BAD_FORMAT_START_DATE = "2021.01.01, 22:00";
	private static final String BAD_FORMAT_END_DATE = "2021.01.01, 23:00";
	
	private static final double VALID_PRICE = 30;
	private static final double INVALID_HIGH_PRICE = 11000;
	private static final double INVALID_LOW_PRICE = 0;
	
	@Test(expected = EventNotFound.class)
	public void testSave_OnEventNotFound() throws ParseException{
		MaintenanceDTO request = new MaintenanceDTO();
		request.setEventId(EVENT_ID);
		
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(null);
		
		maintenanceService.save(request);
	}
	
	@Test(expected = EventNotChangeable.class)
	public void testSave_EventNotChangeable_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(false);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO();
		request.setEventId(EVENT_ID);
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(false);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
	
		maintenanceService.save(request);
	}
	
	@Test(expected = EventNotChangeable.class)
	public void testSave_EventNotChangeable_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(EXPIERED_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(EXPIERED_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);

		MaintenanceDTO request = new MaintenanceDTO();
		request.setEventId(EVENT_ID);
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(false);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
	
		maintenanceService.save(request);
	}
	
	@Test(expected = EventNotChangeable.class)
	public void testSave_EventNotChangeable_3() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(EXPIERED_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(EXPIERED_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(false);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO();
		request.setEventId(EVENT_ID);
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(false);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
	
		maintenanceService.save(request);
	}
	
	@Test(expected = LocationNotFound.class)
	public void testSave_LocationNotFound_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(false);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO();
		request.setEventId(EVENT_ID);
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
	
		maintenanceService.save(request);
	}
	
	@Test(expected = LocationNotFound.class)
	public void testSave_LocationNotFound_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(null);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO();
		request.setEventId(EVENT_ID);
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
	
		maintenanceService.save(request);
	}
	
	@Test(expected = InvalidDate.class)
	public void testSave_InvalidDate_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				EXPIERED_START_DATE,
				EXPIERED_END_DATE,
				ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
	
		maintenanceService.save(request);
	}
	
	@Test(expected = InvalidDate.class)
	public void testSave_InvalidDate_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				MAINTENANCE_25H_START,
				MAINTENANCE_25H_END,
				ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
	
		maintenanceService.save(request);
	}
	
	@Test(expected = InvalidDate.class)
	public void testSave_InvalidDate_3() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				MAINTENANCE_30MIN_START,
				MAINTENANCE_30MIN_END,
				ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
	
		maintenanceService.save(request);
		
	}
	
	@Test(expected = InvalidDate.class)
	public void testSave_InvalidDate_4() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_END_DATE,
				VALID_START_DATE,
				ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		
		maintenanceService.save(request);
	}
	
	@Test(expected = ParseException.class)
	public void testSave_ParseException() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				BAD_FORMAT_START_DATE,
				BAD_FORMAT_END_DATE,
				ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
	
		maintenanceService.save(request);
	}
	
	@Test(expected = LocationNotAvailable.class)
	public void testSave_LocationNotAvailable() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		locationEvents.add(new Event());
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		
		maintenanceService.save(request);
	}
	
	@Test(expected = InvalidLocationZone.class)
	public void testSave_InvalidLocationZone_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		
		maintenanceService.save(request);
	}
	
	@Test(expected = InvalidLocationZone.class)
	public void testSave_InvalidLocationZone_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				EVENT_ID,
				null);
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		
		maintenanceService.save(request);
		
	}
	
	@Test(expected = LocationZoneNotAvailable.class)
	public void testSave_LocationZoneNotAvailable_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				INVALID_ZONE_ID,
				ID,
				VALID_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				EVENT_ID,
				leasedZones);
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		when(locationZoneServiceMock.findById(INVALID_ZONE_ID)).thenReturn(null);
		
		maintenanceService.save(request);
	}
	
	@Test(expected = LocationZoneNotAvailable.class)
	public void testSave_LocationZoneNotAvailable_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		LocationZone lz = new LocationZone(
				VALID_ZONE_ID,
				15,
				"PARTERRE",
				150,
				false,
				10,
				new HashSet<LeasedZone>(),
				location);
		location.getLocationZones().add(lz);
		
		Location location2 = new Location(
				ID,
				"Location2", 
				"Address2", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		LocationZone lz2 = new LocationZone(
				VALID_ZONE_ID,
				15,
				"PARTERRE",
				150,
				false,
				10,
				new HashSet<LeasedZone>(),
				location2);
		location2.getLocationZones().add(lz2);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				INVALID_ZONE_ID,
				ID,
				VALID_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				EVENT_ID,
				leasedZones);
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		when(locationZoneServiceMock.findById(VALID_ZONE_ID)).thenReturn(lz);
		when(locationZoneServiceMock.findById(INVALID_ZONE_ID)).thenReturn(lz2);
		
		maintenanceService.save(request);
	}
	
	@Test(expected = InvalidPrice.class)
	public void testSave_InvalidPrice_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		LocationZone lz = new LocationZone(
				VALID_ZONE_ID,
				15,
				"PARTERRE",
				150,
				false,
				10,
				new HashSet<LeasedZone>(),
				location);
		location.getLocationZones().add(lz);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				VALID_ZONE_ID,
				ID,
				INVALID_HIGH_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				EVENT_ID,
				leasedZones);
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		when(locationZoneServiceMock.findById(VALID_ZONE_ID)).thenReturn(lz);
		
		maintenanceService.save(request);
	}
	
	@Test(expected = InvalidPrice.class)
	public void testSave_InvalidPrice_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		LocationZone lz = new LocationZone(
				VALID_ZONE_ID,
				15,
				"PARTERRE",
				150,
				false,
				10,
				new HashSet<LeasedZone>(),
				location);
		location.getLocationZones().add(lz);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				VALID_ZONE_ID,
				ID,
				INVALID_LOW_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				EVENT_ID,
				leasedZones);
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(eventServiceMock.findById(EVENT_ID)).thenReturn(event);
		when(maintenanceRepositoryMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		when(locationZoneServiceMock.findById(VALID_ZONE_ID)).thenReturn(lz);
		
		maintenanceService.save(request);
	}
	
	//INTEGRACIONI - kada pokusavam dodati istu location zone za isti maintenance
	//			   - validno save
	
	@Test(expected = MaintenanceNotFound.class)
	public void testUpdate_MaintenanceNotFound() throws ParseException{
		MaintenanceDTO request = new MaintenanceDTO();
		request.setId(MAINTENANCE_ID);
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = EventNotFound.class)
	public void testUpdate_EventNotFound() throws ParseException{
		Maintenance maintenance = new Maintenance();
		maintenance.setEvent(null);
		
		MaintenanceDTO request = new MaintenanceDTO();
		request.setId(MAINTENANCE_ID);
		request.setEventId(EVENT_ID);
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = EventNotChangeable.class)
	public void testUpdate_EventNotChangeable() throws ParseException{
		Event event = new Event();
		event.setStatus(true);
		event.setId(EVENT_ID);

		Maintenance maintenance = new Maintenance();
		maintenance.setEvent(event);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO();
		request.setId(MAINTENANCE_ID);
		request.setEventId(EVENT_ID);
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(false);
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = InvalidDate.class)
	public void testUpdate_InvalidDate_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		maintenance.setId(MAINTENANCE_ID);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				EXPIERED_START_DATE,
				EXPIERED_END_DATE,
				MAINTENANCE_ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
	
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = InvalidDate.class)
	public void testUpdate_InvalidDate_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		maintenance.setId(MAINTENANCE_ID);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				MAINTENANCE_25H_START,
				MAINTENANCE_25H_END,
				MAINTENANCE_ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
	
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = InvalidDate.class)
	public void testUpdate_InvalidDate_3() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		maintenance.setId(MAINTENANCE_ID);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				MAINTENANCE_30MIN_START,
				MAINTENANCE_30MIN_END,
				MAINTENANCE_ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
	
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = InvalidDate.class)
	public void testUpdate_InvalidDate_4() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		maintenance.setId(MAINTENANCE_ID);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_END_DATE,
				VALID_START_DATE,
				MAINTENANCE_ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
	
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = ParseException.class)
	public void testUpdate_ParseException() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		maintenance.setId(MAINTENANCE_ID);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				BAD_FORMAT_START_DATE,
				BAD_FORMAT_END_DATE,
				MAINTENANCE_ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
	
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = LocationNotFound.class)
	public void testUpdate_LocationNotFound_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(false);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		maintenance.setId(MAINTENANCE_ID);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				MAINTENANCE_ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
	
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = LocationNotFound.class)
	public void testUpdate_LocationNotFound_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(null);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		maintenance.setId(MAINTENANCE_ID);
		event.getMaintenances().add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				MAINTENANCE_ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
	
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = MaintenanceNotChangeable.class)
	public void testUpdate_MaintenanceNotChangeable() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		maintenance.setId(MAINTENANCE_ID);
		event.getMaintenances().add(maintenance);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		tickets.add(new Ticket());
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				MAINTENANCE_ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getMaintenanceReservedTickets(MAINTENANCE_ID)).thenReturn(tickets);
	
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = LocationNotAvailable.class)
	public void testUpdate_LocationNotAvailable_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		maintenance.setId(MAINTENANCE_ID);
		event.getMaintenances().add(maintenance);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		Maintenance newMaintenance = new Maintenance();
		newMaintenance.setId(ID);
		maintenances.add(newMaintenance);
		maintenances.add(maintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				MAINTENANCE_ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getMaintenanceReservedTickets(MAINTENANCE_ID)).thenReturn(tickets);
		when(maintenanceRepositoryMock.getMaintenancesForDate(LOCATION_ID, startDate, endDate)).thenReturn(maintenances);
	
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = LocationNotAvailable.class)
	public void testUpdate_LocationNotAvailable_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(true);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		maintenance.setId(MAINTENANCE_ID);
		event.getMaintenances().add(maintenance);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		Maintenance newMaintenance = new Maintenance();
		newMaintenance.setId(ID);
		maintenances.add(newMaintenance);
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				MAINTENANCE_ID,
				EVENT_ID,
				new ArrayList<LeasedZoneDTO>());
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getMaintenanceReservedTickets(MAINTENANCE_ID)).thenReturn(tickets);
		when(maintenanceRepositoryMock.getMaintenancesForDate(LOCATION_ID, startDate, endDate)).thenReturn(maintenances);
	
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = LocationZoneNotAvailable.class)
	public void testUpdate_LocationZoneNotAvailable_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		maintenance.setId(MAINTENANCE_ID);
		event.getMaintenances().add(maintenance);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		maintenances.add(maintenance);
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				INVALID_ZONE_ID,
				ID,
				VALID_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				MAINTENANCE_ID,
				EVENT_ID,
				leasedZones);
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getMaintenanceReservedTickets(MAINTENANCE_ID)).thenReturn(tickets);
		when(maintenanceRepositoryMock.getMaintenancesForDate(LOCATION_ID, startDate, endDate)).thenReturn(maintenances);
		when(locationZoneServiceMock.findById(INVALID_ZONE_ID)).thenReturn(null);
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = LocationZoneNotAvailable.class)
	public void testUpdate_LocationZoneNotAvailable_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		LocationZone lz = new LocationZone(
				VALID_ZONE_ID,
				15,
				"PARTERRE",
				150,
				false,
				10,
				new HashSet<LeasedZone>(),
				location);
		location.getLocationZones().add(lz);
		
		Location location2 = new Location(
				ID,
				"Location2", 
				"Address2", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		LocationZone lz2 = new LocationZone(
				VALID_ZONE_ID,
				15,
				"PARTERRE",
				150,
				false,
				10,
				new HashSet<LeasedZone>(),
				location2);
		location2.getLocationZones().add(lz2);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		maintenance.setId(MAINTENANCE_ID);
		event.getMaintenances().add(maintenance);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		maintenances.add(maintenance);
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				INVALID_ZONE_ID,
				ID,
				VALID_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				MAINTENANCE_ID,
				EVENT_ID,
				leasedZones);
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getMaintenanceReservedTickets(MAINTENANCE_ID)).thenReturn(tickets);
		when(maintenanceRepositoryMock.getMaintenancesForDate(LOCATION_ID, startDate, endDate)).thenReturn(maintenances);
		when(locationZoneServiceMock.findById(VALID_ZONE_ID)).thenReturn(lz);
		when(locationZoneServiceMock.findById(INVALID_ZONE_ID)).thenReturn(lz2);
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = InvalidPrice.class)
	public void testUpdate_InvalidPrice_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		LocationZone lz = new LocationZone(
				VALID_ZONE_ID,
				15,
				"PARTERRE",
				150,
				false,
				10,
				new HashSet<LeasedZone>(),
				location);
		location.getLocationZones().add(lz);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		maintenance.setId(MAINTENANCE_ID);
		event.getMaintenances().add(maintenance);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		maintenances.add(maintenance);
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				VALID_ZONE_ID,
				ID,
				INVALID_HIGH_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				MAINTENANCE_ID,
				EVENT_ID,
				leasedZones);
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getMaintenanceReservedTickets(MAINTENANCE_ID)).thenReturn(tickets);
		when(maintenanceRepositoryMock.getMaintenancesForDate(LOCATION_ID, startDate, endDate)).thenReturn(maintenances);
		when(locationZoneServiceMock.findById(VALID_ZONE_ID)).thenReturn(lz);
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = InvalidPrice.class)
	public void testUpdate_InvalidPrice_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		LocationZone lz = new LocationZone(
				VALID_ZONE_ID,
				15,
				"PARTERRE",
				150,
				false,
				10,
				new HashSet<LeasedZone>(),
				location);
		location.getLocationZones().add(lz);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(location);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		maintenance.setId(MAINTENANCE_ID);
		event.getMaintenances().add(maintenance);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<Maintenance> maintenances = new ArrayList<Maintenance>();
		maintenances.add(maintenance);
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				VALID_ZONE_ID,
				ID,
				INVALID_LOW_PRICE));
		
		MaintenanceDTO request = new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				MAINTENANCE_ID,
				EVENT_ID,
				leasedZones);
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getMaintenanceReservedTickets(MAINTENANCE_ID)).thenReturn(tickets);
		when(maintenanceRepositoryMock.getMaintenancesForDate(LOCATION_ID, startDate, endDate)).thenReturn(maintenances);
		when(locationZoneServiceMock.findById(VALID_ZONE_ID)).thenReturn(lz);
		
		maintenanceService.updateMaintenance(request);
	}
	
	@Test(expected = MaintenanceNotFound.class)
	public void test_remove_MaintenanceNotFound() throws ParseException{
		maintenanceService.remove(MAINTENANCE_ID);
	}
	
	@Test(expected = EventNotActive.class)
	public void test_remove_EventNotActive() throws ParseException{
		Event event = new Event();
		event.setId(EVENT_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setId(MAINTENANCE_ID);
		maintenance.setEvent(event);
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(false);
		
		maintenanceService.remove(MAINTENANCE_ID);
	}
	
	@Test(expected = MaintenanceNotChangeable.class)
	public void test_remove_MaintenanceNotChangeable() throws ParseException{
		Event event = new Event();
		event.setId(EVENT_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setId(MAINTENANCE_ID);
		maintenance.setEvent(event);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		tickets.add(new Ticket());
		
		when(maintenanceRepositoryMock.findById(MAINTENANCE_ID)).thenReturn(Optional.of(maintenance));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getMaintenanceReservedTickets(MAINTENANCE_ID)).thenReturn(tickets);
		
		maintenanceService.remove(MAINTENANCE_ID);
	}
}
