package com.nvt.kts.team3.repositoryTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import com.nvt.kts.team3.repository.EventRepository;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class EventRepositoryIntegrationTest {

	@Autowired
	public EventRepository eventRepository;
	
	@Test
	public void testGetAll() {
		List<Event> events = eventRepository.findAll();
		assertEquals(1, events.size());
		
	}
	@Test
	@Transactional
	public void findByName_found() {
		List<Event> events = eventRepository.findByName("Event1");
		assertEquals(1, events.size());
		
	}
	
}
