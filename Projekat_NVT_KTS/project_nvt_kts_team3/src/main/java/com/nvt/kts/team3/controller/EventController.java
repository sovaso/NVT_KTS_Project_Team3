package com.nvt.kts.team3.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nvt.kts.team3.dto.EventDTO;
import com.nvt.kts.team3.dto.EventReportDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.ReservationService;
import com.nvt.kts.team3.service.TicketService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private ReservationService reservationService;
	
	@PostMapping(value = "/createEvent", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> createEvent(@RequestBody EventDTO eventDTO) throws ParseException{
		eventService.save(eventDTO);
		return new ResponseEntity<>(new MessageDTO("Success", "Event successfully created."), HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/updateEvent", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> updateEvent(@RequestBody EventDTO eventDTO)  throws ParseException{
		eventService.update(eventDTO);
		return new ResponseEntity<>(new MessageDTO("Success", "Event successfully updated."), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getEvent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Event> getEvent(@PathVariable(value = "id") Long eventId){
		Event event = eventService.findById(eventId);
		if(event == null){
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(event, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getAllEvents", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getAllEvents(){
		return new ResponseEntity<>(eventService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getActiveEvents", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Event>> getActiveEvents(){
		return new ResponseEntity<>(eventService.getActiveEvents(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getEventLocation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Location> getEventLocation(@PathVariable(value = "id") Long eventId){
		Event event = eventService.findById(eventId);
		if(event == null || event.getLocationInfo() == null){
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(event.getLocationInfo(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getEventTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> getEventTickets(@PathVariable(value = "id") Long eventId){
		return new ResponseEntity<>(ticketService.getEventTickets(eventId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getEventReservedTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> getEventReservedTickets(@PathVariable(value = "id") Long eventId){
		return new ResponseEntity<>(ticketService.getEventReservedTickets(eventId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getEventSoldTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> getEventSoldTickets(@PathVariable(value = "id") Long eventId){
		return new ResponseEntity<>(ticketService.getEventSoldTickets(eventId), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/deleteEvent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> deleteEvent(@PathVariable(value = "id") Long eventId){
		eventService.remove(eventId);
		return new ResponseEntity<>(new MessageDTO("Success", "Event successfully deleted."), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getEventIncome/{event_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Double> getEventIncome(@PathVariable long event_id) {
		Event e=this.eventService.findById(event_id);
		List<Reservation> reservations=this.reservationService.findByEvent(e);
		double income=0;
		for(Reservation r:reservations) {
			if(r.isPaid()==true) {
				income+=r.getTotalPrice();
			}
		}
		return new ResponseEntity<Double>(income,HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/getEventReport/{event_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getEventReport(@PathVariable long event_id) throws ParseException {
		EventReportDTO retVal=this.eventService.getEventReport(event_id);
		if (retVal != null) {
			return new ResponseEntity<>(retVal, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new MessageDTO("Not found", "Event with this ID does not exist."), HttpStatus.NOT_FOUND);
			//return ResponseEntity.badRequest().body("Event with given id does not exist");
		}
	
		
	}
	
	
	
}

