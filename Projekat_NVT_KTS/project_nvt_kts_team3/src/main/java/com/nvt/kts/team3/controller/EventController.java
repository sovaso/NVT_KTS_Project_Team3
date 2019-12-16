package com.nvt.kts.team3.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nvt.kts.team3.dto.EventDTO;
import com.nvt.kts.team3.dto.EventReportDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.dto.UploadFileDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.service.EventService;

import exception.EventNotChangeable;
import exception.EventNotFound;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="http://localhost:4200", allowedHeaders = "*")
public class EventController {

	@Autowired
	private EventService eventService;
	
	@PostMapping(value = "/createEvent", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> createEvent(@RequestBody EventDTO eventDTO) throws ParseException {
		eventService.save(eventDTO);
		return new ResponseEntity<>(new MessageDTO("Success", "Event successfully created."), HttpStatus.CREATED);
	}

	@PostMapping(value = "/updateEvent", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> updateEvent(@RequestBody EventDTO eventDTO) throws ParseException {
		eventService.update(eventDTO);
		return new ResponseEntity<>(new MessageDTO("Success", "Event successfully updated."), HttpStatus.OK);
	}

	@GetMapping(value = "/getEvent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> getEvent(@PathVariable(value = "id") Long eventId) { //menjano
		Optional<Event> event = eventService.findById(eventId);
		if (!event.isPresent()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(event.get(), HttpStatus.OK);
	}

	@GetMapping(value = "/getAllEvents", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllEvents() {
		return new ResponseEntity<>(eventService.findAll(), HttpStatus.OK);
	}

	@GetMapping(value = "/getActiveEvents", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getActiveEvents() {
		return new ResponseEntity<>(eventService.getActiveEvents(), HttpStatus.OK);
	}

	@GetMapping(value = "/getEventLocation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Location> getEventLocation(@PathVariable(value = "id") Long eventId) {
		Optional<Event> event = eventService.findById(eventId);
		if (!event.isPresent() || event.get().getLocationInfo() == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(event.get().getLocationInfo(), HttpStatus.OK);
	}

	@DeleteMapping(value = "/deleteEvent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> deleteEvent(@PathVariable(value = "id") Long eventId) {
		System.out.println("Uslo u delete event");
		try {
			eventService.remove(eventId);
			return new ResponseEntity<>(new MessageDTO("Success", "Event successfully deleted."), HttpStatus.OK);
		}catch(EventNotFound e) {
			return new ResponseEntity<>(new MessageDTO("Not found", "Event already deleted."), HttpStatus.OK);
		}catch(EventNotChangeable e) {
			return new ResponseEntity<>(new MessageDTO("Conflict", "Not able to be deleted. There are sold tickets."), HttpStatus.OK);
		}
		
		
	}

	@GetMapping(value = "/getEventIncome/{event_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getEventIncome(@PathVariable long event_id) {
		Double income = this.eventService.getEventIncome(event_id);
		return new ResponseEntity<Double>(income, HttpStatus.OK);

	}
	
	@GetMapping(value = "/getEventReport/{event_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getEventReport(@PathVariable long event_id) throws ParseException {
		EventReportDTO retVal = this.eventService.getEventReport(event_id);
		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}

	@PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> upload(@RequestBody UploadFileDTO uploadFileDTO)
			throws ParseException, GeneralSecurityException, IOException {
		String path = this.eventService.uploadFile(uploadFileDTO);
		return new ResponseEntity<>(path, HttpStatus.OK);

	}
	
	@GetMapping(value = "/findEvent/{field}/{startDate}/{endDate}")
	public ResponseEntity<?> findRentacars(
			@PathVariable(name = "field") String field,
			/*@RequestParam(name = "startDate")  @DateTimeFormat(iso = ISO.DATE) LocalDateTime startDate*/
			@PathVariable(name = "startDate") String startDate,
			@PathVariable(name = "endDate") String endDate
			/*@RequestParam(name = "endDate") @DateTimeFormat(iso = ISO.DATE) LocalDateTime endDate*/
			) {
		System.out.println("Uslo u search kontroler.");
		
		List<Event> events  = eventService.searchEvent(field, startDate, endDate);
		if (events.size() != 0) {
			return new ResponseEntity<>(events, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(new MessageDTO("Not found", "Event with desired criterias does not exist."), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = "/sortByName", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> sortByName() {
		System.out.println("Sort by name called");
		List<Event> events  = eventService.findAllSortedName();
		return new ResponseEntity<>(events, HttpStatus.OK);
		/*
		if (events.size() != 0) {
			return new ResponseEntity<>(events, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(new MessageDTO("Not found", "No events in database."), HttpStatus.NOT_FOUND);
		}
		*/
	}
	
	@GetMapping(value = "/sortByDateAcs")
	public ResponseEntity<?> sortByDateAcs() {
		System.out.println("Sort by date acs called");
		List<Event> events  = eventService.findAllSortedDateAcs();
		if (events.size() != 0) {
			return new ResponseEntity<>(events, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(new MessageDTO("Not found", "No events in database."), HttpStatus.NOT_FOUND);
		}
		
	}
	
	@GetMapping(value = "/sortByDateDesc")
	public ResponseEntity<?> sortByDateDesc() {
		System.out.println("Sort by date desc called");
		List<Event> events  = eventService.findAllSortedDateDesc();
		if (events.size() != 0) {
			return new ResponseEntity<>(events, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(new MessageDTO("Not found", "No events in database."), HttpStatus.NOT_FOUND);
		}
		
	}

}
