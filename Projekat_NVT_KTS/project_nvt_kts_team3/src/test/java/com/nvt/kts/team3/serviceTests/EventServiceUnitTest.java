package com.nvt.kts.team3.serviceTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.time.Month;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.dto.EventDTO;
import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.MaintenanceDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.TicketService;

import exception.EventNotActive;
import exception.EventNotChangeable;
import exception.EventNotFound;
import exception.InvalidDate;
import exception.InvalidEventType;
import exception.InvalidPrice;
import exception.LocationNotAvailable;
import exception.LocationNotChangeable;
import exception.LocationNotFound;
import exception.LocationZoneNotAvailable;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class EventServiceUnitTest {
	
	@Autowired
	private EventService eventService;
	
	@MockBean
	private MaintenanceService maintenanceServiceMock;

	@MockBean
	private TicketService ticketServiceMock;

	@MockBean
	private LocationService locationServiceMock;

	@MockBean
	private LocationZoneService locationZoneServiceMock;

	@MockBean
	private ReservationRepository reservationRepository;

	@MockBean
	private EventRepository eventRepositoryMock;
	
	//private static final List<Event> events = new ArrayList<Event>();
	
	private static final Event e1 = new Event(1L, "Event1", true, EventType.SPORTS, null, null, new Location(1L), null, null);
	private static final Event e2 = new Event(2L, "Event1", true, EventType.CULTURAL, null, null, new Location(2L), null, null);
	private static final Event e3 = new Event(3L, "Event2", true, EventType.CULTURAL, null, null, new Location(1L), null, null);
	private static final Event e4 = new Event(4L, "Event3", false, EventType.ENTERTAINMENT, null, null, new Location(1L), null, null);
	private static final Event e5 = new Event(5L, "Event4", true, EventType.SPORTS, null, null, new Location(2L), null, null);
	private static final Event e6 = new Event(6L, "Event5", true, EventType.CULTURAL, null, null, new Location(4L), null, null);
	private static final Event e7 = new Event(7L, "Event6", false, EventType.SPORTS, null, null, new Location(5L), null, null);
	private static final Event e8 = new Event(8L, "Event8", true, EventType.ENTERTAINMENT, null, null, new Location(1L), null, null);
	private static final Event e9 = new Event(9L, "Event9", false, EventType.SPORTS, null, null, new Location(6L), null, null);

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	private static final long LOCATION_ID = 125L;
	private static final long EVENT_ID = 126L;
	private static final long ID = 0L;
	private static final long VALID_ZONE_ID = 11L;
	private static final long INVALID_ZONE_ID = 12L;
	
	private static final String VALID_EVENT_TYPE = "SPORTS";
	private static final String INVALID_EVENT_TYPE = "UNEXPECTED_TYPE";
	
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
	
	
	@Test
	public void findAllSortedName_successfull() {
		List<Event> eventsRepo = new ArrayList<Event>();
		eventsRepo.add(e1);
		eventsRepo.add(e2);
		eventsRepo.add(e3);
		eventsRepo.add(e4);
		eventsRepo.add(e5);
		eventsRepo.add(e6);
		eventsRepo.add(e7);
		eventsRepo.add(e8);
		eventsRepo.add(e9);
		when(eventRepositoryMock.findAll(new Sort(Sort.Direction.ASC, "name"))).thenReturn(eventsRepo);
		List<Event> events = eventService.findAllSortedName();
		assertEquals(9, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals("Event1", events.get(1).getName());
		assertEquals("Event2", events.get(2).getName());
		assertEquals("Event3", events.get(3).getName());
		assertEquals("Event4", events.get(4).getName());
		assertEquals("Event5", events.get(5).getName());
		assertEquals("Event6", events.get(6).getName());
		assertEquals("Event8", events.get(7).getName());
		assertEquals("Event9", events.get(8).getName());
	}
	
	@Test
	public void findAllSortedName_nonFound() {
		List<Event> eventsRepo = new ArrayList<Event>();
		when(eventRepositoryMock.findAll(new Sort(Sort.Direction.ASC, "name"))).thenReturn(eventsRepo);
		List<Event> events = eventService.findAllSortedName();
		assertEquals(0, events.size());
	}
	
	@Test
	public void findAllSortedDateAcs_nonFound() {
		ArrayList<Event> eventsRepo = new ArrayList<Event>();
		when(eventRepositoryMock.findAllSortedDateAcs()).thenReturn(eventsRepo);
		List<Event> events = eventService.findAllSortedDateAcs();
		assertEquals(0, events.size());
	}
	
	@Test
	public void findAllSortedDateAcs_successfull() {
		ArrayList<Event> eventsRepo = new ArrayList<Event>();
		eventsRepo.add(e8);
		
		eventsRepo.add(e7);
		
		eventsRepo.add(e9);
		
		eventsRepo.add(e4);
		
		eventsRepo.add(e6);
		
		eventsRepo.add(e8);
		
		eventsRepo.add(e7);
		
		eventsRepo.add(e4);
		
		eventsRepo.add(e5);
		
		eventsRepo.add(e1);
		
		eventsRepo.add(e1);
		
		eventsRepo.add(e2);
		
		eventsRepo.add(e3);
		
		when(eventRepositoryMock.findAllSortedDateAcs()).thenReturn(eventsRepo);
		List<Event> events = eventService.findAllSortedDateAcs();
		assertEquals(13, events.size());
		assertEquals("Event8", events.get(0).getName());
		assertEquals("Event6", events.get(1).getName());
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
		assertEquals("Event2", events.get(12).getName());
	}
	
	@Test
	public void findAllSortedDateDesc_successfull() {
		ArrayList<Event> eventsRepo = new ArrayList<Event>();
		eventsRepo.add(e3);
		eventsRepo.add(e2);
		eventsRepo.add(e1);
		eventsRepo.add(e1);
		eventsRepo.add(e5);
		eventsRepo.add(e4);
		eventsRepo.add(e7);
		eventsRepo.add(e8);
		eventsRepo.add(e6);
		eventsRepo.add(e4);
		eventsRepo.add(e9);
		eventsRepo.add(e7);
		eventsRepo.add(e8);
		when(eventRepositoryMock.findAllSortedDateDesc()).thenReturn(eventsRepo);
		List<Event> events = eventService.findAllSortedDateDesc();
		assertEquals(13, events.size());
		assertEquals("Event2", events.get(0).getName());
		assertEquals("Event1", events.get(1).getName());
		assertEquals("Event1", events.get(2).getName());
		assertEquals("Event1", events.get(3).getName());
		assertEquals("Event4", events.get(4).getName());
		assertEquals("Event3", events.get(5).getName());
		assertEquals("Event6", events.get(6).getName());
		assertEquals("Event8", events.get(7).getName());
		assertEquals("Event5", events.get(8).getName());
		assertEquals("Event3", events.get(9).getName());
		assertEquals("Event9", events.get(10).getName());
		assertEquals("Event6", events.get(11).getName());
		assertEquals("Event8", events.get(12).getName());
	}
	
	@Test
	public void searchEvent_onlyField_eventNameGiven_successfull() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e3);
		when(eventRepositoryMock.searchEventOnlyField("Event2")).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("Event2", "***", "***");
		assertEquals(1, events.size());
		assertEquals("Event2", events.get(0).getName());
		assertEquals(EventType.CULTURAL, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
	}
	
	@Test
	public void searchEvent_onlyField_eventNameGiven_successfullMoreThanOneResult() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e1);
		foundEvents.add(e2);
		when(eventRepositoryMock.searchEventOnlyField("Event1")).thenReturn(foundEvents);
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
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		when(eventRepositoryMock.searchEventOnlyField("Event1")).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("someEventName", "***", "***");
		assertEquals(0, events.size());
	}
	
	@Test
	public void searchEvent_onlyField_eventTypeGiven_successfull() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e4);
		foundEvents.add(e8);
		when(eventRepositoryMock.searchEventOnlyField("entertainment")).thenReturn(foundEvents);
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
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e6);
		when(eventRepositoryMock.searchEventOnlyField("Address4")).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("Address4", "***", "***");
		assertEquals(1, events.size());
		assertEquals("Event5", events.get(0).getName());
		assertTrue(events.get(0).isStatus());
		assertEquals(EventType.CULTURAL, events.get(0).getType());
		assertEquals(4, events.get(0).getLocationInfo().getId());
	}
	
	@Test
	public void searchEvent_onlyField_dateGiven_successfull() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e1);
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		when(eventRepositoryMock.searchEventSpecDate(startDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("***", "2020-01-15T00:00", "***");
		assertEquals(1, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
		
	}
	
	@Test
	public void searchEvent_onlyField_dateGiven_unsuccessfull() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		when(eventRepositoryMock.searchEventSpecDate(startDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("***", "2010-01-15T00:00", "***");
		assertEquals(0, events.size());
	}
	
	@Test
	public void searchEvent_onlyField_dateGiven_successfullMoreThanOneResult() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e8);
		foundEvents.add(e7);
		LocalDateTime startDate = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0);
		startDate=startDate.plusHours(1);
		when(eventRepositoryMock.searchEventSpecDate(startDate)).thenReturn(foundEvents);
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
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e1);
		foundEvents.add(e3);
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		endDate=endDate.plusHours(1);
		when(eventRepositoryMock.searchEventPeriod(startDate, endDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("***", "2020-01-15T00:00", "2020-12-15T00:00");
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
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		LocalDateTime startDate = LocalDateTime.of(2010, Month.JANUARY, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		LocalDateTime endDate = LocalDateTime.of(2010, Month.DECEMBER, 15, 0, 0, 0);
		endDate=endDate.plusHours(1);
		when(eventRepositoryMock.searchEventPeriod(startDate, endDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("***", "2010-01-15T00:00", "2010-12-15T00:00");
		assertEquals(0, events.size());
	}
	
	@Test
	public void searchEvent_periodAndType_unsuccessfull_noTypeInThatPeriod() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		endDate=endDate.plusHours(1);
		when(eventRepositoryMock.searchEventFieldPeriod("ENTERTAINMENT", startDate, endDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("ENTERTAINMENT", "2020-01-15T00:00", "2020-12-15T00:00");
		assertEquals(0, events.size());
	}
	
	@Test
	public void searchEvent_periodAndType_unsuccessfull_noAddressInThatPeriod() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		endDate=endDate.plusHours(1);
		when(eventRepositoryMock.searchEventFieldPeriod("Address6", startDate, endDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("Address6", "2020-01-15T00:00", "2020-12-15T00:00");
		assertEquals(0, events.size());
	}
	
	@Test
	public void searchEvent_periodAndType_unsuccessfull_noEventInThatPeriod() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		endDate=endDate.plusHours(1);
		when(eventRepositoryMock.searchEventFieldPeriod("Event7", startDate, endDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("Event7", "2020-01-15T00:00", "2020-12-15T00:00");
		assertEquals(0, events.size());
	}
	
	@Test
	public void searchEvent_periodAndType_unsuccessfull_eventExistsButNotInThatPeriod() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		LocalDateTime startDate = LocalDateTime.of(2010, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2010, Month.DECEMBER, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		endDate=endDate.plusHours(1);
		when(eventRepositoryMock.searchEventFieldPeriod("Event1", startDate, endDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("Event1", "2010-01-15T00:00", "2010-12-15T00:00");
		assertEquals(0, events.size());
	}
	
	@Test
	public void searchEvent_periodAndType_unsuccessfull_addressExistsButNotInThatPeriod() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		LocalDateTime startDate = LocalDateTime.of(2010, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2010, Month.DECEMBER, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		endDate=endDate.plusHours(1);
		when(eventRepositoryMock.searchEventFieldPeriod("Address1", startDate, endDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("Address1", "2010-01-15T00:00", "2010-12-15T00:00");
		assertEquals(0, events.size());
	}
	
	@Test
	public void searchEventPeriodAndType_unsuccessfull_typeoExistsButNotInThatPeriod() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		LocalDateTime startDate = LocalDateTime.of(2010, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2010, Month.DECEMBER, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		endDate=endDate.plusHours(1);
		when(eventRepositoryMock.searchEventFieldPeriod("sports", startDate, endDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("sports", "2010-01-15T00:00", "2010-12-15T00:00");
		assertEquals(0, events.size());
	}
	
	@Test
	public void searchEvent_periodAndType_successfull_eventNameGivenForPeriod() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e1);
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		endDate=endDate.plusHours(1);
		when(eventRepositoryMock.searchEventFieldPeriod("Event1", startDate, endDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("Event1", "2020-01-15T00:00", "2020-12-15T00:00");
		assertEquals(1, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
	}
	
	@Test
	public void searchEvent_periodAndType_successfull_eventTypeGivenForPeriod() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e1);
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		endDate=endDate.plusHours(1);
		when(eventRepositoryMock.searchEventFieldPeriod("SPORTS", startDate, endDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("SPORTS", "2020-01-15T00:00", "2020-12-15T00:00");
		assertEquals(1, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
	}
	
	@Test
	public void searchEvent_periodAndType_successfull_addressGivenForPeriod() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e1);
		foundEvents.add(e3);
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		endDate=endDate.plusHours(1);
		when(eventRepositoryMock.searchEventFieldPeriod("Address1", startDate, endDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("Address1", "2020-01-15T00:00", "2020-12-15T00:00");
		assertEquals(2, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
		
		assertEquals("Event2", events.get(1).getName());
		assertEquals(EventType.CULTURAL, events.get(1).getType());
		assertEquals(1, events.get(1).getLocationInfo().getId());
	}
		
		
	@Test
	public void searchEvent_fieldSpecDate_unsuccessfull_noEventType() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		when(eventRepositoryMock.searchEventFieldSpecDate("entertainment", startDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("entertainment", "2020-01-15T01:00", "***");
		assertEquals(0, events.size());
	}
	
	@Test
	public void searchEvent_fieldSpecDate_unsuccessfull_noDate() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		when(eventRepositoryMock.searchEventFieldSpecDate("sports", startDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("sports", "2020-01-15T01:00", "***");
		assertEquals(0, events.size());
	}
	
	@Test
	public void searchEventSpecDate_successfull_typeGiven() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e7);
		LocalDateTime startDate = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0);
		startDate=startDate.plusHours(1);
		when(eventRepositoryMock.searchEventFieldSpecDate("sports", startDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("sports", "2018-01-01T00:00", "***");
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}
	
	
	@Test
	public void searchEventSpecDate_successfull_addressGiven() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e7);
		LocalDateTime startDate = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0);
		startDate=startDate.plusHours(1);
		when(eventRepositoryMock.searchEventFieldSpecDate("address5", startDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("address5", "2018-01-01T00:00", "***");
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}
	
	
	@Test
	public void searchEventSpecDate_successfull_eventNameGiven() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e7);
		LocalDateTime startDate = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0);
		startDate=startDate.plusHours(1);
		when(eventRepositoryMock.searchEventFieldSpecDate("event6", startDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("event6", "2018-01-01T00:00", "***");
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}
	
	
		
		
		
		
		
		
		
		
		
		
		
		
		
	@Test
	public void testEventIsActive_ExpieredEvent() throws ParseException {
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
		
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		
		boolean active = eventService.eventIsActive(EVENT_ID);
		
		assertFalse(active);
	}
	
	@Test
	public void testEventIsActive_InactiveEvent() throws ParseException {
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
		
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		
		boolean active = eventService.eventIsActive(EVENT_ID);
		
		assertFalse(active);
	}
	
	@Test
	public void testEventIsActive_True() throws ParseException {
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
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
		
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		
		boolean active = eventService.eventIsActive(EVENT_ID);
		
		assertTrue(active);
	}
	
	@Test(expected = InvalidEventType.class)
	public void testCreate_InvalidEventType() throws ParseException{
		EventDTO request = new EventDTO();
		request.setEventType(INVALID_EVENT_TYPE);
		eventService.save(request);
	}
	
	@Test(expected = LocationNotFound.class)
	public void testCreateEvent_LocationNotFound_1() throws ParseException{
		EventDTO request = new EventDTO();
		request.setEventType("SPORTS");
		request.setLocationId(LOCATION_ID);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(null);
		eventService.save(request);
	}
	
	@Test(expected = LocationNotFound.class)
	public void testCreateEvent_LocationNotFound_2() throws ParseException{
		EventDTO request = new EventDTO();
		request.setEventType("SPORTS");
		request.setLocationId(LOCATION_ID);
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(false);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		eventService.save(request);
	}
	
	/* Testiramo da li se moze kreirati Event sa datumom koji je pre sadasnjeg trenutka */
	@Test(expected = InvalidDate.class)
	public void testCreateEvent_InvalidDate_1() throws ParseException{
		Location location = new Location(
				LOCATION_ID,
				"Location1",
				"Address1",
				"description",
				true,
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				EXPIERED_START_DATE,
				EXPIERED_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);
		
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		
		eventService.save(request);
	}
	
	/* Testiramo da li se moze kreirati Event sa izvodjenjem koje traje vise od 24h */
	@Test(expected = InvalidDate.class)
	public void testCreateEvent_InvalidDate_2() throws ParseException{
		Location location = new Location(
				LOCATION_ID,
				"Location1",
				"Address1",
				"description",
				true,
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				MAINTENANCE_25H_START,
				MAINTENANCE_25H_END,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);
		
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		
		eventService.save(request);
	}
	
	/* Testiramo da li se moze kreirati Event sa izvodjenjem koje traje manje od 30min */
	@Test(expected = InvalidDate.class)
	public void testCreateEvent_InvalidDate_3() throws ParseException{
		Location location = new Location(
				LOCATION_ID,
				"Location1",
				"Address1",
				"description",
				true,
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				MAINTENANCE_30MIN_START,
				MAINTENANCE_30MIN_END,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);
		
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		
		eventService.save(request);
	}
	
	/* Testiramo da li se moze kreirati Event sa izvodjenjem ciji je kraj pre pocetka */
	@Test(expected = InvalidDate.class)
	public void testCreateEvent_InvalidDate_4() throws ParseException{
		Location location = new Location(
				LOCATION_ID,
				"Location1",
				"Address1",
				"description",
				true,
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_END_DATE,
				VALID_START_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);
		
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		
		eventService.save(request);
	}
	
	/* Testiramo kako reaguje aplikacija kada se prosledi nevalidan format datuma */
	@Test(expected = ParseException.class)
	public void testCreateEvent_ParseException() throws ParseException{
		Location location = new Location(
				LOCATION_ID,
				"Location1",
				"Address1",
				"description",
				true,
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				BAD_FORMAT_START_DATE,
				BAD_FORMAT_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);
		
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		
		eventService.save(request);
	}
	
	/* Testiramo kako reaguje aplikacija kada zelimo da dodamo Event u zauzetom terminu neke lokacije */
	@Test(expected = LocationNotAvailable.class)
	public void testCreateEvent_LocationNotAvailable() throws ParseException{
		Location location = new Location(
				LOCATION_ID,
				"Location1",
				"Address1",
				"description",
				true,
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		Event event = new Event();
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		locationEvents.add(event);
		
		EventDTO request = new EventDTO(
				ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);
		
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		
		eventService.save(request);
		
	}
	
	/* Testiramo kako reaguje aplikacija kada nevalidne zone lokacije,
	*  odnosno zone koje ne postoje ili ne pripadaju prosledjenoj lokaciji  */
	@Test(expected = LocationZoneNotAvailable.class)
	public void testCreateEvent_LocationZoneNotAvailable_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1",
				"Address1",
				"description",
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		LocationZone lz = new LocationZone(VALID_ZONE_ID,
				15,
				"PARTERRE",
				150,
				false,
				10,
				new HashSet<LeasedZone>(),
				location);
		location.getLocationZones().add(lz);
		
		Location location2 = new Location(
				556L,
				"Location2",
				"Address2",
				"description",
				true,
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		LocationZone lz2 = new LocationZone(INVALID_ZONE_ID,
				15,
				"PARTERRE",
				150,
				false,
				10,
				new HashSet<LeasedZone>(),
				location2);
		location2.getLocationZones().add(lz2);
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				INVALID_ZONE_ID,
				ID,
				VALID_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				LOCATION_ID);
		
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(locationZoneServiceMock.findById(INVALID_ZONE_ID)).thenReturn(lz2);
		
		eventService.save(request);
	}
	
	@Test(expected = LocationZoneNotAvailable.class)
	public void testCreateEvent_LocationZoneNotAvailable_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		
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
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(ID,
				INVALID_ZONE_ID,
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
		
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(locationZoneServiceMock.findById(INVALID_ZONE_ID)).thenReturn(null);
		
		eventService.save(request);
	}
	
	@Test(expected = InvalidPrice.class)
	public void testCreateEvent_InvalidPrice_1() throws ParseException {
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		
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
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				VALID_ZONE_ID,
				ID,
				INVALID_HIGH_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				LOCATION_ID);
		
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(locationZoneServiceMock.findById(VALID_ZONE_ID)).thenReturn(lz);
		
		eventService.save(request);
	}
	
	@Test(expected = InvalidPrice.class)
	public void testCreateEvent_InvalidPrice_2() throws ParseException {
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		
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
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				VALID_ZONE_ID,
				ID,
				INVALID_LOW_PRICE));
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones,
				LOCATION_ID);
		
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(locationZoneServiceMock.findById(VALID_ZONE_ID)).thenReturn(lz);
		
		eventService.save(request);
	}
	
	@Test(expected = EventNotFound.class)
	public void testUpdate_EventNotFound() throws ParseException{
		EventDTO request = new EventDTO();
		request.setId(EVENT_ID);
		
		eventService.update(request);
	}
	
	@Test(expected = EventNotChangeable.class)
	public void testUpdate_EventNotChangeable_1() throws ParseException{
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
		
		EventDTO request = new EventDTO();
		request.setId(EVENT_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		
		eventService.update(request);
	}
	
	@Test(expected = EventNotChangeable.class)
	public void testUpdate_EventNotChangeable_2() throws ParseException{
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
		
		EventDTO request = new EventDTO();
		request.setId(EVENT_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		
		eventService.update(request);
	}
	
	@Test(expected = InvalidEventType.class)
	public void testUpdate_InvalidEventType() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
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
		
		EventDTO request = new EventDTO();
		request.setId(EVENT_ID);
		request.setEventType(INVALID_EVENT_TYPE);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		
		eventService.update(request);
	}
	
	@Test(expected = LocationNotFound.class)
	public void testUpdateEvent_LocationNotFound_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location oldLocation = new Location();
		oldLocation.setId(ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(oldLocation);
		
		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		EventDTO request = new EventDTO();
		request.setId(EVENT_ID);
		request.setEventType(VALID_EVENT_TYPE);
		request.setLocationId(LOCATION_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(null);
		
		eventService.update(request);
	}
	
	@Test(expected = LocationNotFound.class)
	public void testUpdateEvent_LocationNotFound_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location oldLocation = new Location();
		oldLocation.setId(ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(oldLocation);
		
		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		Location location = new Location();
		location.setId(LOCATION_ID);
		location.setStatus(false);
		
		EventDTO request = new EventDTO();
		request.setId(EVENT_ID);
		request.setEventType(VALID_EVENT_TYPE);
		request.setLocationId(LOCATION_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		
		eventService.update(request);
	}
	
	@Test(expected = LocationNotChangeable.class)
	public void testUpdateEvent_LocationNotChangeable() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location oldLocation = new Location();
		oldLocation.setId(ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(oldLocation);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		tickets.add(new Ticket());
		
		EventDTO request = new EventDTO();
		request.setId(EVENT_ID);
		request.setEventType(VALID_EVENT_TYPE);
		request.setLocationId(LOCATION_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(ticketServiceMock.getEventReservedTickets(EVENT_ID)).thenReturn(tickets);
		
		eventService.update(request);
	}
	
	/* Testiramo da li se moze kreirati Event sa datumom koji je pre sadasnjeg trenutka */
	@Test(expected = InvalidDate.class)
	public void testUpdateEvent_InvalidDate_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location oldLocation = new Location();
		oldLocation.setId(ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(oldLocation);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				EXPIERED_START_DATE, 
				EXPIERED_END_DATE, 
				ID, 
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				EVENT_ID, 
				"Event1", 
				"description", 
				VALID_EVENT_TYPE,
				maintenances,
				leasedZones, 
				LOCATION_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(ticketServiceMock.getEventReservedTickets(EVENT_ID)).thenReturn(tickets);
		
		eventService.update(request);
	}
	
	/* Testiramo da li se moze kreirati Event sa izvodjenjem koje traje vise od 24h */
	@Test(expected = InvalidDate.class)
	public void testUpdateEvent_InvalidDate_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location oldLocation = new Location();
		oldLocation.setId(ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(oldLocation);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();

		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				MAINTENANCE_25H_START, 
				MAINTENANCE_25H_END, 
				ID, 
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				EVENT_ID, 
				"Event1", 
				"description", 
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(), 
				LOCATION_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(ticketServiceMock.getEventReservedTickets(EVENT_ID)).thenReturn(tickets);
		
		eventService.update(request);
	}
	
	/* Testiramo da li se moze kreirati Event sa izvodjenjem koje traje manje od 30min */
	@Test(expected = InvalidDate.class)
	public void testUpdateEvent_InvalidDate_3() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location oldLocation = new Location();
		oldLocation.setId(ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(oldLocation);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();

		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				MAINTENANCE_30MIN_START, 
				MAINTENANCE_30MIN_END, 
				ID, 
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				EVENT_ID, 
				"Event1", 
				"description", 
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(), 
				LOCATION_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(ticketServiceMock.getEventReservedTickets(EVENT_ID)).thenReturn(tickets);
		
		eventService.update(request);
	}
	
	/* Testiramo da li se moze kreirati Event sa izvodjenjem ciji je kraj pre pocetka */
	@Test(expected = InvalidDate.class)
	public void testUpdateEvent_InvalidDate_4() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location oldLocation = new Location();
		oldLocation.setId(ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(oldLocation);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();

		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_END_DATE, 
				VALID_START_DATE, 
				ID, 
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				EVENT_ID, 
				"Event1", 
				"description", 
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(), 
				LOCATION_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(ticketServiceMock.getEventReservedTickets(EVENT_ID)).thenReturn(tickets);
		
		eventService.update(request);
	}
	
	/* Testiramo kako reaguje aplikacija kada se prosledi nevalidan format datuma */
	@Test(expected = ParseException.class)
	public void testUpdateEvent_ParseException() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location oldLocation = new Location();
		oldLocation.setId(ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(oldLocation);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();

		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				BAD_FORMAT_START_DATE, 
				BAD_FORMAT_END_DATE, 
				ID, 
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				EVENT_ID, 
				"Event1", 
				"description", 
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(), 
				LOCATION_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(ticketServiceMock.getEventReservedTickets(EVENT_ID)).thenReturn(tickets);
		
		eventService.update(request);
	}
	
	/* Testiramo kako reaguje aplikacija kada zelimo da dodamo Event u zauzetom terminu neke lokacije */
	@Test(expected = LocationNotAvailable.class)
	public void testUpdateEvent_LocationNotAvailable() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location oldLocation = new Location();
		oldLocation.setId(ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(oldLocation);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		Location location = new Location(
				LOCATION_ID,
				"Location1", 
				"Address1", 
				"description", 
				true, 
				new HashSet<Event>(),
				new HashSet<LocationZone>());
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		Event event2 = new Event();
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		locationEvents.add(event2);
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		
		ArrayList<MaintenanceDTO> maintenances = new ArrayList<MaintenanceDTO>();
		maintenances.add(new MaintenanceDTO(
				VALID_START_DATE,
				VALID_END_DATE,
				ID,
				ID,
				leasedZones));
		
		EventDTO request = new EventDTO(
				EVENT_ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(ticketServiceMock.getEventReservedTickets(EVENT_ID)).thenReturn(tickets);
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate))
			.thenReturn(locationEvents);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		
		eventService.update(request);
	}
	
	/* Testiramo kako reaguje aplikacija kada nevalidne zone lokacije,
	*  odnosno zone koje ne postoje ili ne pripadaju prosledjenoj lokaciji  */
	@Test(expected = LocationZoneNotAvailable.class)
	public void testUpdateEvent_LocationZoneNotAvailable_1() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location oldLocation = new Location();
		oldLocation.setId(ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(oldLocation);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
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
				INVALID_ZONE_ID,
				15,
				"PARTERRE",
				150,
				false,
				10,
				new HashSet<LeasedZone>(),
				location2);
		location2.getLocationZones().add(lz2);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				INVALID_ZONE_ID,
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
				EVENT_ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		when(ticketServiceMock.getEventReservedTickets(EVENT_ID)).thenReturn(tickets);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(locationZoneServiceMock.findById(INVALID_ZONE_ID)).thenReturn(lz2);
		
		eventService.update(request);
	}
	
	@Test(expected = LocationZoneNotAvailable.class)
	public void testUpdateEvent_LocationZoneNotAvailable_2() throws ParseException{
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location oldLocation = new Location();
		oldLocation.setId(ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(oldLocation);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
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
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				INVALID_ZONE_ID,
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
				EVENT_ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		when(ticketServiceMock.getEventReservedTickets(EVENT_ID)).thenReturn(tickets);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(locationZoneServiceMock.findById(INVALID_ZONE_ID)).thenReturn(null);
		
		eventService.update(request);
	}
	
	@Test(expected = InvalidPrice.class)
	public void testUpdateEvent_InvalidPrice_1() throws ParseException {
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location oldLocation = new Location();
		oldLocation.setId(ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(oldLocation);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
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
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				VALID_ZONE_ID,
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
				EVENT_ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		when(ticketServiceMock.getEventReservedTickets(EVENT_ID)).thenReturn(tickets);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(locationZoneServiceMock.findById(VALID_ZONE_ID)).thenReturn(lz);
		
		eventService.update(request);
	}
	
	@Test(expected = InvalidPrice.class)
	public void testUpdateEvent_InvalidPrice_2() throws ParseException {
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Location oldLocation = new Location();
		oldLocation.setId(ID);
		
		Event event = new Event();
		event.setId(EVENT_ID);
		event.setStatus(true);
		event.setLocationInfo(oldLocation);

		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
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
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		ArrayList<Event> locationEvents = new ArrayList<Event>();
		
		ArrayList<LeasedZoneDTO> leasedZones = new ArrayList<LeasedZoneDTO>();
		leasedZones.add(new LeasedZoneDTO(
				ID,
				VALID_ZONE_ID,
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
				EVENT_ID,
				"Event1",
				"description",
				VALID_EVENT_TYPE,
				maintenances,
				new ArrayList<LeasedZoneDTO>(),
				LOCATION_ID);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(locationServiceMock.checkIfAvailable(LOCATION_ID, startDate, endDate)).thenReturn(locationEvents);
		when(ticketServiceMock.getEventReservedTickets(EVENT_ID)).thenReturn(tickets);
		when(locationServiceMock.findById(LOCATION_ID)).thenReturn(location);
		when(locationZoneServiceMock.findById(VALID_ZONE_ID)).thenReturn(lz);
		
		eventService.update(request);
	}
	
	@Test(expected = EventNotFound.class)
	public void testRemove_OnEventNotFound() {
		eventService.remove(EVENT_ID);
	}
	
	@Test(expected = EventNotActive.class)
	public void testRemove_OnEventNotActive() throws ParseException {
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(EXPIERED_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(EXPIERED_END_DATE)),df);
		LocalDateTime expiryDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_EXPIRY_DATE)),df);
		
		Event event = new Event();
		event.setStatus(false);
		event.setId(EVENT_ID);
		
		Maintenance maintenance = new Maintenance(
				startDate,
				endDate,
				expiryDate,
				new HashSet<LeasedZone>(),
				event);
		event.getMaintenances().add(maintenance);
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);

		eventService.remove(EVENT_ID);
	}
	
	@Test(expected = EventNotChangeable.class)
	public void testRemove_EventNotChangeable() throws ParseException {
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
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
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		tickets.add(new Ticket());
		
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(ticketServiceMock.getEventReservedTickets(EVENT_ID)).thenReturn(tickets);
		
		eventService.remove(EVENT_ID);
	}
	
	@Test
	public void testRemove_Success_1() throws ParseException {
		LocalDateTime startDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_START_DATE)),df);
		LocalDateTime endDate = LocalDateTime.parse(sdf.format(sdf.parse(VALID_END_DATE)),df);
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
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(ticketServiceMock.getEventReservedTickets(EVENT_ID)).thenReturn(tickets);
		
		eventService.remove(EVENT_ID);
	}
	
	@Test
	public void testRemove_Success_2() throws ParseException {
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
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		tickets.add(new Ticket());
		
		when(maintenanceServiceMock.getLastMaintenanceOfEvent(EVENT_ID)).thenReturn(maintenance);
		when(eventRepositoryMock.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(ticketServiceMock.getEventReservedTickets(EVENT_ID)).thenReturn(tickets);
		
		eventService.remove(EVENT_ID);
	}
	
	@Test
	public void testUploadFile() {
		// TODO
	}
	
	@Test
	public void testGetEventIncome() {
		// TODO
	}
	
	@Test
	public void testGetEventReport() {
		// TODO
	}	
}
