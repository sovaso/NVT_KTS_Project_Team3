package com.nvt.kts.team3.controller;

import java.util.List;
import java.util.Set;

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

import com.nvt.kts.team3.dto.LocationZoneDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationZoneController {

	@Autowired
	private LocationService locationService;
	
	@Autowired
	private LocationZoneService locationZoneService;
	
	@PostMapping(value = "/createLocationZone", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> createLocationZone(@RequestBody LocationZoneDTO lz){
		Location location = locationService.findById(lz.getLocationId());
		
		if(location == null || location.isStatus() == false){
			return new ResponseEntity<>(new MessageDTO("Unable to create location zone", "Location with this ID does not exist or is deleted."), HttpStatus.NOT_FOUND);
		}
		LocationZone newZone = new LocationZone();
		newZone.setLocation(location);
		try {
			if(lz.isMatrix() && lz.getCol() > 0 && lz.getRow() > 0){
				int capacity = lz.getCol() * lz.getRow();
				newZone.setName(lz.getName());
				newZone.setColNumber(lz.getCol());
				newZone.setRowNumber(lz.getRow());
				newZone.setCapacity(capacity);
				newZone.setMatrix(true);
				newZone.setLocation(location);
				location.getLocationZones().add(newZone);
			}
			else if(lz.isMatrix() == false && lz.getCapacity() > 0){
				newZone.setName(lz.getName());
				newZone.setColNumber(0);
				newZone.setRowNumber(0);
				newZone.setCapacity(lz.getCapacity());
				newZone.setMatrix(false);
				newZone.setLocation(location);
				location.getLocationZones().add(newZone);
			}
			else{
				return new ResponseEntity<>(new MessageDTO("Unable to create location zone", "Request is not valid. Please specify all parameters corectly."), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageDTO("Unable to create location zone", "Request is not valid. Please specify all parameters corectly."), HttpStatus.BAD_REQUEST);
		}
		locationZoneService.save(newZone);
		return new ResponseEntity<>(new MessageDTO("Success", "Location zone successfuly created!"), HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/updateLocationZone", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> updateLocationZone(@RequestBody LocationZoneDTO lz){
		LocationZone zone = locationZoneService.findById(lz.getId());
		
		if(zone == null || zone.getLocation().isStatus() == false){
			return new ResponseEntity<>(new MessageDTO("Unable to update location zone", "Location with this ID does not exist or it's location is deleted from system."), HttpStatus.NOT_FOUND);
		}
		List<Maintenance> activeMaintenances = locationZoneService.getActiveMaintenances(zone.getId());
		if(activeMaintenances != null && activeMaintenances.isEmpty() == false){
			return new ResponseEntity<>(new MessageDTO("Unable to update location zone", "There are active events that take place at this location zone, so it can not be modified now."), HttpStatus.OK);
		}
		try {
			if(lz.isMatrix() && lz.getCol() > 0 && lz.getRow() > 0){
				int capacity = lz.getCol() * lz.getRow();
				zone.setName(lz.getName());
				zone.setColNumber(lz.getCol());
				zone.setRowNumber(lz.getRow());
				zone.setCapacity(capacity);
				zone.setMatrix(true);
			}
			else if(lz.isMatrix() == false && lz.getCapacity() > 0){
				zone.setName(lz.getName());
				zone.setColNumber(0);
				zone.setRowNumber(0);
				zone.setCapacity(lz.getCapacity());
				zone.setMatrix(false);
			}
			else{
				return new ResponseEntity<>(new MessageDTO("Unable to update location zone", "Request is not valid. Please specify all parameters corectly."), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageDTO("Unable to update location zone", "Request is not valid. Please specify all parameters corectly."), HttpStatus.BAD_REQUEST);
		}
		locationZoneService.save(zone);
		return new ResponseEntity<>(new MessageDTO("Success", "Location zone successfuly updated!"), HttpStatus.OK);
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
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping(value = "/deleteLocationZone/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MessageDTO> deleteLocationZone(@PathVariable(value = "id") Long id){
		LocationZone lz = locationZoneService.findById(id);
		if(lz == null){
			return new ResponseEntity<>(new MessageDTO("Location zone was not found", "Location zone with this ID was not found."), HttpStatus.NOT_FOUND);
		}
		List<Maintenance> activeMaintenances = locationZoneService.getActiveMaintenances(id);
		if(activeMaintenances != null && activeMaintenances.isEmpty() == false){
			return new ResponseEntity<>(new MessageDTO("Unable to delete location zone", "There are active events that take place at this location zone, so it can not be modified now."), HttpStatus.OK);
		}
		
		lz.setLocation(null);
		lz.setLeasedZone(null);
		locationZoneService.remove(lz.getId());
		return new ResponseEntity<>(new MessageDTO("Success", "Location zone successfully removed."), HttpStatus.OK);
	}
}
