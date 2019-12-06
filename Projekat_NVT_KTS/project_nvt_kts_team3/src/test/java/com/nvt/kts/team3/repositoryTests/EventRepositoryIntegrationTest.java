package com.nvt.kts.team3.repositoryTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
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
		assertEquals(7, events.size());
		
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
	
}
