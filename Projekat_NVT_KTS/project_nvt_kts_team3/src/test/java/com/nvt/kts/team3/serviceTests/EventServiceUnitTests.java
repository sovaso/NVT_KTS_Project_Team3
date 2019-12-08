package com.nvt.kts.team3.serviceTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.TicketService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class EventServiceUnitTests {
	@Autowired
	private EventService eventService;
	
	@MockBean
	private MaintenanceService maintenanceService;

	@MockBean
	private TicketService ticketService;

	@MockBean
	private LocationService locationService;

	@MockBean
	private LocationZoneService locationZoneService;

	@MockBean
	private ReservationRepository reservationRepository;

	@MockBean
	private EventRepository eventRepository;
	
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
		when(eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"))).thenReturn(eventsRepo);
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
		when(eventRepository.findAll(new Sort(Sort.Direction.ASC, "name"))).thenReturn(eventsRepo);
		List<Event> events = eventService.findAllSortedName();
		assertEquals(0, events.size());
	}
	
	@Test
	public void findAllSortedDateAcs_nonFound() {
		ArrayList<Event> eventsRepo = new ArrayList<Event>();
		when(eventRepository.findAllSortedDateAcs()).thenReturn(eventsRepo);
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
		
		when(eventRepository.findAllSortedDateAcs()).thenReturn(eventsRepo);
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
		when(eventRepository.findAllSortedDateDesc()).thenReturn(eventsRepo);
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
		when(eventRepository.searchEventOnlyField("Event2")).thenReturn(foundEvents);
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
		when(eventRepository.searchEventOnlyField("Event1")).thenReturn(foundEvents);
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
		when(eventRepository.searchEventOnlyField("Event1")).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("someEventName", "***", "***");
		assertEquals(0, events.size());
	}
	
	@Test
	public void searchEvent_onlyField_eventTypeGiven_successfull() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e4);
		foundEvents.add(e8);
		when(eventRepository.searchEventOnlyField("entertainment")).thenReturn(foundEvents);
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
		when(eventRepository.searchEventOnlyField("Address4")).thenReturn(foundEvents);
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
		when(eventRepository.searchEventSpecDate(startDate)).thenReturn(foundEvents);
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
		when(eventRepository.searchEventSpecDate(startDate)).thenReturn(foundEvents);
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
		when(eventRepository.searchEventSpecDate(startDate)).thenReturn(foundEvents);
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
		when(eventRepository.searchEventPeriod(startDate, endDate)).thenReturn(foundEvents);
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
		when(eventRepository.searchEventPeriod(startDate, endDate)).thenReturn(foundEvents);
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
		when(eventRepository.searchEventFieldPeriod("ENTERTAINMENT", startDate, endDate)).thenReturn(foundEvents);
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
		when(eventRepository.searchEventFieldPeriod("Address6", startDate, endDate)).thenReturn(foundEvents);
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
		when(eventRepository.searchEventFieldPeriod("Event7", startDate, endDate)).thenReturn(foundEvents);
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
		when(eventRepository.searchEventFieldPeriod("Event1", startDate, endDate)).thenReturn(foundEvents);
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
		when(eventRepository.searchEventFieldPeriod("Address1", startDate, endDate)).thenReturn(foundEvents);
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
		when(eventRepository.searchEventFieldPeriod("sports", startDate, endDate)).thenReturn(foundEvents);
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
		when(eventRepository.searchEventFieldPeriod("Event1", startDate, endDate)).thenReturn(foundEvents);
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
		when(eventRepository.searchEventFieldPeriod("SPORTS", startDate, endDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("SPORTS", "2020-01-15T00:00", "2020-12-15T00:00");
		assertEquals(1, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
	}
	
	@Test
	@Transactional
	public void searchEvent_periodAndType_successfull_addressGivenForPeriod() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e1);
		foundEvents.add(e3);
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		endDate=endDate.plusHours(1);
		when(eventRepository.searchEventFieldPeriod("Address1", startDate, endDate)).thenReturn(foundEvents);
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
	@Transactional
	public void searchEvent_fieldSpecDate_unsuccessfull_noEventType() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		when(eventRepository.searchEventFieldSpecDate("entertainment", startDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("entertainment", "2020-01-15T01:00", "***");
		assertEquals(0, events.size());
	}
	
	@Test
	@Transactional
	public void searchEvent_fieldSpecDate_unsuccessfull_noDate() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		when(eventRepository.searchEventFieldSpecDate("sports", startDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("sports", "2020-01-15T01:00", "***");
		assertEquals(0, events.size());
	}
	
	@Test
	@Transactional
	public void searchEventSpecDate_successfull_typeGiven() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e7);
		LocalDateTime startDate = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0);
		startDate=startDate.plusHours(1);
		when(eventRepository.searchEventFieldSpecDate("sports", startDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("sports", "2018-01-01T00:00", "***");
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}
	
	
	@Test
	@Transactional
	public void searchEventSpecDate_successfull_addressGiven() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e7);
		LocalDateTime startDate = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0);
		startDate=startDate.plusHours(1);
		when(eventRepository.searchEventFieldSpecDate("address5", startDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("address5", "2018-01-01T00:00", "***");
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}
	
	
	@Test
	@Transactional
	public void searchEventSpecDate_successfull_eventNameGiven() {
		ArrayList<Event> foundEvents = new ArrayList<Event>();
		foundEvents.add(e7);
		LocalDateTime startDate = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0);
		startDate=startDate.plusHours(1);
		when(eventRepository.searchEventFieldSpecDate("event6", startDate)).thenReturn(foundEvents);
		List<Event> events = eventService.searchEvent("event6", "2018-01-01T00:00", "***");
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}
	

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
}
