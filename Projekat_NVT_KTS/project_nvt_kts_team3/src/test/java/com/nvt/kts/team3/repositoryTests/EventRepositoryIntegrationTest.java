package com.nvt.kts.team3.repositoryTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.repository.EventRepository;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class EventRepositoryIntegrationTest {

	@Autowired
	public EventRepository eventRepository;
	
	@Test
	public void testGetAll() {
		List<Event> events = eventRepository.findAll();
		assertEquals(9, events.size());
		
	}
	@Test
	@Transactional
	public void findByName_OneFound() {
		List<Event> events = eventRepository.findByName("Event2");
		assertEquals(1, events.size());
		assertEquals(EventType.CULTURAL, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
		assertTrue(events.get(0).isStatus());
	}
	
	@Test
	@Transactional
	public void findByName_MoreThanOneFound() {
		List<Event> events = eventRepository.findByName("Event1");
		assertEquals(2, events.size());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
		assertTrue(events.get(0).isStatus());
		assertEquals(EventType.CULTURAL, events.get(1).getType());
		assertEquals(2, events.get(1).getLocationInfo().getId());
		assertTrue(events.get(1).isStatus());
	}
	
	@Test
	public void findByName_nonFound() {
		List<Event> events = eventRepository.findByName("asdfgdfsg");
		assertEquals(0, events.size());
	}
	
	@Test
	@Transactional
	public void searchEventOnlyField_nonFound() {
		ArrayList<Event> events = eventRepository.searchEventOnlyField("someField");
		assertEquals(0, events.size());
	}
	
	@Test
	@Transactional
	public void searchEventOnlyField_eventNameGiven_oneFound() {
		ArrayList<Event> events = eventRepository.searchEventOnlyField("Event2");
		assertEquals(1, events.size());
		assertTrue(events.get(0).isStatus());
		assertEquals(EventType.CULTURAL, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
	} 
	
	@Test
	@Transactional
	public void searchEventOnlyField_eventNameGiven_moreThanOneFound() {
		ArrayList<Event> events = eventRepository.searchEventOnlyField("Event1");
		assertEquals(2, events.size());
		assertTrue(events.get(0).isStatus());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
		
		assertTrue(events.get(1).isStatus());
		assertEquals(EventType.CULTURAL, events.get(1).getType());
		assertEquals(2, events.get(1).getLocationInfo().getId());
	} 
	
	
	@Test
	@Transactional
	public void searchEventOnlyField_addressGiven_moreThanOneFound() {
		ArrayList<Event> events = eventRepository.searchEventOnlyField("Address1");
		assertEquals(4, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
		
		assertEquals("Event2", events.get(1).getName());
		assertTrue(events.get(1).isStatus());
		assertEquals(EventType.CULTURAL, events.get(1).getType());
		assertEquals(1, events.get(1).getLocationInfo().getId());
		
		assertEquals("Event3", events.get(2).getName());
		assertEquals(EventType.ENTERTAINMENT, events.get(2).getType());
		assertEquals(1, events.get(2).getLocationInfo().getId());
		
		assertEquals("Event8", events.get(3).getName());
		assertTrue(events.get(3).isStatus());
		assertEquals(EventType.ENTERTAINMENT, events.get(3).getType());
		assertEquals(1, events.get(3).getLocationInfo().getId());
	}
	
	
	@Test
	@Transactional
	public void searchEventOnlyField_addressGiven_oneFound() {
		ArrayList<Event> events = eventRepository.searchEventOnlyField("Address4");
		assertEquals(1, events.size());
		assertEquals("Event5", events.get(0).getName());
		assertTrue(events.get(0).isStatus());
		assertEquals(EventType.CULTURAL, events.get(0).getType());
		assertEquals(4, events.get(0).getLocationInfo().getId());
	}
	
	@Test
	@Transactional
	public void searchEventOnlyField_eventTypeGiven_successfull() {
		ArrayList<Event> events = eventRepository.searchEventOnlyField("Sports");
		assertEquals(4, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
		
		assertEquals("Event4", events.get(1).getName());
		assertTrue(events.get(1).isStatus());
		assertEquals(EventType.SPORTS, events.get(1).getType());
		assertEquals(2, events.get(1).getLocationInfo().getId());
		
		assertEquals("Event6", events.get(2).getName());
		assertEquals(EventType.SPORTS, events.get(2).getType());
		assertEquals(5, events.get(2).getLocationInfo().getId());
		
		assertEquals("Event9", events.get(3).getName());
		//assertFalse(events.get(3).isStatus());
		assertEquals(EventType.SPORTS, events.get(3).getType());
		assertEquals(6, events.get(3).getLocationInfo().getId());
	}
	
	
	
	@Test
	@Transactional
	public void searchEventPeriod_nonFound() {
		LocalDateTime startDate = LocalDateTime.of(2015, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2015, Month.DECEMBER, 15, 0, 0, 0);
		ArrayList<Event> events = eventRepository.searchEventPeriod(startDate, endDate);
		assertEquals(0, events.size());
	
	}
	
	@Test
	@Transactional
	public void searchEventPeriod_successfull() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		ArrayList<Event> events = eventRepository.searchEventPeriod(startDate, endDate);
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
	@Transactional
	public void searchEventPeriodAndType_unsuccessfull_noTypeInThatPeriod() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		ArrayList<Event> events = eventRepository.searchEventFieldPeriod("ENTERTAINMENT", startDate, endDate);
		assertEquals(0, events.size());
	}
	
	@Test
	@Transactional
	public void searchEventPeriodAndType_unsuccessfull_noAddressInThatPeriod() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		ArrayList<Event> events = eventRepository.searchEventFieldPeriod("Address6", startDate, endDate);
		assertEquals(0, events.size());
	}
	
	@Test
	@Transactional
	public void searchEventPeriodAndType_unsuccessfull_noEventInThatPeriod() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		ArrayList<Event> events = eventRepository.searchEventFieldPeriod("Event7", startDate, endDate);
		assertEquals(0, events.size());
	}
	
	@Test
	@Transactional
	public void searchEventPeriodAndType_unsuccessfull_eventExistsButNotInThatPeriod() {
		LocalDateTime startDate = LocalDateTime.of(2010, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2010, Month.DECEMBER, 15, 0, 0, 0);
		ArrayList<Event> events = eventRepository.searchEventFieldPeriod("Event1", startDate, endDate);
		assertEquals(0, events.size());
	}
	
	@Test
	@Transactional
	public void searchEventPeriodAndType_unsuccessfull_addressExistsButNotInThatPeriod() {
		LocalDateTime startDate = LocalDateTime.of(2010, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2010, Month.DECEMBER, 15, 0, 0, 0);
		ArrayList<Event> events = eventRepository.searchEventFieldPeriod("Address1", startDate, endDate);
		assertEquals(0, events.size());
	}
	
	@Test
	@Transactional
	public void searchEventPeriodAndType_unsuccessfull_typeoExistsButNotInThatPeriod() {
		LocalDateTime startDate = LocalDateTime.of(2010, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2010, Month.DECEMBER, 15, 0, 0, 0);
		ArrayList<Event> events = eventRepository.searchEventFieldPeriod("sports", startDate, endDate);
		assertEquals(0, events.size());
	}
	@Test
	@Transactional
	public void searchEventPeriodAndType_successfull_eventNameGivenForPeriod() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		ArrayList<Event> events = eventRepository.searchEventFieldPeriod("Event1", startDate, endDate);
		assertEquals(1, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
	}
	
	@Test
	@Transactional
	public void searchEventPeriodAndType_successfull_eventTypeGivenForPeriod() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		ArrayList<Event> events = eventRepository.searchEventFieldPeriod("SPORTS", startDate, endDate);
		assertEquals(1, events.size());
		assertEquals("Event1", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
	}
	
	@Test
	@Transactional
	public void searchEventPeriodAndType_successfull_addressGivenForPeriod() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0, 0);
		ArrayList<Event> events = eventRepository.searchEventFieldPeriod("Address1", startDate, endDate);
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
	public void findAllSortedDateAcs() {
		ArrayList<Event> events = eventRepository.findAllSortedDateAcs();
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
	
//Za ovo sortiranje uraditi slucaj i praznjenja baze
	@Test
	@Transactional
	public void findAllSortedDateDescs() {
		ArrayList<Event> events = eventRepository.findAllSortedDateDesc();
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
	@Transactional
	public void searchEventSpecDate_successfull() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		ArrayList<Event> events = eventRepository.searchEventSpecDate(startDate);
		assertEquals(1, events.size());
		assertEquals(EventType.SPORTS, events.get(0).getType());
		assertEquals(1, events.get(0).getLocationInfo().getId());
		assertTrue(events.get(0).isStatus());
	}
	
	@Test
	@Transactional
	public void searchEventSpecDate_unsuccessfull() {
		LocalDateTime startDate = LocalDateTime.of(2010, Month.JANUARY, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		ArrayList<Event> events = eventRepository.searchEventSpecDate(startDate);
		assertEquals(0, events.size());
	}
	
	@Test
	@Transactional
	public void searchEventSpecDate_unsuccessfull_noEventType() {
		LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		ArrayList<Event> events = eventRepository.searchEventFieldSpecDate("entertainment", startDate);
		assertEquals(0, events.size());
	}
	
	@Test
	@Transactional
	public void searchEventSpecDate_unsuccessfull_noDate() {
		LocalDateTime startDate = LocalDateTime.of(2010, Month.JANUARY, 15, 0, 0, 0);
		startDate=startDate.plusHours(1);
		ArrayList<Event> events = eventRepository.searchEventFieldSpecDate("sports", startDate);
		assertEquals(0, events.size());
	}
	
	@Test
	@Transactional
	public void searchEventSpecDate_successfull_typeGiven() {
		LocalDateTime startDate = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0);
		startDate=startDate.plusHours(1);
		ArrayList<Event> events = eventRepository.searchEventFieldSpecDate("sports", startDate);
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}
	
	@Test
	@Transactional
	public void searchEventSpecDate_successfull_addressGiven() {
		LocalDateTime startDate = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0);
		startDate=startDate.plusHours(1);
		ArrayList<Event> events = eventRepository.searchEventFieldSpecDate("address5", startDate);
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}
	
	@Test
	@Transactional
	public void searchEventSpecDate_successfull_eventNameGiven() {
		LocalDateTime startDate = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0);
		startDate=startDate.plusHours(1);
		ArrayList<Event> events = eventRepository.searchEventFieldSpecDate("event6", startDate);
		assertEquals(1, events.size());
		assertEquals("Event6", events.get(0).getName());
		assertEquals(EventType.SPORTS, events.get(0).getType());
	}
}
