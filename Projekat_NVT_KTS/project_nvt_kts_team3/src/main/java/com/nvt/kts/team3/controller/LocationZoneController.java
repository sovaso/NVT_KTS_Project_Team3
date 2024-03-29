package com.nvt.kts.team3.controller;

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

import com.nvt.kts.team3.dto.LocationZoneDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;

import exception.InvalidLocationZone;
import exception.LocationExists;
import exception.LocationNotFound;
import exception.LocationZoneNotChangeable;
import exception.LocationZoneNotFound;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="http://localhost:4200", allowedHeaders = "*")
public class LocationZoneController {

	@Autowired
	private LocationService locationService;
	
	@Autowired
	private LocationZoneService locationZoneService;
	
	@PostMapping(value = "/createLocationZone", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> createLocationZone(@RequestBody LocationZoneDTO lz){
		System.out.println("USAOOOO U FJU");
		try {
			locationZoneService.save(lz);
			return new ResponseEntity<>(new MessageDTO("Success", "Location zone successfully created."), HttpStatus.OK);
		}catch(LocationNotFound e) {
			return new ResponseEntity<>(new MessageDTO("Not found", "Location of location zone not found."), HttpStatus.OK);
		}catch(InvalidLocationZone e) {
			return new ResponseEntity<>(new MessageDTO("Bad request", "Invalid inputs for location zone."), HttpStatus.OK);
		}
		
	}
	
	@PostMapping(value = "/updateLocationZone", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> updateLocationZone(@RequestBody LocationZoneDTO lz){
		try {
			locationZoneService.update(lz);
			return new ResponseEntity<>(new MessageDTO("Success", "Location zone successfully updated."), HttpStatus.OK);
		}catch(LocationZoneNotFound e) {
			return new ResponseEntity<>(new MessageDTO("Not found", "Location zone not found."), HttpStatus.OK);
		}catch(LocationZoneNotChangeable e) {
			return new ResponseEntity<>(new MessageDTO("Conflict", "Location zone not changeable."), HttpStatus.OK);
		}catch(InvalidLocationZone e) {
			return new ResponseEntity<>(new MessageDTO("Bad request", "Invalid input for location zone."), HttpStatus.OK);
		}
		
	}
	
	@GetMapping(value = "/getLocationZone/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationZone> getLocationZone(@PathVariable(value = "id") Long id){
		LocationZone locationZone = locationZoneService.findById(id);
		if(locationZone != null){
			return new ResponseEntity<>(locationZone, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(value = "/getLocationZones/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<LocationZone>> getLocationZones(@PathVariable(value = "id") Long locationId){
		Location location = locationService.findById(locationId);
		if(location != null){
			return new ResponseEntity<>(location.getLocationZones(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/deleteLocationZone/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> deleteLocationZone(@PathVariable(value = "id") Long id){
		try {
			locationZoneService.remove(id);
			return new ResponseEntity<>(new MessageDTO("Success", "Location zone successfully deleted."), HttpStatus.OK);
		}catch(LocationZoneNotFound e) {
			return new ResponseEntity<>(new MessageDTO("Not found", "Location zone not found."), HttpStatus.OK);
		}catch(LocationZoneNotChangeable e) {
			return new ResponseEntity<>(new MessageDTO("Conflict", "Location zone not changeable."), HttpStatus.OK);
		}
		
	}
	
}
