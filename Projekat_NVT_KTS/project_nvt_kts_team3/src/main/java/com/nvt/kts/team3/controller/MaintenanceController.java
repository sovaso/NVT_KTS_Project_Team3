package com.nvt.kts.team3.controller;

import java.text.ParseException;
import java.util.Optional;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.MaintenanceService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="http://localhost:4200", allowedHeaders = "*")
public class MaintenanceController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private MaintenanceService maintenanceService;
	
	@PostMapping(value = "/createMaintenance", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> createMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) throws ParseException{
		maintenanceService.save(maintenanceDTO);
		return new ResponseEntity<>(new MessageDTO("Success", "Maintenance successfully created."), HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/updateMaintenance", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> updateMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) throws ParseException{
		maintenanceService.updateMaintenance(maintenanceDTO);
		return new ResponseEntity<>(new MessageDTO("Success", "Maintenance successfully updated."), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/deleteMaintenance/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
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
	
	@GetMapping(value = "/getMaintenances/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Maintenance>> getMaintenances(@PathVariable(value = "eventId") Long eventId){
		Optional<Event> event = eventService.findById(eventId);
		return new ResponseEntity<>(event.get().getMaintenances(), HttpStatus.OK);
	}
	
	//@Scheduled(cron = "0 0 * * * *") //the top of every hour
	@Scheduled(cron = "*/10 * * * * *") //every ten seconds (FOR TEST PURPOSE ONLY)
	public void doHourlyTasks() throws AddressException, MessagingException{
		maintenanceService.checkForExpieredTickets();
		maintenanceService.warnUsersAboutExpiry();
	}
}
