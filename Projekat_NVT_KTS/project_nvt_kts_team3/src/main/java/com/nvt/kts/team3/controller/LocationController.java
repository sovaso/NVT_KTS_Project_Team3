package com.nvt.kts.team3.controller;

import java.util.List;

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

import com.nvt.kts.team3.dto.LocationDTO;
import com.nvt.kts.team3.dto.LocationZoneDTO;
import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationController {

	@Autowired
	private LocationService locationService;
	
	@Autowired
	private LocationZoneService locationZoneService;
	
	@PostMapping(value = "/createLocation", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> createLocation(@RequestBody LocationDTO locationDTO){
		Location location = new Location();
		location.setStatus(true);
		location.setName(locationDTO.getName());
		location.setAddress(locationDTO.getAddress());
		if(locationService.findByNameAndAddress(location.getName(), location.getAddress()) != null){
			return new ResponseEntity<>(new MessageDTO("Unable to create location", "Location with this name and address already exists."), HttpStatus.OK);
		}
		location.setDescription(locationDTO.getDescription());
		
		if(locationDTO.getLocationZone() == null || locationDTO.getLocationZone().isEmpty()){
			return new ResponseEntity<>(new MessageDTO("Unable to create location", "Location zones are not valid."), HttpStatus.BAD_REQUEST);
		}
		
		try {
			for(LocationZoneDTO lz : locationDTO.getLocationZone()){
				if(lz.isMatrix() && lz.getCol() > 0 && lz.getRow() > 0){
					int capacity = lz.getCol() * lz.getRow();
					LocationZone newZone = new LocationZone();
					newZone.setName(lz.getName());
					newZone.setColNumber(lz.getCol());
					newZone.setRowNumber(lz.getRow());
					newZone.setCapacity(capacity);
					newZone.setMatrix(true);
					newZone.setLocation(location);
					location.getLocationZones().add(newZone);
				}
				else if(lz.isMatrix() == false && lz.getCapacity() > 0){
					LocationZone newZone = new LocationZone();
					newZone.setName(lz.getName());
					newZone.setColNumber(0);
					newZone.setRowNumber(0);
					newZone.setCapacity(lz.getCapacity());
					newZone.setMatrix(false);
					newZone.setLocation(location);
					location.getLocationZones().add(newZone);
				}
				else{
					return new ResponseEntity<>(new MessageDTO("Unable to create location", "Location zones are not valid."), HttpStatus.BAD_REQUEST);
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageDTO("Unable to create location", "Location zones are not valid."), HttpStatus.BAD_REQUEST);
		}
		
		location = locationService.save(location);
		for(LocationZone lz : location.getLocationZones()){
			locationZoneService.save(lz);
		}
		return new ResponseEntity<>(new MessageDTO("Success", "Location successfuly created!"), HttpStatus.CREATED);
	}
	
	
	@PostMapping(value = "/updateLocation", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> updateLocation(@RequestBody LocationDTO locationDTO){
		Location location = locationService.findById(locationDTO.getId());
		if(location == null){
			return new ResponseEntity<>(new MessageDTO("Could not find location", "Location with this ID does not exist."), HttpStatus.NOT_FOUND);
		}
		if(location.isStatus() == false){
			return new ResponseEntity<>(new MessageDTO("Location is deleted", "Deleted locations can not be updated."), HttpStatus.OK);
		}
		
		Location testName = locationService.findByNameAndAddress(locationDTO.getName(), locationDTO.getAddress());
		if(testName != null && testName.getId() != locationDTO.getId()){
			return new ResponseEntity<>(new MessageDTO("Could not update location", "Location with this name and address already exists."), HttpStatus.OK);
		}
		location.setDescription(locationDTO.getDescription());
		location.setName(locationDTO.getName());
		location.setAddress(locationDTO.getAddress());
		
		if(locationDTO.getLocationZone() == null || locationDTO.getLocationZone().isEmpty()){
			return new ResponseEntity<>(new MessageDTO("Could not update location", "Location zones are not valid. Make sure to have at least 1 location zone."), HttpStatus.BAD_REQUEST);
		}
		locationService.save(location);
		
		boolean updatable = true;
		
		if(locationService.getActiveEvents(location.getId()) != null && locationService.getActiveEvents(location.getId()).isEmpty() == false){
			updatable = false;
		}
		
		if(updatable){
			try {
				for(LocationZoneDTO lz : locationDTO.getLocationZone()){
					LocationZone zone = locationZoneService.findById(lz.getId());
					if(zone != null){
						boolean changed = false;
						if(lz.getName().equals("") == false && lz.getName().equals(zone.getName()) == false){
							zone.setName(lz.getName());
							changed = true;
						}
						if(lz.isMatrix() && lz.getCol() > 0 && lz.getRow() > 0){
							int capacity = lz.getCol() * lz.getRow();
							if(capacity != zone.getCapacity()){
								changed = true;
							}
							zone.setColNumber(lz.getCol());
							zone.setRowNumber(lz.getRow());
							zone.setCapacity(capacity);
							if(zone.isMatrix() == false){
								changed = true;
							}
							zone.setMatrix(true);
						}
						else if(lz.isMatrix() == false && lz.getCapacity() > 0){
							if(zone.isMatrix() == true){
								changed = true;
							}
							zone.setColNumber(0);
							zone.setRowNumber(0);
							if(zone.getCapacity() != lz.getCapacity()){
								zone.setCapacity(lz.getCapacity());
								changed = true;
							}
							zone.setMatrix(false);
						}
						else{
							return new ResponseEntity<>(new MessageDTO("Could not update location", "Location zones are not valid."), HttpStatus.BAD_REQUEST);
						}
						if(changed){
							locationZoneService.save(zone);
						}
					}
				}
			} catch (Exception e) {
				return new ResponseEntity<>(new MessageDTO("Unable to update location zones", "Location zones are not valid."), HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<>(new MessageDTO("Success", "Location successfuly updated!"), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getLocation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Location> getLocation(@PathVariable(value = "id") Long locationId){
		Location location = locationService.findById(locationId);
		if(location != null && location.isStatus()){
			return new ResponseEntity<>(location, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(value = "/getAllLocations", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Location>> getAllLocations(){
		List<Location> locations = locationService.findAll();
		return new ResponseEntity<>(locations, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getActiveLocations", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Location>> getActiveLocations(){
		List<Location> locations = locationService.findAllActive();
		return new ResponseEntity<>(locations, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/deleteLocation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> deleteLocation(@PathVariable(value = "id") Long locationId){
		List<Event> activeEvents = locationService.getActiveEvents(locationId);
		Location location = locationService.findById(locationId);
		if(location == null){
			return new ResponseEntity<>(new MessageDTO("Not found", "Location with this ID does not exist."), HttpStatus.NOT_FOUND);
		}
		if(location.isStatus() == false){
			return new ResponseEntity<>(new MessageDTO("Location deleted", "Location is already deleted."), HttpStatus.OK);
		}
		if(activeEvents != null && activeEvents.isEmpty() == false){
			return new ResponseEntity<>(new MessageDTO("Unable to delete location", "Location can not be deleted because there are active events on this location."), HttpStatus.OK);
		}
		
		location.setStatus(false);
		locationService.save(location);
		return new ResponseEntity<>(new MessageDTO("Success", "Location successfully deleted!."), HttpStatus.OK);
	}
}
