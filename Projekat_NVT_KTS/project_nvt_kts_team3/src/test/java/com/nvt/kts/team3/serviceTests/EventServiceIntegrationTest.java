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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.TicketService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventServiceIntegrationTest {

	@Autowired
	private EventService eventService;
	/*
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
	*/

	@Test
	public void findAllSortedName_successfull() {
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
	//ove metode srediti i za slucaj da je prazna baza
	@Test
	public void findAllSortedDateAcs_successfull() {
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
		assertEquals("Event8", events.get(11).getName());
		assertEquals("Event6", events.get(12).getName());
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
		List<Event> events = eventService.searchEvent("***", "2020-01-15T00:00", "***");
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
		List<Event> events = eventService.searchEvent("***", "2010-01-15T00:00", "2010-12-15T00:00");
		assertEquals(0, events.size());
	}

	@Test
	public void searchEvent_periodAndType_unsuccessfull_noTypeInThatPeriod() {
		List<Event> events = eventService.searchEvent("ENTERTAINMENT", "2020-01-15T00:00", "2020-12-15T00:00");
		assertEquals(0, events.size());
	}

	@Test
	public void searchEvent_periodAndType_unsuccessfull_noAddressInThatPeriod() {
		List<Event> events = eventService.searchEvent("Address6", "2020-01-15T00:00", "2020-12-15T00:00");
		assertEquals(0, events.size());
	}

	@Test
	public void searchEvent_periodAndType_unsuccessfull_noEventInThatPeriod() {
		List<Event> events = eventService.searchEvent("Event7", "2020-01-15T00:00", "2020-12-15T00:00");
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
		List<Event> events = eventService.searchEvent("Event1", "2020-01-15T00:00", "2020-12-15T00:00");
		assertEquals(1, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
	}

	@Test
	public void searchEvent_periodAndType_successfull_eventTypeGivenForPeriod() {
		List<Event> events = eventService.searchEvent("SPORTS", "2020-01-15T00:00", "2020-12-15T00:00");
		assertEquals(1, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
	}

	@Test
	@Transactional
	public void searchEvent_periodAndType_successfull_addressGivenForPeriod() {
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
		List<Event> events = eventService.searchEvent("entertainment", "2020-01-15T01:00", "***");
		assertEquals(0, events.size());
	}

	@Test
	@Transactional
	public void searchEvent_fieldSpecDate_unsuccessfull_noDate() {
		List<Event> events = eventService.searchEvent("sports", "2020-01-15T01:00", "***");
		assertEquals(0, events.size());
	}

	@Test
	@Transactional
	public void searchEventSpecDate_successfull_typeGiven() {
		List<Event> events = eventService.searchEvent("sports", "2018-01-01T00:00", "***");
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}


	@Test
	@Transactional
	public void searchEventSpecDate_successfull_addressGiven() {
		List<Event> events = eventService.searchEvent("address5", "2018-01-01T00:00", "***");
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}


	@Test
	@Transactional
	public void searchEventSpecDate_successfull_eventNameGiven() {
		List<Event> events = eventService.searchEvent("event6", "2018-01-01T00:00", "***");
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}

}