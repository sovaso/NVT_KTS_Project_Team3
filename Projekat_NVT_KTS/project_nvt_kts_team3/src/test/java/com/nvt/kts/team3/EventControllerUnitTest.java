package com.nvt.kts.team3;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.service.EventService;






@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventControllerUnitTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@MockBean
	private EventService eventServiceMock;
	
	private static final String EVENT_NAME1 = "Aaa";
	private static final String EVENT_NAME2= "Bbb";
	private static final boolean EVENT_STATUS = true;
	private static final EventType EVENT_TYPE1 = EventType.SPORTS;
	private static final EventType EVENT_TYPE2 = EventType.CULTURAL;
	private static final Set<Reservation> RESERVATIONS1=new HashSet<>();
	private static final Set<Maintenance> MAINTENANCES1 = new HashSet<>();
	private static final Set<Maintenance> MAINTENANCES2 = new HashSet<>();
	private static final Location locationInfo = new Location();
	private static final ArrayList<String> pictures = new ArrayList<>();
	private static final ArrayList<String> videos = new ArrayList<>();

	private static final LocalDateTime d11 = LocalDate.parse("2020-01-01").atStartOfDay();
	private static final LocalDateTime d12 = LocalDate.parse("2020-01-05").atStartOfDay();
	private static final LocalDateTime ex1 = LocalDate.parse("2020-01-03").atStartOfDay();
	
	private static final LocalDateTime d21 = LocalDate.parse("2020-12-01").atStartOfDay();
	private static final LocalDateTime d22 = LocalDate.parse("2020-12-05").atStartOfDay();
	private static final LocalDateTime ex2 = LocalDate.parse("2020-12-03").atStartOfDay();
	
	private static final Set<LeasedZone> leasedZones = new HashSet<>();
	
	private static final String START_DATE = "2020-01-01";
	private static final String END_DATE = "2020-01-05";
	private static final String FIELD = "Sports";
	
	

	@Test
	public void sortByNameNoEvents() {
		List<Event> events = new ArrayList<Event>();
		when(eventServiceMock.findAllSortedName()).thenReturn(events);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/sortByName", MessageDTO.class);
		MessageDTO responseBody = responseEntity.getBody();
		assertEquals("Not found", responseBody.getMessage());
		assertEquals("No events in database.", responseBody.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		
	}
	
	
	@Test
	public void sortByNameSuccessfull() {
		List<Event> events = new ArrayList<Event>();
		Event event1 = new Event(1L, EVENT_NAME1, EVENT_STATUS, EVENT_TYPE1, RESERVATIONS1, MAINTENANCES1, locationInfo, pictures, videos);
		events.add(event1);
		Event event2 = new Event(1L, EVENT_NAME2, EVENT_STATUS, EVENT_TYPE2, RESERVATIONS1, MAINTENANCES1, locationInfo, pictures, videos);
		events.add(event2);
		when(eventServiceMock.findAllSortedName()).thenReturn(events);
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/sortByName", Event[].class);
		Event[] responseBody = responseEntity.getBody();
		assertEquals(2, responseBody.length);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		
	}
	
	@Test
	public void sortByDateAcsNoEvents() {
		List<Event> events = new ArrayList<Event>();
		when(eventServiceMock.findAllSortedDateAcs()).thenReturn(events);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/sortByDateAcs", MessageDTO.class);
		MessageDTO responseBody = responseEntity.getBody();
		assertEquals("Not found", responseBody.getMessage());
		assertEquals("No events in database.", responseBody.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		
	}
	
	@Test
	public void sortByDateDescNoEvents() {
		List<Event> events = new ArrayList<Event>();
		when(eventServiceMock.findAllSortedDateDesc()).thenReturn(events);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/sortByDateDesc", MessageDTO.class);
		MessageDTO responseBody = responseEntity.getBody();
		assertEquals("Not found", responseBody.getMessage());
		assertEquals("No events in database.", responseBody.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		
	}
	
	@Test
	public void sortByDateAcsSuccessfull() {
		List<Event> events = new ArrayList<Event>();
		Maintenance m1 = new Maintenance(d11, d12, ex1, 2L, leasedZones, new Event());
		MAINTENANCES1.add(m1);
		Event event1 = new Event(1L, EVENT_NAME1, EVENT_STATUS, EVENT_TYPE1, RESERVATIONS1, MAINTENANCES1, locationInfo, pictures, videos);
		events.add(event1);
		m1.setEvent(event1);
		Maintenance m2 = new Maintenance(d21, d22, ex2, 3L, leasedZones, new Event());
		MAINTENANCES2.add(m2);
		Event event2 = new Event(1L, EVENT_NAME2, EVENT_STATUS, EVENT_TYPE2, RESERVATIONS1, MAINTENANCES2, locationInfo, pictures, videos);
		events.add(event2);
		m2.setEvent(event2);
		when(eventServiceMock.findAllSortedDateAcs()).thenReturn(events);
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/sortByDateAcs", Event[].class);
		Event[] responseBody = responseEntity.getBody();
		assertEquals(2, responseBody.length);
		assertEquals(EVENT_NAME1, responseBody[0].getName());
		assertEquals(EVENT_NAME2, responseBody[1].getName());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		
	}
	
	@Test
	public void sortByDateDescSuccessfull() {
		List<Event> events = new ArrayList<Event>();
		Maintenance m1 = new Maintenance(d11, d12, ex1, 2L, leasedZones, new Event());
		MAINTENANCES1.add(m1);
		Event event1 = new Event(1L, EVENT_NAME1, EVENT_STATUS, EVENT_TYPE1, RESERVATIONS1, MAINTENANCES1, locationInfo, pictures, videos);
		m1.setEvent(event1);
		Maintenance m2 = new Maintenance(d21, d22, ex2, 3L, leasedZones, new Event());
		MAINTENANCES2.add(m2);
		Event event2 = new Event(1L, EVENT_NAME2, EVENT_STATUS, EVENT_TYPE2, RESERVATIONS1, MAINTENANCES2, locationInfo, pictures, videos);
		m2.setEvent(event2);
		events.add(event2);
		events.add(event1);
		when(eventServiceMock.findAllSortedDateDesc()).thenReturn(events);
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/sortByDateDesc", Event[].class);
		Event[] responseBody = responseEntity.getBody();
		assertEquals(2, responseBody.length);
		assertEquals(EVENT_NAME1, responseBody[1].getName());
		assertEquals(EVENT_NAME2, responseBody[0].getName());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		
	}
	
	@Test
	public void searchEventsNotFound() {
		List<Event> events = new ArrayList<Event>();
		when(eventServiceMock.searchEvent(FIELD, START_DATE, END_DATE)).thenReturn(events);
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/findEvent/"+FIELD+"/"+START_DATE + "/"+END_DATE, MessageDTO.class);
		MessageDTO responseBody = responseEntity.getBody();
		assertEquals("Not found", responseBody.getMessage());
		assertEquals("Event with desired criterias does not exist.", responseBody.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	public void searchEventsSuccessfull() {
		List<Event> events = new ArrayList<Event>();
		Maintenance m1 = new Maintenance(d11, d12, ex1, 2L, leasedZones, new Event());
		MAINTENANCES1.add(m1);
		Event event1 = new Event(1L, EVENT_NAME1, EVENT_STATUS, EVENT_TYPE1, RESERVATIONS1, MAINTENANCES1, locationInfo, pictures, videos);
		m1.setEvent(event1);
		events.add(event1);
		when(eventServiceMock.searchEvent(FIELD, START_DATE, END_DATE)).thenReturn(events);
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/findEvent/"+FIELD+"/"+START_DATE + "/"+END_DATE, Event[].class);
		Event[] responseBody = responseEntity.getBody();
		assertEquals(1, responseBody.length);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	
}

