package com.nvt.kts.team3.controllerTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.repository.EventRepository;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EventControllerIntegrationTest {
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private EventRepository eventRepository;

	private String token;
	
	@Test
	public void testSortByName_successfull() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/sortByName", Event[].class);
		Event[] events = responseEntity.getBody();
		assertEquals(9, events.length);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Event1", events[0].getName());
		assertEquals("Event1", events[1].getName());
		assertEquals("Event2", events[2].getName());
		assertEquals("Event3", events[3].getName());
		assertEquals("Event4", events[4].getName());
		assertEquals("Event5", events[5].getName());
		assertEquals("Event6", events[6].getName());
		assertEquals("Event8", events[7].getName());
		assertEquals("Event9", events[8].getName());
	}
	
	@Test
	public void testSortByDateAcs_successfull() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/sortByDateAcs", Event[].class);
		Event[] events = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(13, events.length);
		assertEquals("Event8", events[0].getName());
		assertEquals("Event6", events[1].getName());
		assertEquals("Event9", events[2].getName());
		assertEquals("Event3", events[3].getName());
		assertEquals("Event5", events[4].getName());
		assertEquals("Event8", events[5].getName());
		assertEquals("Event6", events[6].getName());
		assertEquals("Event3", events[7].getName());
		assertEquals("Event4", events[8].getName());
		assertEquals("Event1", events[9].getName());
		assertEquals("Event1", events[10].getName());
		assertEquals("Event1", events[11].getName());
		assertEquals("Event2", events[12].getName());
	}
	
	@Test
	public void testSortByDateDesc_successfull() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/sortByDateDesc", Event[].class);
		Event[] events = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(13, events.length);
		assertEquals("Event6", events[12].getName());
		assertEquals("Event8", events[11].getName());
		assertEquals("Event9", events[10].getName());
		assertEquals("Event3", events[9].getName());
		assertEquals("Event5", events[8].getName());
		assertEquals("Event8", events[7].getName());
		assertEquals("Event6", events[6].getName());
		assertEquals("Event3", events[5].getName());
		assertEquals("Event4", events[4].getName());
		assertEquals("Event1", events[3].getName());
		assertEquals("Event1", events[2].getName());
		assertEquals("Event1", events[1].getName());
		assertEquals("Event2", events[0].getName());
	}
	
	@Test
	public void testSearchEvent_fieldGiven_eventName() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/findEvent/Event2/***/***", Event[].class);
		Event[] events = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(1, events.length);
		assertEquals("Event2", events[0].getName());
		assertEquals(EventType.CULTURAL, events[0].getType());
		assertEquals(1, events[0].getLocationInfo().getId());
	}
	
	@Test
	public void testSearchEvent_fieldGiven_eventName_moreThanOneResult() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/findEvent/Event1/***/***", Event[].class);
		Event[] events = responseEntity.getBody();
		assertEquals(2, events.length);
		assertEquals("Event1", events[0].getName());
		assertEquals(EventType.SPORTS, events[0].getType());
		assertEquals(1, events[0].getLocationInfo().getId());
		assertEquals("Event1", events[1].getName());
		assertEquals(EventType.CULTURAL, events[1].getType());
		assertEquals(2, events[1].getLocationInfo().getId());
	}
	
	
}
