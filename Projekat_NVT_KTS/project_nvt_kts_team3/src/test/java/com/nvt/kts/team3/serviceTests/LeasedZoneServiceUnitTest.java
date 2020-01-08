package com.nvt.kts.team3.serviceTests;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.repository.LeasedZoneRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LeasedZoneService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.TicketService;

import exception.EventNotActive;
import exception.InvalidPrice;
import exception.LeasedZoneNotChangeable;
import exception.LeasedZoneNotFound;
import exception.LocationZoneNotAvailable;
import exception.LocationZoneNotFound;
import exception.MaintenanceNotFound;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LeasedZoneServiceUnitTest {

	@MockBean
	private LeasedZoneRepository leasedZoneRepositoryMock;

	@MockBean
	private MaintenanceService maintenanceServiceMock;

	@MockBean
	private EventService eventServiceMock;

	@MockBean
	private TicketService ticketServiceMock;

	@MockBean
	private LocationZoneService locationZoneServiceMock;
	
	@Autowired
	private LeasedZoneService leasedZoneService;
	
	private static final long ID = 0L;
	private static final long MAINTENANCE_ID = 127L;
	private static final long EVENT_ID = 126L;
	private static final long LOCATION_ZONE_ID = 125L;
	private static final long LOCATION_ID_1 = 123L;
	private static final long LOCATION_ID_2 = 122L;
	private static final long LEASED_ZONE_ID = 121L;
	
	private static final double VALID_PRICE = 30;
	private static final double INVALID_HIGH_PRICE = 11000;
	private static final double INVALID_LOW_PRICE = 0;
	
	@Test(expected = MaintenanceNotFound.class)
	public void testSave_OnMaintenanceNotFound() {
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(MAINTENANCE_ID);
		
		leasedZoneService.save(request);
	}
	
	@Test(expected = EventNotActive.class)
	public void testSave_OnEventNotActive() {
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(MAINTENANCE_ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setEvent(event);
		
		when(maintenanceServiceMock.findById(MAINTENANCE_ID)).thenReturn(maintenance);
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(false);
		
		leasedZoneService.save(request);
	}
	
	@Test(expected = LocationZoneNotFound.class)
	public void testSave_OnLocationZoneNotFound() {
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setMaintenanceId(MAINTENANCE_ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setEvent(event);
		
		when(maintenanceServiceMock.findById(MAINTENANCE_ID)).thenReturn(maintenance);
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(locationZoneServiceMock.findById(LOCATION_ZONE_ID)).thenReturn(null);
		
		leasedZoneService.save(request);
	}
	
	@Test(expected = LocationZoneNotAvailable.class)
	public void testSave_OnLocationZoneNotAvailable() {
		Event event = new Event();
		event.setId(EVENT_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setEvent(event);
		
		LocationZone lz = new LocationZone();
		lz.setId(LOCATION_ZONE_ID);
		
		Location location1 = new Location();
		location1.setId(LOCATION_ID_1);
		location1.getLocationZones().add(lz);
		lz.setLocation(location1);
		
		Location location2 = new Location();
		location1.setId(LOCATION_ID_2);
		
		event.setLocationInfo(location2);
		
		LeasedZoneDTO request = new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				MAINTENANCE_ID,
				VALID_PRICE);
		
		when(maintenanceServiceMock.findById(MAINTENANCE_ID)).thenReturn(maintenance);
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(locationZoneServiceMock.findById(LOCATION_ZONE_ID)).thenReturn(lz);
		
		leasedZoneService.save(request);
	}
	
	@Test(expected = InvalidPrice.class)
	public void testSave_OnInvalidPrice_1() {
		Event event = new Event();
		event.setId(EVENT_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setEvent(event);
		
		LocationZone lz = new LocationZone();
		lz.setId(LOCATION_ZONE_ID);
		
		Location location = new Location();
		location.setId(LOCATION_ID_1);
		location.getLocationZones().add(lz);
		lz.setLocation(location);
		
		event.setLocationInfo(location);
		
		LeasedZoneDTO request = new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				MAINTENANCE_ID,
				INVALID_HIGH_PRICE);
		
		when(maintenanceServiceMock.findById(MAINTENANCE_ID)).thenReturn(maintenance);
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(locationZoneServiceMock.findById(LOCATION_ZONE_ID)).thenReturn(lz);
		
		leasedZoneService.save(request);
	}
	
	@Test(expected = InvalidPrice.class)
	public void testSave_OnInvalidPrice_2() {
		Event event = new Event();
		event.setId(EVENT_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setEvent(event);
		
		LocationZone lz = new LocationZone();
		lz.setId(LOCATION_ZONE_ID);
		
		Location location = new Location();
		location.setId(LOCATION_ID_1);
		location.getLocationZones().add(lz);
		lz.setLocation(location);
		
		event.setLocationInfo(location);
		
		LeasedZoneDTO request = new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				MAINTENANCE_ID,
				INVALID_LOW_PRICE);
		
		when(maintenanceServiceMock.findById(MAINTENANCE_ID)).thenReturn(maintenance);
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(locationZoneServiceMock.findById(LOCATION_ZONE_ID)).thenReturn(lz);
		
		leasedZoneService.save(request);
	}
	
	@Test(expected = LeasedZoneNotFound.class)
	public void testUpdate_OnLeasedZoneNotFound() {
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(LEASED_ZONE_ID);
		
		leasedZoneService.update(request);
	}
	
	@Test(expected = EventNotActive.class)
	public void testUpdate_OnEventNotActive() {
		Event event = new Event();
		event.setId(EVENT_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setId(MAINTENANCE_ID);
		maintenance.setEvent(event);
		
		LeasedZone lz = new LeasedZone();
		lz.setId(LEASED_ZONE_ID);
		lz.setMaintenance(maintenance);
		
		LeasedZoneDTO request = new LeasedZoneDTO();
		request.setId(LEASED_ZONE_ID);
		request.setMaintenanceId(MAINTENANCE_ID);
		
		when(leasedZoneRepositoryMock.findById(LEASED_ZONE_ID)).thenReturn(Optional.of(lz));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(false);
		
		leasedZoneService.update(request);
	}
	
	@Test(expected = LocationZoneNotFound.class)
	public void testUpdate_OnLocationZoneNotFound() {
		Event event = new Event();
		event.setId(EVENT_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setId(MAINTENANCE_ID);
		maintenance.setEvent(event);
		
		LeasedZone lz = new LeasedZone();
		lz.setId(LEASED_ZONE_ID);
		lz.setMaintenance(maintenance);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		LeasedZoneDTO request = new LeasedZoneDTO(
				LEASED_ZONE_ID,
				LOCATION_ZONE_ID,
				MAINTENANCE_ID,
				VALID_PRICE);
		
		when(leasedZoneRepositoryMock.findById(LEASED_ZONE_ID)).thenReturn(Optional.of(lz));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getLeasedZoneReservedTickets(LEASED_ZONE_ID)).thenReturn(tickets);
		when(locationZoneServiceMock.findById(LOCATION_ZONE_ID)).thenReturn(null);
		
		leasedZoneService.update(request);
	}
	
	@Test(expected = LocationZoneNotAvailable.class)
	public void testUpdate_OnLocationZoneNotAvailable() {
		Event event = new Event();
		event.setId(EVENT_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setId(MAINTENANCE_ID);
		maintenance.setEvent(event);
		
		LeasedZone lz = new LeasedZone();
		lz.setId(LEASED_ZONE_ID);
		lz.setMaintenance(maintenance);
		
		LocationZone locationZone = new LocationZone();
		lz.setId(LOCATION_ZONE_ID);
		
		Location location1 = new Location();
		location1.setId(LOCATION_ID_1);
		location1.getLocationZones().add(locationZone);
		locationZone.setLocation(location1);
		
		Location location2 = new Location();
		location1.setId(LOCATION_ID_2);
		
		event.setLocationInfo(location2);
		
		LeasedZoneDTO request = new LeasedZoneDTO(
				LEASED_ZONE_ID,
				LOCATION_ZONE_ID,
				MAINTENANCE_ID,
				VALID_PRICE);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		when(leasedZoneRepositoryMock.findById(LEASED_ZONE_ID)).thenReturn(Optional.of(lz));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getLeasedZoneReservedTickets(LEASED_ZONE_ID)).thenReturn(tickets);
		when(locationZoneServiceMock.findById(LOCATION_ZONE_ID)).thenReturn(locationZone);
		
		leasedZoneService.update(request);
	}
	
	@Test(expected = LeasedZoneNotChangeable.class)
	public void testUpdate_OnLeasedZoneNotChangeable() {
		Event event = new Event();
		event.setId(EVENT_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setId(MAINTENANCE_ID);
		maintenance.setEvent(event);
		
		LeasedZone lz = new LeasedZone();
		lz.setId(LEASED_ZONE_ID);
		lz.setMaintenance(maintenance);
		
		LocationZone locationZone = new LocationZone();
		lz.setId(LOCATION_ZONE_ID);
		
		Location location = new Location();
		location.setId(LOCATION_ID_1);
		location.getLocationZones().add(locationZone);
		locationZone.setLocation(location);
		
		event.setLocationInfo(location);
		
		LeasedZoneDTO request = new LeasedZoneDTO(
				LEASED_ZONE_ID,
				LOCATION_ZONE_ID,
				MAINTENANCE_ID,
				VALID_PRICE);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		tickets.add(new Ticket());
		
		when(leasedZoneRepositoryMock.findById(LEASED_ZONE_ID)).thenReturn(Optional.of(lz));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getLeasedZoneReservedTickets(LEASED_ZONE_ID)).thenReturn(tickets);
		when(locationZoneServiceMock.findById(LOCATION_ZONE_ID)).thenReturn(locationZone);
		
		leasedZoneService.update(request);
	}
	
	@Test(expected = InvalidPrice.class)
	public void testUpdate_OnInvalidPrice_1() {
		Event event = new Event();
		event.setId(EVENT_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setId(MAINTENANCE_ID);
		maintenance.setEvent(event);
		
		LeasedZone lz = new LeasedZone();
		lz.setId(LEASED_ZONE_ID);
		lz.setMaintenance(maintenance);
		
		LocationZone locationZone = new LocationZone();
		lz.setId(LOCATION_ZONE_ID);
		
		Location location = new Location();
		location.setId(LOCATION_ID_1);
		location.getLocationZones().add(locationZone);
		locationZone.setLocation(location);
		
		event.setLocationInfo(location);
		
		LeasedZoneDTO request = new LeasedZoneDTO(
				LEASED_ZONE_ID,
				LOCATION_ZONE_ID,
				MAINTENANCE_ID,
				INVALID_HIGH_PRICE);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		when(leasedZoneRepositoryMock.findById(LEASED_ZONE_ID)).thenReturn(Optional.of(lz));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getLeasedZoneReservedTickets(LEASED_ZONE_ID)).thenReturn(tickets);
		when(locationZoneServiceMock.findById(LOCATION_ZONE_ID)).thenReturn(locationZone);
		
		leasedZoneService.update(request);
	}
	
	@Test(expected = InvalidPrice.class)
	public void testUpdate_OnInvalidPrice_2() {
		Event event = new Event();
		event.setId(EVENT_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setId(MAINTENANCE_ID);
		maintenance.setEvent(event);
		
		LeasedZone lz = new LeasedZone();
		lz.setId(LEASED_ZONE_ID);
		lz.setMaintenance(maintenance);
		
		LocationZone locationZone = new LocationZone();
		lz.setId(LOCATION_ZONE_ID);
		
		Location location = new Location();
		location.setId(LOCATION_ID_1);
		location.getLocationZones().add(locationZone);
		locationZone.setLocation(location);
		
		event.setLocationInfo(location);
		
		LeasedZoneDTO request = new LeasedZoneDTO(
				LEASED_ZONE_ID,
				LOCATION_ZONE_ID,
				MAINTENANCE_ID,
				INVALID_LOW_PRICE);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		when(leasedZoneRepositoryMock.findById(LEASED_ZONE_ID)).thenReturn(Optional.of(lz));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getLeasedZoneReservedTickets(LEASED_ZONE_ID)).thenReturn(tickets);
		when(locationZoneServiceMock.findById(LOCATION_ZONE_ID)).thenReturn(locationZone);
		
		leasedZoneService.update(request);
	}
	
	@Test(expected = LeasedZoneNotFound.class)
	public void test_remove_LeasedZoneNotFound() {
		leasedZoneService.remove(LEASED_ZONE_ID);
	}
	
	
	@Test(expected = EventNotActive.class)
	public void test_remove_EventNotActive() {
		LeasedZone lz = new LeasedZone();
		lz.setId(LEASED_ZONE_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setId(MAINTENANCE_ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		
		event.getMaintenances().add(maintenance);
		maintenance.setEvent(event);
		maintenance.getLeasedZones().add(lz);
		lz.setMaintenance(maintenance);
		
		when(leasedZoneRepositoryMock.findById(LEASED_ZONE_ID)).thenReturn(Optional.of(lz));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(false);
		
		leasedZoneService.remove(LEASED_ZONE_ID);
	}
	
	@Test(expected = LeasedZoneNotChangeable.class)
	public void test_remove_LeasedZoneNotChangeable() {
		LeasedZone lz = new LeasedZone();
		lz.setId(LEASED_ZONE_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setId(MAINTENANCE_ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		
		event.getMaintenances().add(maintenance);
		maintenance.setEvent(event);
		maintenance.getLeasedZones().add(lz);
		lz.setMaintenance(maintenance);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		tickets.add(new Ticket());
		
		when(leasedZoneRepositoryMock.findById(LEASED_ZONE_ID)).thenReturn(Optional.of(lz));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getLeasedZoneReservedTickets(LEASED_ZONE_ID)).thenReturn(tickets);
		
		leasedZoneService.remove(LEASED_ZONE_ID);
	}
	
	@Test
	public void test_remove_Success() {
		LeasedZone lz = new LeasedZone();
		lz.setId(LEASED_ZONE_ID);
		
		Maintenance maintenance = new Maintenance();
		maintenance.setId(MAINTENANCE_ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		
		event.getMaintenances().add(maintenance);
		maintenance.setEvent(event);
		maintenance.getLeasedZones().add(lz);
		lz.setMaintenance(maintenance);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		when(leasedZoneRepositoryMock.findById(LEASED_ZONE_ID)).thenReturn(Optional.of(lz));
		when(eventServiceMock.eventIsActive(EVENT_ID)).thenReturn(true);
		when(ticketServiceMock.getLeasedZoneReservedTickets(LEASED_ZONE_ID)).thenReturn(tickets);
		
		leasedZoneService.remove(LEASED_ZONE_ID);
	}
}

