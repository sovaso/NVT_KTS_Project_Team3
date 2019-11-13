package com.nvt.kts.team3.controller;

import java.text.ParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nvt.kts.team3.dto.MessageDTO;

import exception.EventNotChangeable;
import exception.EventNotFound;
import exception.InvalidDate;
import exception.InvalidEventType;
import exception.InvalidLocationZone;
import exception.InvalidPrice;
import exception.LeasedZoneNotChangeable;
import exception.LeasedZoneNotFound;
import exception.LocationExists;
import exception.LocationNotAvailable;
import exception.LocationNotChangeable;
import exception.LocationNotFound;
import exception.LocationZoneNotAvailable;
import exception.LocationZoneNotChangeable;
import exception.LocationZoneNotFound;
import exception.MaintenanceNotChangeable;
import exception.MaintenanceNotFound;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = EventNotChangeable.class)
    protected ResponseEntity<MessageDTO> handleEventNotChangeable() {
		return new ResponseEntity<>(new MessageDTO("Event Not Changeable", "Event is not active, so, it could not be updated."), HttpStatus.NOT_ACCEPTABLE);
    }
	
	@ExceptionHandler(value = EventNotFound.class)
    protected ResponseEntity<MessageDTO> handleEventNotFound() {
		return new ResponseEntity<>(new MessageDTO("Event Not Found", "Event with this ID does not exist."), HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(value = InvalidDate.class)
    protected ResponseEntity<MessageDTO> handleInvalidDate() {
		return new ResponseEntity<>(new MessageDTO("Invalid Date", "Event must be created at least 7 days before maintenance and date must be valid."), HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(value = InvalidEventType.class)
    protected ResponseEntity<MessageDTO> handleInvalidEventType() {
		return new ResponseEntity<>(new MessageDTO("Invalid Event Type", "Choosen event type could not be found."), HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(value = InvalidLocationZone.class)
    protected ResponseEntity<MessageDTO> handleInvalidLocationZone() {
		return new ResponseEntity<>(new MessageDTO("Invalid Location Zone", "Arguments for location zone are not valid."), HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(value = InvalidPrice.class)
    protected ResponseEntity<MessageDTO> handleInvalidPrice() {
		return new ResponseEntity<>(new MessageDTO("Invalid Price", "Please set a price of ticket that is between 1$ and 5000$."), HttpStatus.NOT_ACCEPTABLE);
    }
	
	@ExceptionHandler(value = LeasedZoneNotChangeable.class)
    protected ResponseEntity<MessageDTO> handleLeasedZoneNotChangeable() {
		return new ResponseEntity<>(new MessageDTO("Leased Zone Not Changeable", "There are reserved tickets for this zone, so, it can not be changed."), HttpStatus.NOT_MODIFIED);
    }
	
	@ExceptionHandler(value = LeasedZoneNotFound.class)
    protected ResponseEntity<MessageDTO> handleLeasedZoneNotFound() {
		return new ResponseEntity<>(new MessageDTO("Leased Zone Not Found", "Leased zone with this ID does not exist."), HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(value = LocationNotAvailable.class)
    protected ResponseEntity<MessageDTO> handleLocationNotAvailable() {
		return new ResponseEntity<>(new MessageDTO("Location Not Available", "Location is not available for specified period."), HttpStatus.NOT_ACCEPTABLE);
    }
	
	@ExceptionHandler(value = LocationNotChangeable.class)
    protected ResponseEntity<MessageDTO> handleLocationNotChangeable() {
		return new ResponseEntity<>(new MessageDTO("Location Not Changeable", "Location could not be updated because there are reserved tickets for this location."), HttpStatus.NOT_MODIFIED);
    }
	
	@ExceptionHandler(value = LocationNotFound.class)
    protected ResponseEntity<MessageDTO> handleLocationNotFound() {
		return new ResponseEntity<>(new MessageDTO("Location Not Found", "Choosen location does not exist or is not active."), HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(value = LocationZoneNotAvailable.class)
    protected ResponseEntity<MessageDTO> handleLocationZoneNotAvailable() {
		return new ResponseEntity<>(new MessageDTO("Location Zone Not Available", "Please make sure to choose location zone that exists and belongs to choosen location."), HttpStatus.NOT_ACCEPTABLE);
    }
	
	@ExceptionHandler(value = MaintenanceNotChangeable.class)
    protected ResponseEntity<MessageDTO> handleMaintenanceNotChangeable() {
		return new ResponseEntity<>(new MessageDTO("Maintenance Not Changeable", "Choosen maintenance could not be updated because expiry date has passed."), HttpStatus.NOT_MODIFIED);
    }
	
	@ExceptionHandler(value = MaintenanceNotFound.class)
    protected ResponseEntity<MessageDTO> handleMaintenanceNotFound() {
		return new ResponseEntity<>(new MessageDTO("Maintenance Not Found", "Maintenance with this ID does not exist."), HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(value = ParseException.class)
    protected ResponseEntity<MessageDTO> handleParseException() {
		return new ResponseEntity<>(new MessageDTO("Bad date format", "Please make sure that your date format is: yyyy-MM-dd HH:mm."), HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(value = LocationZoneNotFound.class)
    protected ResponseEntity<MessageDTO> handleLocationZoneNotFound() {
		return new ResponseEntity<>(new MessageDTO("Location Zone Not Found", "Location zone with this ID does not exist."), HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(value = LocationZoneNotChangeable.class)
    protected ResponseEntity<MessageDTO> handleLocationZoneNotChangeable() {
		return new ResponseEntity<>(new MessageDTO("Location Zone Not Changeable", "There are active events that take place on this location zone, so, it can not be changed. Please wait for all events that take place on this location to pass."), HttpStatus.NOT_MODIFIED);
    }
	
	@ExceptionHandler(value = LocationExists.class)
    protected ResponseEntity<MessageDTO> handleLocationExists() {
		return new ResponseEntity<>(new MessageDTO("Location Already Exists", "Location with this name and address already exists."), HttpStatus.NOT_ACCEPTABLE);
    }
}
