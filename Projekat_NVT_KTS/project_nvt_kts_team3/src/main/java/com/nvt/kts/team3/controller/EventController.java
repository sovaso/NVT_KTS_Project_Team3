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
		
		Date maintenanceDate = null;
		Date endDate = null;
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		Calendar expiry = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 7);
		Date validDate = calendar.getTime();
		for(MaintenanceDTO dates : eventDTO.getMaintenance()){
			maintenanceDate = null;
			try {
				maintenanceDate = sdf.parse(dates.getStartDate());
				endDate = sdf.parse(dates.getEndDate());
				if(maintenanceDate.before(validDate) || maintenanceDate.before(today)){
					return new ResponseEntity<>(new MessageDTO("Unable to create event", "Event must be created at least 7 days before maintenance and date must be valid."), HttpStatus.OK);
				}
				expiry.setTime(maintenanceDate);
				expiry.add(Calendar.DATE, -3);
				
				Maintenance maintenance = new Maintenance();
				
				ArrayList<Event> locationEvents = locationService.checkIfAvailable(location.getId(), maintenance.getMaintenanceDate(), maintenance.getMaintenanceEndTime());
				if(locationEvents != null && locationEvents.isEmpty() == false){
					return new ResponseEntity<>(new MessageDTO("Unable to create event", "Location is not available for specified period."), HttpStatus.OK);
				}
				
				for(LeasedZoneDTO lz : eventDTO.getLocationZones()){
					LocationZone locationZone = locationZoneService.findById(lz.getZoneId());
					if(locationZone == null){
						return new ResponseEntity<>(new MessageDTO("Unable to create event", "Choosen location zone could not be found."), HttpStatus.NOT_FOUND);
					}
					LeasedZone newZone = new LeasedZone();
					newZone.setZone(locationZone);
					newZone.setSeatPrice(lz.getPrice());
					
					if(locationZone.isMatrix()){
						for(int i = 1; i <= locationZone.getColNumber(); i++){
							System.out.println("Proso 3");
							for(int j = 1; j < locationZone.getRowNumber(); j++){
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
				maintenance.setMaintenanceDate(maintenanceDate);
				maintenance.setMaintenanceEndTime(endDate);
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
			Maintenance newMaintenance = maintenanceService.save(m);
			for(LeasedZone lz : m.getLeasedZones()){
				lz.setMaintenance(newMaintenance);
				LeasedZone newZone = leasedZoneService.save(lz);
				for(Ticket t : lz.getTickets()){
					t.setZone(newZone);
					ticketService.save(t);
				}
			}
		}
		return new ResponseEntity<>(new MessageDTO("Success", "Event successfuly created!"), HttpStatus.CREATED);
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
