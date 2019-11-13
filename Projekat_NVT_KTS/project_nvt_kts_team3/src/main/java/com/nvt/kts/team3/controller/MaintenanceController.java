package com.nvt.kts.team3.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nvt.kts.team3.dto.MaintenanceDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.MaintenanceService;
import com.nvt.kts.team3.service.TicketService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class MaintenanceController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private MaintenanceService maintenanceService;
	
	@Autowired
	private TicketService ticketService;
	
	@PostMapping(value = "/createMaintenance", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> createMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) throws ParseException{
		maintenanceService.save(maintenanceDTO);
		return new ResponseEntity<>(new MessageDTO("Success", "Maintenance successfully created."), HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/updateMaintenance", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> updateMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) throws ParseException{
		maintenanceService.updateMaintenance(maintenanceDTO);
		return new ResponseEntity<>(new MessageDTO("Success", "Maintenance successfully updated."), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/deleteMaintenance/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> deleteMaintenance(@PathVariable(value = "id") Long id) {
		maintenanceService.remove(id);
		return new ResponseEntity<>(new MessageDTO("Success", "Maintenance successfully deleted."), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getMaintenance/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Maintenance> getMaintenance(@PathVariable(value = "id") Long maintenanceId){
		Maintenance maintenance = maintenanceService.findById(maintenanceId);
		if(maintenance == null){
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(maintenance, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getMaintenanceTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> getMaintenanceTickets(@PathVariable(value = "id") Long maintenanceId){
		return new ResponseEntity<>(ticketService.getMaintenanceTickets(maintenanceId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getMaintenanceReservedTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> getMaintenanceReservedTickets(@PathVariable(value = "id") Long maintenanceId){
		return new ResponseEntity<>(ticketService.getMaintenanceReservedTickets(maintenanceId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getMaintenanceSoldTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> getMaintenanceSoldTickets(@PathVariable(value = "id") Long maintenanceId){
		return new ResponseEntity<>(ticketService.getMaintenanceSoldTickets(maintenanceId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getMaintenances/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Maintenance>> getMaintenances(@PathVariable(value = "eventId") Long eventId){
		Event event = eventService.findById(eventId);
		return new ResponseEntity<>(event.getMaintenances(), HttpStatus.OK);
	}
}
