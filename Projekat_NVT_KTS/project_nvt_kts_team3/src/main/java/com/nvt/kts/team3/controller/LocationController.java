 package com.nvt.kts.team3.controller;

import java.text.ParseException;
import java.util.List;

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

import com.nvt.kts.team3.dto.LocationDTO;
import com.nvt.kts.team3.dto.LocationReportDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.UserTokenState;
import com.nvt.kts.team3.security.auth.JwtAuthenticationRequest;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.ReservationService;

import exception.InvalidLocationZone;
import exception.LocationExists;
import exception.LocationNotChangeable;
import exception.LocationNotFound;


@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="http://localhost:4200", allowedHeaders = "*")
public class LocationController {

	@Autowired
	private LocationService locationService;
	
	@Autowired
	private ReservationService reservationService;
	
	@PostMapping(value = "/createLocation", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> createLocation(@RequestBody LocationDTO locationDTO){
		
		try {
			locationService.save(locationDTO);
			return new ResponseEntity<>(new MessageDTO("Success", "Location successfully created."), HttpStatus.CREATED);

		} catch (LocationExists e) {
			return new ResponseEntity<>(new MessageDTO("Bad request", "Location with that name and address already exist."), HttpStatus.BAD_REQUEST);

		} catch (InvalidLocationZone e) {
			return new ResponseEntity<>(new MessageDTO("Conflict", "Invalid location zone."), HttpStatus.CONFLICT);
		}
		
	}
	
	
	@PostMapping(value = "/updateLocation", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> updateLocation(@RequestBody LocationDTO locationDTO){
		try {
			locationService.update(locationDTO);
			return new ResponseEntity<>(new MessageDTO("Success", "Location successfully updated."), HttpStatus.OK);
		} catch(LocationNotFound e) {
			return new ResponseEntity<>(new MessageDTO("Bad request", "Location not found."), HttpStatus.BAD_REQUEST);
		}catch (LocationExists e) {
			return new ResponseEntity<>(new MessageDTO("Not found", "Location with submited name and address already exist."), HttpStatus.CONFLICT);
		}
		
	}
	
	//***
	@GetMapping(value = "/getLocation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Location> getLocation(@PathVariable(value = "id") Long locationId){
		Location location = locationService.findById(locationId);
		if(location != null && location.isStatus()){
			return new ResponseEntity<>(location, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	//***
	@GetMapping(value = "/getAllLocations", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Location>> getAllLocations(){
		List<Location> locations = locationService.findAll();
		return new ResponseEntity<>(locations, HttpStatus.OK);
	}
	
	//***
	@GetMapping(value = "/getActiveLocations", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Location>> getActiveLocations(){
		List<Location> locations = locationService.findAllActive();
		return new ResponseEntity<>(locations, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/deleteLocation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> deleteLocation(@PathVariable(value = "id") Long locationId){
		try {
			locationService.remove(locationId);
			return new ResponseEntity<>(new MessageDTO("Success", "Location successfully deleted."), HttpStatus.OK);
		}catch(LocationNotFound e) {
			return new ResponseEntity<>(new MessageDTO("Not found", "Location not found."), HttpStatus.NOT_FOUND);
		}catch(LocationNotChangeable e) {
			return new ResponseEntity<>(new MessageDTO("Conflict", "Location not changeable."), HttpStatus.CONFLICT);
		}
		
	}
	
	@GetMapping(value = "/getLocationReport/{location_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getLocationReport(@PathVariable long location_id) throws ParseException {
		try {
			LocationReportDTO retVal=this.locationService.getLocationReport(location_id);
			return new ResponseEntity<>(retVal, HttpStatus.OK);
		}catch(LocationNotFound e) {
			return new ResponseEntity<>(new MessageDTO("Not found", "Location not found."), HttpStatus.NOT_FOUND);
		}
		
	}
}
