package com.nvt.kts.team3.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.dto.LeasedZoneUpdatable;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.LeasedZoneService;
import com.nvt.kts.team3.service.MaintenanceService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="http://localhost:4200", allowedHeaders = "*")
public class LeasedZoneController {
	
	@Autowired
	private MaintenanceService maintenanceService;
	
	@Autowired
	private LeasedZoneService leasedZoneService;
	
	@Autowired
	private EventService eventService;
	
	@PostMapping(value = "/createLeasedZone", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<LeasedZone> createLeasedZone(@RequestBody LeasedZoneDTO lz){
		return new ResponseEntity<>(leasedZoneService.save(lz), HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/updateLeasedZone", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<LeasedZone> updateLeasedZone(@RequestBody LeasedZoneDTO lz){
		leasedZoneService.update(lz);
		return new ResponseEntity<>(leasedZoneService.update(lz), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getLeasedZone/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LeasedZone> getLeasedZone(@PathVariable(value = "id") Long id){
		LeasedZone lz = leasedZoneService.findById(id);
		if(lz == null){
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(lz, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getLeasedZones/{maintenanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<LeasedZone>> getLeasedZones(@PathVariable(value = "maintenanceId") Long maintenanceId){
		Maintenance maintenance = maintenanceService.findById(maintenanceId);
		if(maintenance == null){
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(maintenance.getLeasedZones(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getEventLeasedZones/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LeasedZone>> getEventLeasedZones(@PathVariable(value = "eventId") Long eventId){
		Event event = eventService.findById(eventId);
		if(event == null){
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(leasedZoneService.getEventLeasedZones(eventId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getEventLeasedZonesDto/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LeasedZoneUpdatable>> getEventLeasedZonesDTO(@PathVariable(value = "eventId") Long eventId){
		Event event = eventService.findById(eventId);
		if(event == null){
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(leasedZoneService.getEventLeasedZonesDto(eventId), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/deleteLeasedZone/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> delete(@PathVariable(value = "id") Long id){
		leasedZoneService.remove(id);
		return new ResponseEntity<>(new MessageDTO("Success", "Leased zone successfully deleted."), HttpStatus.OK);
	}
}
