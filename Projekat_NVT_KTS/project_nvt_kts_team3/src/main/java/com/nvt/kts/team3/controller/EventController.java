package com.nvt.kts.team3.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.MaintenanceDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LeasedZoneService;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.ReservationService;
import com.nvt.kts.team3.service.TicketService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private MaintenanceService maintenanceService;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private LeasedZoneService leasedZoneService;
	
	@Autowired
	private LocationZoneService locationZoneService;
	
	@Autowired
	private ReservationService reservationService;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@PostMapping(value = "/createEvent", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> createEvent(@RequestBody EventDTO eventDTO){
		Event event = new Event();
		event.setStatus(true);
		event.setName(eventDTO.getName());
		event.setType(EventType.valueOf(eventDTO.getEventType()));
		if(event.getType() == null){
			return new ResponseEntity<>(new MessageDTO("Unable to create event", "Choosen event type could not be found."), HttpStatus.NOT_FOUND);
		}
		Location location = locationService.findById(eventDTO.getLocationId());
		if(location == null || location.isStatus() == false){
			return new ResponseEntity<>(new MessageDTO("Unable to create event", "Choosen location does not exist or is deleted."), HttpStatus.NOT_FOUND);
		}
		
		Date maintenanceStartDate = null;
		Date maintenanceEndDate = null;
		Date today = new Date();
		Calendar expiry = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 7);
		Date validDate = calendar.getTime();
		for(MaintenanceDTO dates : eventDTO.getMaintenance()){
			maintenanceStartDate = null;
			try {
				maintenanceStartDate = sdf.parse(dates.getStartDate());
				maintenanceEndDate = sdf.parse(dates.getEndDate());
				if(maintenanceStartDate.before(validDate) || maintenanceStartDate.before(today)){
					return new ResponseEntity<>(new MessageDTO("Unable to create event", "Event must be created at least 7 days before maintenance and date must be valid."), HttpStatus.OK);
				}
				expiry.setTime(maintenanceStartDate);
				expiry.add(Calendar.DATE, -3);
				
				Maintenance maintenance = new Maintenance();
				
				ArrayList<Event> locationEvents = locationService.checkIfAvailable(location.getId(), maintenanceStartDate, maintenanceEndDate);
				if(locationEvents.isEmpty() == false){
					return new ResponseEntity<>(new MessageDTO("Unable to create event", "Location is not available for specified period."), HttpStatus.OK);
				}
				
				for(LeasedZoneDTO lz : eventDTO.getLocationZones()){
					LocationZone locationZone = locationZoneService.findById(lz.getZoneId());
					if(locationZone == null || locationZone.getLocation().getId() != location.getId()){
						return new ResponseEntity<>(new MessageDTO("Unable to create event", "ID of location zone is not valid. Please make sure to choose location zone that exists and belongs to choosen location."), HttpStatus.NOT_FOUND);
					}
					LeasedZone newZone = new LeasedZone();
					newZone.setZone(locationZone);
					if(lz.getPrice() < 1 || lz.getPrice() > 10000){
						return new ResponseEntity<>(new MessageDTO("Unable to create event", "Please set a price of ticket that is between 1$ and 5000$."), HttpStatus.OK);
					}
					newZone.setSeatPrice(lz.getPrice());
					
					if(locationZone.isMatrix()){
						for(int i = 1; i <= locationZone.getColNumber(); i++){
							for(int j = 1; j <= locationZone.getRowNumber(); j++){
								Ticket ticket = new Ticket();
								ticket.setCol(i);
								ticket.setRow(j);
								ticket.setPrice(lz.getPrice());
								ticket.setReserved(false);
								ticket.setZone(newZone);
								newZone.getTickets().add(ticket);
							}
						}
					}
					else{
						for(int i = 0; i < locationZone.getCapacity(); i++){
							Ticket ticket = new Ticket();
							ticket.setCol(0);
							ticket.setRow(0);
							ticket.setPrice(lz.getPrice());
							ticket.setReserved(false);
							ticket.setZone(newZone);
							newZone.getTickets().add(ticket);
						}
					}
					newZone.setMaintenance(maintenance);
					maintenance.getLeasedZones().add(newZone);
				}
				maintenance.setMaintenanceDate(maintenanceStartDate);
				maintenance.setMaintenanceEndTime(maintenanceEndDate);
				maintenance.setReservationExpiry(expiry.getTime());
				event.getMaintenances().add(maintenance);
			} catch (Exception e) {
				return new ResponseEntity<>(new MessageDTO("Bad date format", "Please make sure that your date format is: yyyy-MM-dd HH:mm."), HttpStatus.BAD_REQUEST);
			}
		}
		
		event.setLocationInfo(location);
		Event newEvent = eventService.save(event);
		location.getEvents().add(newEvent);
		locationService.save(location);
		for(Maintenance m : event.getMaintenances()){
			m.setEvent(newEvent);
			Maintenance newMaintenance = maintenanceService.saveAndFlush(m);
			for(LeasedZone lz : m.getLeasedZones()){
				lz.setMaintenance(newMaintenance);
				LeasedZone newZone = leasedZoneService.saveAndFlush(lz);
				for(Ticket t : lz.getTickets()){
					t.setZone(newZone);
					ticketService.saveAndFlush(t);
				}
			}
		}
		return new ResponseEntity<>(new MessageDTO("Success", "Event successfuly created!"), HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/updateEvent", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> updateEvent(@RequestBody EventDTO eventDTO){
		Event event = eventService.findById(eventDTO.getId());
		if(eventIsActive(event.getId()) == false){
			return new ResponseEntity<>(new MessageDTO("Unable to update event", "Event with this ID does not exist or is not active."), HttpStatus.NOT_FOUND);
		}
		if(eventDTO.getName() != null){
			event.setName(eventDTO.getName());
		}
		
		event.setType(EventType.valueOf(eventDTO.getEventType()));
		if(event.getType() == null){
			return new ResponseEntity<>(new MessageDTO("Unable to update event", "Choosen event type could not be found."), HttpStatus.OK);
		}
		
		Location location = locationService.findById(eventDTO.getLocationId());
		if(location == null || location.isStatus() == false){
			return new ResponseEntity<>(new MessageDTO("Unable to update event", "Choosen location does not exist or is deleted from system."), HttpStatus.NOT_FOUND);
		}
		if(location.getId() == event.getLocationInfo().getId()){
			eventService.save(event);
			return new ResponseEntity<>(new MessageDTO("Success", "Event successfuly updated!"), HttpStatus.OK);
		}
		
		if(ticketService.getEventReservedTickets(event.getId()).isEmpty()){
			clearEventLocation(event);
		}
		else{
			eventService.save(event);
			return new ResponseEntity<>(new MessageDTO("Unable to update event location", "Basic informations about event are updated, but location could not be changed sience there are reserved tickets for this event."), HttpStatus.OK);
		}
		
		event.setLocationInfo(location);
		Date maintenanceStartDate = null;
		Date maintenanceEndDate = null;
		Date today = new Date();
		Calendar expiry = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 7);
		Date validDate = calendar.getTime();
		for(MaintenanceDTO dates : eventDTO.getMaintenance()){
			maintenanceStartDate = null;
			try {
				maintenanceStartDate = sdf.parse(dates.getStartDate());
				maintenanceEndDate = sdf.parse(dates.getEndDate());
				if(maintenanceStartDate.before(validDate) || maintenanceStartDate.before(today)){
					return new ResponseEntity<>(new MessageDTO("Unable to create event", "Event must be created at least 7 days before maintenance and date must be valid."), HttpStatus.OK);
				}
				expiry.setTime(maintenanceStartDate);
				expiry.add(Calendar.DATE, -3);
				
				Maintenance maintenance = new Maintenance();
				maintenance.setEvent(event);
				maintenance.setMaintenanceDate(maintenanceStartDate);
				maintenance.setMaintenanceEndTime(maintenanceEndDate);
				maintenance.setReservationExpiry(expiry.getTime());
				maintenance = maintenanceService.save(maintenance);
				event.getMaintenances().add(maintenance);
				
				ArrayList<Event> locationEvents = locationService.checkIfAvailable(location.getId(), maintenanceStartDate, maintenanceEndDate);
				if(locationEvents.isEmpty() == false){
					return new ResponseEntity<>(new MessageDTO("Unable to update event", "Location is not available for specified period."), HttpStatus.OK);
				}
				
				for(LeasedZoneDTO lz : eventDTO.getLocationZones()){
					LocationZone locationZone = locationZoneService.findById(lz.getZoneId());
					if(locationZone == null || locationZone.getLocation().getId() != location.getId()){
						return new ResponseEntity<>(new MessageDTO("Unable to update event", "ID of location zone is not valid. Please make sure to choose location zone that exists and belongs to choosen location."), HttpStatus.NOT_FOUND);
					}
					LeasedZone newZone = new LeasedZone();
					newZone.setZone(locationZone);
					if(lz.getPrice() < 1 || lz.getPrice() > 10000){
						return new ResponseEntity<>(new MessageDTO("Unable to create event", "Please set a price of ticket that is between 1$ and 5000$."), HttpStatus.OK);
					}
					newZone.setSeatPrice(lz.getPrice());
					newZone.setMaintenance(maintenance);
					newZone = leasedZoneService.save(newZone);
					
					maintenance.getLeasedZones().add(newZone);
					
					if(locationZone.isMatrix()){
						for(int i = 1; i <= locationZone.getColNumber(); i++){
							for(int j = 1; j <= locationZone.getRowNumber(); j++){
								Ticket ticket = new Ticket();
								ticket.setCol(i);
								ticket.setRow(j);
								ticket.setPrice(lz.getPrice());
								ticket.setReserved(false);
								ticket.setZone(newZone);
								ticketService.save(ticket);
							}
						}
					}
					else{
						for(int i = 0; i < locationZone.getCapacity(); i++){
							Ticket ticket = new Ticket();
							ticket.setCol(0);
							ticket.setRow(0);
							ticket.setPrice(lz.getPrice());
							ticket.setReserved(false);
							ticket.setZone(newZone);
							ticketService.save(ticket);
						}
					}
					
				}
				
			} catch (Exception e) {
				return new ResponseEntity<>(new MessageDTO("Bad date format", "Please make sure that your date format is: yyyy-MM-dd HH:mm."), HttpStatus.BAD_REQUEST);
			}
		}
		eventService.save(event);
		return new ResponseEntity<>(new MessageDTO("Success", "Event successfuly updated!"), HttpStatus.OK);
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
		Event event = eventService.findById(eventId);
		if(event == null || event.isStatus() == false){
			return new ResponseEntity<>(new MessageDTO("Could not find event", "Event with this ID does not exist or is already deleted."), HttpStatus.OK);
		}
		if(!(eventService.getSoldTickets(event.getId()).isEmpty()) && eventIsActive(event.getId())){
			return new ResponseEntity<>(new MessageDTO("Could not delete event", "There are sold tickets for this event, so it can not be deleted. Please wait for event to pass and then try to delete it."), HttpStatus.OK);
		}
		event.setStatus(false);
		eventService.save(event);
		return new ResponseEntity<>(new MessageDTO("Success", "Event successfuly deleted."), HttpStatus.OK);
	}
	
	private void clearEventLocation(Event event){
		for(Maintenance m : event.getMaintenances()){
			for(LeasedZone lz : m.getLeasedZones()){
				for(Ticket t : lz.getTickets()){
					t.setZone(null);
					ticketService.save(t);
					ticketService.remove(t.getId());
				}
				lz.setMaintenance(null);
				lz.setZone(null);
				leasedZoneService.save(lz);
				leasedZoneService.remove(lz.getId());
			}
			maintenanceService.remove(m.getId());
		}
	}
	
	private boolean eventIsActive(long eventId){
		Maintenance maintenance = maintenanceService.getLastMaintenanceOfEvent(eventId);
		if(maintenance.getMaintenanceDate().before(new Date()) || maintenance.getEvent().isStatus() == false){
			return false;
		}
		return true;
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
	
	
	
}

