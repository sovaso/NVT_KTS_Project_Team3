package com.nvt.kts.team3.controllerTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

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

import com.nvt.kts.team3.dto.EventDTO;
import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.MaintenanceDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.repository.EventRepository;
import com.nvt.kts.team3.repository.LeasedZoneRepository;
import com.nvt.kts.team3.repository.LocationZoneRepository;
import com.nvt.kts.team3.repository.MaintenanceRepository;
import com.nvt.kts.team3.repository.TicketRepository;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EventControllerIntegrationTestsForSearch {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Test
	@Transactional
	public void testSortByName_successfull() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/sortByName", Event[].class);
		Event[] events = responseEntity.getBody();
		assertEquals(11, events.length);
		assertEquals("Event1", events[0].getName());
		assertEquals("Event1", events[1].getName());
		assertEquals("Event10", events[2].getName());
		assertEquals("Event11", events[3].getName());
		assertEquals("Event2", events[4].getName());
		assertEquals("Event3", events[5].getName());
		assertEquals("Event4", events[6].getName());
		assertEquals("Event5", events[7].getName());
		assertEquals("Event6", events[8].getName());
		assertEquals("Event8", events[9].getName());
		assertEquals("Event9", events[10].getName());
	}
	
	@Test
	@Transactional
	public void testSortByDateAcs_successfull() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/sortByDateAcs", Event[].class);
		Event[] events = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(17, events.length);
		assertEquals("Event6", events[0].getName());
		assertEquals("Event8", events[1].getName());
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
		assertEquals("Event10", events[12].getName());
		assertEquals("Event11", events[13].getName());
		assertEquals("Event10", events[14].getName());
		assertEquals("Event1", events[15].getName());
		assertEquals("Event2", events[16].getName());
	}
	
	
	@Test
	@Transactional
	public void testSortByDateDesc_successfull() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/sortByDateDesc", Event[].class);
		Event[] events = responseEntity.getBody();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(17, events.length);
		assertEquals("Event8", events[16].getName());
		assertEquals("Event6", events[15].getName());
		assertEquals("Event9", events[14].getName());
		assertEquals("Event3", events[13].getName());
		assertEquals("Event5", events[12].getName());
		assertEquals("Event8", events[11].getName());
		assertEquals("Event6", events[10].getName());
		assertEquals("Event3", events[9].getName());
		assertEquals("Event4", events[8].getName());
		assertEquals("Event1", events[7].getName());
		assertEquals("Event1", events[6].getName());
		assertEquals("Event11", events[5].getName());
		assertEquals("Event10", events[4].getName());
		assertEquals("Event1", events[3].getName());
		assertEquals("Event10", events[2].getName());
		assertEquals("Event1", events[1].getName());
		assertEquals("Event2", events[0].getName());
	}
	
	@Test
	@Transactional
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
	@Transactional
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
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	
	@Test
	@Transactional
	public void searchEvent_onlyField_eventNameGiven_nonFound() {
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/findEvent/someEventName/***/***", MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Event with desired criterias does not exist.", messageDto.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	@Transactional
	public void searchEvent_onlyField_eventTypeGiven_successfull() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/findEvent/entertainment/***/***", Event[].class);
		Event[] events = responseEntity.getBody();
		assertEquals(2, events.length);
		assertEquals("Event3", events[0].getName());
		assertEquals(EventType.ENTERTAINMENT, events[0].getType());
		assertEquals(1, events[0].getLocationInfo().getId());
		assertEquals("Event8", events[1].getName());
		assertEquals(EventType.ENTERTAINMENT, events[1].getType());
		assertEquals(1, events[1].getLocationInfo().getId());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	
	@Test
	@Transactional
	public void searchEvent_onlyField_addressGiven_successfull() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/findEvent/Address4/***/***", Event[].class);
		Event[] events = responseEntity.getBody();
		assertEquals(1, events.length);
		assertEquals("Event5", events[0].getName());
		assertTrue(events[0].isStatus());
		assertEquals(EventType.CULTURAL, events[0].getType());
		assertEquals(4, events[0].getLocationInfo().getId());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	
	@Test
	@Transactional
	public void searchEvent_onlyField_dateGiven_successfull() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/findEvent/***/2021-01-15T00:00/***", Event[].class);
		Event[] events = responseEntity.getBody();
		assertEquals(1, events.length);
		assertEquals("Event1", events[0].getName());
		assertEquals(EventType.SPORTS, events[0].getType());
		assertEquals(1, events[0].getLocationInfo().getId());

	}
	
	@Test
	@Transactional
	public void searchEvent_onlyField_dateGiven_unsuccessfull() {
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/findEvent/***/2010-01-15T00:00/***", MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Event with desired criterias does not exist.", messageDto.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	@Transactional
	public void searchEvent_onlyField_dateGiven_successfullMoreThanOneResult() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/findEvent/***/2018-01-01T00:00/***", Event[].class);
		Event[] events = responseEntity.getBody();
		assertEquals(2, events.length);
		assertEquals("Event8", events[0].getName());
		assertEquals(EventType.ENTERTAINMENT, events[0].getType());
		assertEquals(1, events[0].getLocationInfo().getId());
		assertEquals("Event6", events[1].getName());
		assertEquals(EventType.SPORTS, events[1].getType());
		assertEquals(5, events[1].getLocationInfo().getId());
	}
	
	@Test
	@Transactional
	public void searchEvent_period_successfull() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/findEvent/***/2021-01-15T00:00/2021-12-15T00:00", Event[].class);
		Event[] events = responseEntity.getBody();
		assertEquals(2, events.length);

		assertEquals("Event1", events[0].getName());
		assertEquals(EventType.SPORTS, events[0].getType());
		assertEquals(1, events[0].getLocationInfo().getId());

		assertEquals("Event2", events[1].getName());
		assertTrue(events[1].isStatus());
		assertEquals(EventType.CULTURAL, events[1].getType());
		assertEquals(1, events[1].getLocationInfo().getId());
	}
	
	@Test
	@Transactional
	public void searchEvent_period_nonFound() {
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/findEvent/***/2010-01-15T00:00/2010-12-15T00:00", MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Event with desired criterias does not exist.", messageDto.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	@Transactional
	public void searchEvent_periodAndType_unsuccessfull_noTypeInThatPeriod() {
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/findEvent/ENTERTAINMENT/2021-01-15T00:00/2021-12-15T00:00", MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Event with desired criterias does not exist.", messageDto.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	@Transactional
	public void searchEvent_periodAndType_unsuccessfull_noAddressInThatPeriod() {
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/findEvent/Address6/2021-01-15T00:00/2021-12-15T00:00", MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Event with desired criterias does not exist.", messageDto.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	@Transactional
	public void searchEvent_periodAndType_unsuccessfull_noEventInThatPeriod() {
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/findEvent/Event7/2021-01-15T00:00/2021-12-15T00:00", MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Event with desired criterias does not exist.", messageDto.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	@Transactional
	public void searchEvent_periodAndType_unsuccessfull_eventExistsButNotInThatPeriod() {
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/findEvent/Event1/2010-01-15T00:00/2010-12-15T00:00", MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Event with desired criterias does not exist.", messageDto.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	

	@Test
	@Transactional
	public void searchEvent_periodAndType_unsuccessfull_addressExistsButNotInThatPeriod() {
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/findEvent/Address1/2010-01-15T00:00/2010-12-15T00:00", MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Event with desired criterias does not exist.", messageDto.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	@Transactional
	public void searchEventPeriodAndType_unsuccessfull_typeoExistsButNotInThatPeriod() {
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/findEvent/sports/2010-01-15T00:00/2010-12-15T00:00", MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Event with desired criterias does not exist.", messageDto.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	@Transactional
	public void searchEvent_periodAndType_successfull_eventNameGivenForPeriod() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/findEvent/Event1/2021-01-15T00:00/2021-12-15T00:00", Event[].class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Event[] events = responseEntity.getBody();
		assertEquals(1, events.length);
		assertEquals("Event1", events[0].getName());
		assertEquals(EventType.SPORTS, events[0].getType());
		assertEquals(1, events[0].getLocationInfo().getId());
	}

	@Test
	@Transactional
	public void searchEvent_periodAndType_successfull_eventTypeGivenForPeriod() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/findEvent/SPORTS/2021-01-15T00:00/2021-12-15T00:00", Event[].class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Event[] events = responseEntity.getBody();
		assertEquals(1, events.length);
		assertEquals("Event1", events[0].getName());
		assertEquals(EventType.SPORTS, events[0].getType());
		assertEquals(1, events[0].getLocationInfo().getId());
	}

	@Test
	@Transactional
	public void searchEvent_periodAndType_successfull_addressGivenForPeriod() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/findEvent/Address1/2021-01-15T00:00/2021-12-15T00:00", Event[].class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Event[] events = responseEntity.getBody();
		assertEquals(2, events.length);
		assertEquals("Event1", events[0].getName());
		assertEquals(EventType.SPORTS, events[0].getType());
		assertEquals(1, events[0].getLocationInfo().getId());

		assertEquals("Event2", events[1].getName());
		assertEquals(EventType.CULTURAL, events[1].getType());
		assertEquals(1, events[1].getLocationInfo().getId());
	}
	
	@Test
	@Transactional
	public void searchEvent_fieldSpecDate_unsuccessfull_noEventType() {
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/findEvent/entertainment/2021-01-15T01:00/***", MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Event with desired criterias does not exist.", messageDto.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	@Transactional
	public void searchEvent_fieldSpecDate_unsuccessfull_noDate() {
		ResponseEntity<MessageDTO> responseEntity = testRestTemplate.getForEntity("/api/findEvent/sports/2021-01-15T01:00/***", MessageDTO.class);
		MessageDTO messageDto = responseEntity.getBody();
		assertEquals("Not found", messageDto.getMessage());
		assertEquals("Event with desired criterias does not exist.", messageDto.getHeader());
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	@Transactional
	public void searchEventSpecDate_successfull_typeGiven() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/findEvent/sports/2018-01-01T00:00/***", Event[].class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Event[] events = responseEntity.getBody();
		assertEquals(1, events.length);
		assertEquals("Event6", events[0].getName());
		assertEquals(EventType.SPORTS, events[0].getType());
	}


	@Test
	@Transactional
	public void searchEventSpecDate_successfull_addressGiven() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/findEvent/address5/2018-01-01T00:00/***", Event[].class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Event[] events = responseEntity.getBody();
		assertEquals(1, events.length);
		assertEquals("Event6", events[0].getName());
		assertEquals(EventType.SPORTS, events[0].getType());
	}

	@Test
	@Transactional
	public void searchEventSpecDate_successfull_eventNameGiven() {
		ResponseEntity<Event[]> responseEntity = testRestTemplate.getForEntity("/api/findEvent/event6/2018-01-01T00:00/***", Event[].class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Event[] events = responseEntity.getBody();
		assertEquals(1, events.length);
		assertEquals("Event6", events[0].getName());
		assertEquals(EventType.SPORTS, events[0].getType());
	}
	

}
