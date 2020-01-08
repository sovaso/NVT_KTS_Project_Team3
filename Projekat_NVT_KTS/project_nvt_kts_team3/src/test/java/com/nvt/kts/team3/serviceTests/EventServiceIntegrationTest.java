package com.nvt.kts.team3.serviceTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.EventDTO;
import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.MaintenanceDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.service.EventService;

import exception.EventNotActive;
import exception.EventNotChangeable;
import exception.EventNotFound;
import exception.InvalidDate;
import exception.InvalidEventType;
import exception.InvalidLocationZone;
import exception.InvalidPrice;
import exception.LocationNotAvailable;
import exception.LocationNotChangeable;
import exception.LocationNotFound;
import exception.LocationZoneNotAvailable;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventServiceIntegrationTest {

	@Autowired
	private EventService eventService;
	
	private static final long NONEXISTENT_EVENT_ID = 10000L;
	private static final long INACTIVE_EVENT_ID = 4L;
	private static final long INACTIVE_LOCATION_ID = 3L;
	private static final long LOCATION_ID = 1L;
	private static final long VALID_EVENT_ID = 1L;
	private static final long LOCATION_ZONE_ID = 1L;
	private static final long INVALID_LOCATION_ZONE_ID = 2L;
	private static final long INACTIVE_LOCATION_EVENT_ID = 10L;
	private static final long ID = 0L;
	
	private static final String VALID_START_DATE = "2021-01-01 22:00";
	private static final String VALID_END_DATE = "2021-01-01 23:00";
	private static final String NOT_AVAILABLE_START_DATE = "2021-01-25 21:00";
	private static final String NOT_AVAILABLE_END_DATE = "2021-01-25 23:00";
	private static final String NOT_AVAILABLE_START_DATE_2 = "2021-01-15 21:00";
	private static final String NOT_AVAILABLE_END_DATE_2 = "2021-01-15 23:00";
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
	
	private static final String INVALID_EVENT_TYPE = "UNEXPECTED_TYPE";
	private static final String VALID_EVENT_TYPE = "SPORTS";
	

	@Test
	public void findAllSortedName_successfull() {
		List<Event> events = eventService.findAllSortedName();
		assertEquals(11, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals("Event1", events.get(1).getName());
		assertEquals("Event10", events.get(2).getName());
		assertEquals("Event11", events.get(3).getName());
		assertEquals("Event2", events.get(4).getName());
		assertEquals("Event3", events.get(5).getName());
		assertEquals("Event4", events.get(6).getName());
		assertEquals("Event5", events.get(7).getName());
		assertEquals("Event6", events.get(8).getName());
		assertEquals("Event8", events.get(9).getName());
		assertEquals("Event9", events.get(10).getName());
		
	}
	//ove metode srediti i za slucaj da je prazna baza
	@Test
	public void findAllSortedDateAcs_successfull() {
		List<Event> events = eventService.findAllSortedDateAcs();
		assertEquals(17, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals("Event8", events.get(1).getName());
		assertEquals("Event9", events.get(2).getName());
		assertEquals("Event3", events.get(3).getName());
		assertEquals("Event5", events.get(4).getName());
		assertEquals("Event8", events.get(5).getName());
		assertEquals("Event6", events.get(6).getName());
		assertEquals("Event3", events.get(7).getName());
		assertEquals("Event4", events.get(8).getName());
		assertEquals("Event1", events.get(9).getName());
		assertEquals("Event1", events.get(10).getName());
		assertEquals("Event1", events.get(11).getName());
		assertEquals("Event10", events.get(12).getName());
		assertEquals("Event11", events.get(13).getName());
		assertEquals("Event10", events.get(14).getName());
		assertEquals("Event1", events.get(15).getName());
		assertEquals("Event2", events.get(16).getName());
	}

	@Test
	public void findAllSortedDateDesc_successfull() {
		List<Event> events = eventService.findAllSortedDateDesc();
		assertEquals(17, events.size());
		assertEquals("Event2", events.get(0).getName());
		assertEquals("Event1", events.get(1).getName());
		assertEquals("Event10", events.get(2).getName());
		assertEquals("Event1", events.get(3).getName());
		assertEquals("Event10", events.get(4).getName());
		assertEquals("Event11", events.get(5).getName());
		assertEquals("Event1", events.get(6).getName());
		assertEquals("Event1", events.get(7).getName());
		assertEquals("Event4", events.get(8).getName());
		assertEquals("Event3", events.get(9).getName());
		assertEquals("Event6", events.get(10).getName());
		assertEquals("Event8", events.get(11).getName());
		assertEquals("Event5", events.get(12).getName());
		assertEquals("Event3", events.get(9).getName());
		assertEquals("Event6", events.get(10).getName());
		assertEquals("Event8", events.get(11).getName());
		assertEquals("Event5", events.get(12).getName());
	}

	@Test
	public void searchEvent_onlyField_eventNameGiven_successfull() {
		List<Event> events = eventService.searchEvent("Event2", "***", "***");
		assertEquals(1, events.size());
		assertEquals("Event2", events.get(0).getName());
		assertEquals(EventType.CULTURAL, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
	}

	@Test
	public void searchEvent_onlyField_eventNameGiven_successfullMoreThanOneResult() {
		List<Event> events = eventService.searchEvent("Event1", "***", "***");
		assertEquals(2, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
		assertEquals("Event1", events.get(1).getName());
		assertEquals(EventType.CULTURAL, events.get(1).getType());
		assertEquals(2, events.get(1).getLocationInfo().getId());
	}

	@Test
	public void searchEvent_onlyField_eventNameGiven_nonFound() {
		List<Event> events = eventService.searchEvent("someEventName", "***", "***");
		assertEquals(0, events.size());
	}

	@Test
	public void searchEvent_onlyField_eventTypeGiven_successfull() {
		List<Event> events = eventService.searchEvent("entertainment", "***", "***");
		assertEquals(2, events.size());
		assertEquals("Event3", events.get(0).getName());
		assertEquals(EventType.ENTERTAINMENT, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
		assertEquals("Event8", events.get(1).getName());
		assertEquals(EventType.ENTERTAINMENT, events.get(1).getType());
		assertEquals(1, events.get(1).getLocationInfo().getId());
	}

	@Test
	public void searchEvent_onlyField_addressGiven_successfull() {
		List<Event> events = eventService.searchEvent("Address4", "***", "***");
		assertEquals(1, events.size());
		assertEquals("Event5", events.get(0).getName());
		assertTrue(events.get(0).isStatus());
		assertEquals(EventType.CULTURAL, events.get(0).getType());
		assertEquals(4, events.get(0).getLocationInfo().getId());
	}

	@Test
	public void searchEvent_onlyField_dateGiven_successfull() {
		List<Event> events = eventService.searchEvent("***", "2021-01-15T00:00", "***");
		assertEquals(1, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());

	}

	@Test
	public void searchEvent_onlyField_dateGiven_unsuccessfull() {
		List<Event> events = eventService.searchEvent("***", "2010-01-15T00:00", "***");
		assertEquals(0, events.size());
	}

	@Test
	public void searchEvent_onlyField_dateGiven_successfullMoreThanOneResult() {
		List<Event> events = eventService.searchEvent("***", "2018-01-01T00:00", "***");
		assertEquals(2, events.size());
		assertEquals("Event8", events.get(0).getName());
		assertEquals(EventType.ENTERTAINMENT, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
		assertEquals("Event6", events.get(1).getName());
		assertEquals(EventType.SPORTS, events.get(1).getType());
		assertEquals(5, events.get(1).getLocationInfo().getId());
	}

	@Test
	public void searchEvent_period_successfull() {
		List<Event> events = eventService.searchEvent("***", "2021-01-15T00:00", "2021-12-15T00:00");
		assertEquals(2, events.size());

		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());

		assertEquals("Event2", events.get(1).getName());
		assertTrue(events.get(1).isStatus());
		assertEquals(EventType.CULTURAL, events.get(1).getType());
		assertEquals(1, events.get(1).getLocationInfo().getId());
	}

	@Test
	public void searchEvent_period_nonFound() {
		List<Event> events = eventService.searchEvent("***", "2010-01-15T00:00", "2010-12-15T00:00");
		assertEquals(0, events.size());
	}

	@Test
	public void searchEvent_periodAndType_unsuccessfull_noTypeInThatPeriod() {
		List<Event> events = eventService.searchEvent("ENTERTAINMENT", "2021-01-15T00:00", "2021-12-15T00:00");
		assertEquals(0, events.size());
	}

	@Test
	public void searchEvent_periodAndType_unsuccessfull_noAddressInThatPeriod() {
		List<Event> events = eventService.searchEvent("Address6", "2021-01-15T00:00", "2021-12-15T00:00");
		assertEquals(0, events.size());
	}

	@Test
	public void searchEvent_periodAndType_unsuccessfull_noEventInThatPeriod() {
		List<Event> events = eventService.searchEvent("Event7", "2021-01-15T00:00", "2021-12-15T00:00");
		assertEquals(0, events.size());
	}

	@Test
	public void searchEvent_periodAndType_unsuccessfull_eventExistsButNotInThatPeriod() {
		List<Event> events = eventService.searchEvent("Event1", "2010-01-15T00:00", "2010-12-15T00:00");
		assertEquals(0, events.size());
	}

	@Test
	public void searchEvent_periodAndType_unsuccessfull_addressExistsButNotInThatPeriod() {
		List<Event> events = eventService.searchEvent("Address1", "2010-01-15T00:00", "2010-12-15T00:00");
		assertEquals(0, events.size());
	}

	@Test
	public void searchEventPeriodAndType_unsuccessfull_typeoExistsButNotInThatPeriod() {
		List<Event> events = eventService.searchEvent("sports", "2010-01-15T00:00", "2010-12-15T00:00");
		assertEquals(0, events.size());
	}

	@Test
	public void searchEvent_periodAndType_successfull_eventNameGivenForPeriod() {
		List<Event> events = eventService.searchEvent("Event1", "2021-01-15T00:00", "2021-12-15T00:00");
		assertEquals(1, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
	}

	@Test
	public void searchEvent_periodAndType_successfull_eventTypeGivenForPeriod() {
		List<Event> events = eventService.searchEvent("SPORTS", "2021-01-15T00:00", "2021-12-15T00:00");
		assertEquals(1, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
	}

	@Test
	public void searchEvent_periodAndType_successfull_addressGivenForPeriod() {
		List<Event> events = eventService.searchEvent("Address1", "2021-01-15T00:00", "2021-12-15T00:00");
		assertEquals(2, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());

		assertEquals("Event2", events.get(1).getName());
		assertEquals(EventType.CULTURAL, events.get(1).getType());
		assertEquals(1, events.get(1).getLocationInfo().getId());
	}

	@Test
	@Transactional
	public void searchEvent_fieldSpecDate_unsuccessfull_noEventType() {
		List<Event> events = eventService.searchEvent("entertainment", "2021-01-15T01:00", "***");
		assertEquals(0, events.size());
	}

	@Test
	public void searchEvent_fieldSpecDate_unsuccessfull_noDate() {
		List<Event> events = eventService.searchEvent("sports", "2020-01-15T01:00", "***");
		assertEquals(0, events.size());
	}

	@Test
	public void searchEventSpecDate_successfull_typeGiven() {
		List<Event> events = eventService.searchEvent("sports", "2018-01-01T00:00", "***");
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}


	@Test
	public void searchEventSpecDate_successfull_addressGiven() {
		List<Event> events = eventService.searchEvent("address5", "2018-01-01T00:00", "***");
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}


	@Test
	public void searchEventSpecDate_successfull_eventNameGiven() {
		List<Event> events = eventService.searchEvent("event6", "2018-01-01T00:00", "***");
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}
	
	
	
	@Test(expected = InvalidEventType.class)
	public void test_create_InvalidEventType() throws ParseException{
		EventDTO request = new EventDTO();
		request.setEventType(INVALID_EVENT_TYPE);
		eventService.save(request);
	}
	
	@Test(expected = LocationNotFound.class)
	public void test_create_LocationNotFound() throws ParseException{
		EventDTO request = new EventDTO();
		request.setEventType(VALID_EVENT_TYPE);
		request.setId(INACTIVE_LOCATION_ID);
		eventService.save(request);
	}
	
	@Test(expected = InvalidDate.class)
	public void test_create_InvalidDate_1() throws ParseException{
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				MAINTENANCE_30MIN_START,
				MAINTENANCE_30MIN_END,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);

		eventService.save(request);
	}
	
	@Test(expected = InvalidDate.class)
	public void test_create_InvalidDate_2() throws ParseException{
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				MAINTENANCE_25H_START,
				MAINTENANCE_25H_END,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);

		eventService.save(request);
	}
	
	@Test(expected = InvalidDate.class)
	public void test_create_InvalidDate_3() throws ParseException{
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_END_DATE,
				VALID_START_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);

		eventService.save(request);
	}
	
	@Test(expected = InvalidDate.class)
	public void test_create_InvalidDate_4() throws ParseException{
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				EXPIERED_START_DATE,
				EXPIERED_END_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);

		eventService.save(request);
	}
	
	@Test(expected = ParseException.class)
	public void test_create_PaseException() throws ParseException{
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				BAD_FORMAT_START_DATE,
				BAD_FORMAT_END_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);

		eventService.save(request);
	}
	
	@Test(expected = LocationNotAvailable.class)
	public void test_create_LocationNotAvailable() throws ParseException{
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				NOT_AVAILABLE_START_DATE,
				NOT_AVAILABLE_END_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);

		eventService.save(request);
	}
	
	@Test(expected = InvalidLocationZone.class)
	public void test_create_InvalidLocationZone() throws ParseException{
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);

		eventService.save(request);
	}
	
	@Transactional
	@Test(expected = LocationZoneNotAvailable.class)
	public void test_create_LocationZoneNotAvailable() throws ParseException{
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				INVALID_LOCATION_ZONE_ID,
				ID,
				VALID_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				LOCATION_ID);

		eventService.save(request);
	}
	
	@Transactional
	@Test(expected = InvalidPrice.class)
	public void test_create_InvalidPrice_1() throws ParseException{
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				INVALID_LOW_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				LOCATION_ID);

		eventService.save(request);
	}
	
	@Transactional
	@Test(expected = InvalidPrice.class)
	public void test_create_InvalidPrice_2() throws ParseException{
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				LOCATION_ZONE_ID,
				ID,
				INVALID_HIGH_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				LOCATION_ID);

		eventService.save(request);
	}
	
	@Transactional
	@Test(expected = EventNotFound.class)
	public void test_update_EventNotFound() throws ParseException{
		EventDTO request = new EventDTO();
		request.setId(NONEXISTENT_EVENT_ID);

		eventService.update(request);
	}
	
	@Transactional
	@Test(expected = EventNotChangeable.class)
	public void test_update_EventNotChangeable() throws ParseException{
		EventDTO request = new EventDTO();
		request.setId(INACTIVE_EVENT_ID);

		eventService.update(request);
	}
	
	@Transactional
	@Test(expected = InvalidEventType.class)
	public void test_update_InvalidEventType() throws ParseException{
		EventDTO request = new EventDTO();
		request.setId(VALID_EVENT_ID);
		request.setEventType(INVALID_EVENT_TYPE);

		eventService.update(request);
	}
	
	@Transactional
	@Test(expected = LocationNotFound.class)
	public void test_update_LocationNotFound() throws ParseException{
		EventDTO request = new EventDTO();
		request.setId(INACTIVE_LOCATION_EVENT_ID);
		request.setEventType(VALID_EVENT_TYPE);

		eventService.update(request);
	}
	
	@Transactional
	@Test(expected = LocationNotChangeable.class)
	public void test_update_LocationNotChangeable() throws ParseException{
		EventDTO request = new EventDTO();
		request.setId(VALID_EVENT_ID);
		request.setEventType(VALID_EVENT_TYPE);
		request.setLocationId(2L);

		eventService.update(request);
	}
	
	@Transactional
	@Test(expected = InvalidDate.class)
	public void test_update_InvalidDate_1() throws ParseException{
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_END_DATE,
				VALID_START_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				1L);

		eventService.update(request);
	}
	
	@Transactional
	@Test(expected = InvalidDate.class)
	public void test_update_InvalidDate_2() throws ParseException{
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				EXPIERED_START_DATE,
				EXPIERED_END_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				1L);

		eventService.update(request);
	}
	
	@Transactional
	@Test(expected = InvalidDate.class)
	public void test_update_InvalidDate_3() throws ParseException{
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				MAINTENANCE_25H_START,
				MAINTENANCE_25H_END,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				1L);

		eventService.update(request);
	}
	
	@Transactional
	@Test(expected = InvalidDate.class)
	public void test_update_InvalidDate_4() throws ParseException{
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				MAINTENANCE_30MIN_START,
				MAINTENANCE_30MIN_END,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				1L);

		eventService.update(request);
	}
	
	@Transactional
	@Test(expected = ParseException.class)
	public void test_update_ParseException() throws ParseException{
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				BAD_FORMAT_START_DATE,
				BAD_FORMAT_END_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				1L);

		eventService.update(request);
	}
	
	@Transactional
	@Test(expected = LocationNotAvailable.class)
	public void test_update_LocationNotAvailable() throws ParseException{
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				NOT_AVAILABLE_START_DATE_2,
				NOT_AVAILABLE_END_DATE_2,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				1L);

		eventService.update(request);
	}
	
	@Transactional
	@Test(expected = InvalidLocationZone.class)
	public void test_update_InvalidLocationZone() throws ParseException{
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				new ArrayList<LeasedZoneDTO>()));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				1L);

		eventService.update(request);
	}
	
	@Transactional
	@Test(expected = LocationZoneNotAvailable.class)
	public void test_update_LocationZoneNotAvailable() throws ParseException{
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				2L,
				ID,
				VALID_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				1L);

		eventService.update(request);
	}
	
	@Transactional
	@Test(expected = InvalidPrice.class)
	public void test_update_InvalidPrice_1() throws ParseException{
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				1L,
				ID,
				INVALID_HIGH_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				1L);

		eventService.update(request);
	}
	
	@Transactional
	@Test(expected = InvalidPrice.class)
	public void test_update_InvalidPrice_2() throws ParseException{
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				1L,
				ID,
				INVALID_LOW_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				11L,
				"Event11",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				1L);

		eventService.update(request);
	}
	
	@Test
	@Transactional
	public void test_findById_Found(){
		Event event = eventService.findById(VALID_EVENT_ID);
		
		assertNotNull(event);
		assertEquals(event.getId(), VALID_EVENT_ID);
	}
	
	@Test
	public void test_findById_NotFound(){
		Event event = eventService.findById(NONEXISTENT_EVENT_ID);
		
		assertNull(event);
	}
	
	@Test
	public void test_eventIsActive_NotActive(){
		boolean active = eventService.eventIsActive(INACTIVE_EVENT_ID);
		
		assertFalse(active);
	}
	
	@Transactional
	public void test_eventIsActive_Active(){
		boolean active = eventService.eventIsActive(VALID_EVENT_ID);
		
		assertTrue(active);
	}
	
	@Transactional
	@Test(expected = EventNotFound.class)
	public void test_deleteEvent_EventNotFound(){
		eventService.remove(NONEXISTENT_EVENT_ID);
	}
	
	@Transactional
	@Test(expected = EventNotActive.class)
	public void test_deleteEvent_EventNotActive(){
		eventService.remove(INACTIVE_EVENT_ID);
	}
	
	@Transactional
	@Test(expected = EventNotChangeable.class)
	public void test_deleteEvent_EventNotChangeable(){
		eventService.remove(1L);
	}

}